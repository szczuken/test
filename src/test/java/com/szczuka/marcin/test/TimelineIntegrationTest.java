package com.szczuka.marcin.test;

import static com.szczuka.marcin.test.TestUtil.asJsonString;
import static com.szczuka.marcin.test.TestUtil.fromJsonToObject;
import static com.szczuka.marcin.test.TestUtil.getPostInput;
import static com.szczuka.marcin.test.TestUtil.getUserDto;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.szczuka.marcin.test.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TimelineIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void timelineTest() throws Exception {
        // create user 1
        UserDto user1 = fromJsonToObject(createUser().getResponse().getContentAsString(), UserDto.class);

        // create user 2
        UserDto user2 = fromJsonToObject(createUser().getResponse().getContentAsString(), UserDto.class);

        // create user 3
        UserDto user3 = fromJsonToObject(createUser().getResponse().getContentAsString(), UserDto.class);

        // user 1 follow user 2
        followUser(user1.getId(), user2.getId());

        // user 1 follow user 3
        followUser(user1.getId(), user3.getId());

        // user 2 follow user 1
        followUser(user2.getId(), user1.getId());

        // user 2 follow user 3
        followUser(user2.getId(), user3.getId());

        // user 3 is not follow anyone

        // create post user 1
        createPost(user1.getId());

        // create post user 2
        createPost(user2.getId());

        // create post user 3
        createPost(user3.getId());

        // get timeline for user 1 should return post from user 3 and 2 in that order
        mockMvc.perform(get("/post/user/"+user1.getId()+"/timeline"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].userId", is(user3.getId().intValue())))
                .andExpect(jsonPath("$.[1].userId", is(user2.getId().intValue())));

        // get timeline for user 2 should return post from user 3 and 1 in that order
        mockMvc.perform(get("/post/user/"+user2.getId()+"/timeline"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].userId", is(user3.getId().intValue())))
                .andExpect(jsonPath("$.[1].userId", is(user1.getId().intValue())));

        // get timeline for user 3 should return empty list
        mockMvc.perform(get("/post/user/"+user3.getId()+"/timeline"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    private MvcResult createUser() throws Exception {
        return mockMvc.perform(post("/user")
                .content(asJsonString(getUserDto()))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    private void followUser(Long followerId, Long followedId) throws Exception {
        mockMvc.perform(post("/user/"+followerId+"/follow/"+followedId)
                .content(asJsonString(getUserDto()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private void createPost(Long userId) throws Exception {
        mockMvc.perform(post("/post")
                .content(asJsonString(getPostInput(userId, "Test content")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
