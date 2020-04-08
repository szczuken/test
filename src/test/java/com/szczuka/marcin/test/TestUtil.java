package com.szczuka.marcin.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szczuka.marcin.test.dto.CreatePostDto;
import com.szczuka.marcin.test.dto.CreateUserDto;

public class TestUtil {
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJsonToObject(String json, Class<T> valueType) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static CreateUserDto getUserDto() {
        return CreateUserDto.builder().name("Bob").build();
    }

    public static CreatePostDto getPostInput(Long userId, String content) {
        return CreatePostDto.builder()
                .userId(userId)
                .content(content)
                .build();
    }
}
