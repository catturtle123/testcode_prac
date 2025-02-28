package com.example.demo.controller;


import com.example.demo.model.dto.PostUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(scripts = "file:src/test/java/resources/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "file:src/test/java/resources/sql/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "file:src/test/java/resources/sql/delete_all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void id로_게시물을_조회할_수_있다() throws Exception {
        //given


        //when


        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.content").value("wow"));
    }

    @Test
    public void 게시물을_수정할_수_있다() throws Exception {
        //given
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .content("안녕하세요!")
                .build();
        //when


        //the
        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUpdateDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.content").value("안녕하세요!"));
    }
}