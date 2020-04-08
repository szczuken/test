package com.szczuka.marcin.test;

import static com.szczuka.marcin.test.TestUtil.asJsonString;
import static com.szczuka.marcin.test.TestUtil.fromJsonToObject;
import static com.szczuka.marcin.test.TestUtil.getPostInput;
import static com.szczuka.marcin.test.TestUtil.getUserDto;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.szczuka.marcin.test.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PostIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void postIntegrationTest() throws Exception {

        // creat user
        MvcResult userResult = mockMvc.perform(post("/user")
                .content(asJsonString(getUserDto()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Bob")))
                .andReturn();

        UserDto user = fromJsonToObject(userResult.getResponse().getContentAsString(), UserDto.class);

        // create post 1
        mockMvc.perform(post("/post")
                .content(asJsonString(getPostInput(user.getId(), "Test content")))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("Test content")))
                .andExpect(jsonPath("$.userId", is(user.getId().intValue())))
                .andExpect(jsonPath("$.userName", is("Bob")));

        // create post 2
        mockMvc.perform(post("/post")
                .content(asJsonString(getPostInput(user.getId(), "Test content 2")))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("Test content 2")))
                .andExpect(jsonPath("$.userId", is(user.getId().intValue())))
                .andExpect(jsonPath("$.userName", is("Bob")));

        // get all users posts from newest
        mockMvc.perform(get("/post/user/"+user.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.[0].content", is("Test content 2")))
                .andExpect(jsonPath("$.[1].content", is("Test content")))
                .andExpect(status().isOk());

    }
}
