package com.szczuka.marcin.test;

import static com.szczuka.marcin.test.TestUtil.asJsonString;
import static com.szczuka.marcin.test.TestUtil.fromJsonToObject;
import static com.szczuka.marcin.test.TestUtil.getUserDto;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
public class UserIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void userIntegrationTest() throws Exception {

        // create user 1
        MvcResult result = mockMvc.perform(post("/user")
                .content(asJsonString(getUserDto()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Bob")))
                .andReturn();

        UserDto user1 = fromJsonToObject(result.getResponse().getContentAsString(), UserDto.class);

        // create user 2
        result = mockMvc.perform(post("/user")
                .content(asJsonString(getUserDto()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Bob")))
                .andReturn();

        UserDto user2 = fromJsonToObject(result.getResponse().getContentAsString(), UserDto.class);

        // user 1 follow user 2
        mockMvc.perform(post("/user/" + user1.getId() + "/follow/" + user2.getId())
                .content(asJsonString(getUserDto()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // check if user 1 is following user 2
        mockMvc.perform(get("/user/" + user1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followingUsers[0]", is(user2.getId().intValue())));

        // subscribe once again - expected bad request

        // user 1 follow user 2
        mockMvc.perform(post("/user/" + user1.getId() + "/follow/" + user2.getId())
                .content(asJsonString(getUserDto()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getUserNotFound() throws Exception {

        mockMvc.perform(get("/user/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserNotExist() throws Exception {
        // user 1 follow user 2
        mockMvc.perform(post("/user/1/follow/2")
                .content(asJsonString(getUserDto()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
