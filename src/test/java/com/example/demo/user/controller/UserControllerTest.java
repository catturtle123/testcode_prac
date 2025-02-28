package com.example.demo.user.controller;

import com.example.demo.user.domain.UserUpdate;
import com.example.demo.post.infastructure.PostJPARepository;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(scripts = "file:src/test/java/resources/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "file:src/test/java/resources/sql/delete_all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostJPARepository postJPARepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_특정_유저의_정보를_전달받을_수_있다() throws Exception {
        //given

        // when

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("a@naver.com"))
                .andExpect(jsonPath("$.nickname").value("jamey"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.address").doesNotExist());
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api를_호출할_경우_404에러() throws Exception {
        //given

        // when

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/2213123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 2213123를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() throws Exception {
        //given

        // when

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/2/verify")
                        .queryParam("certificationCode", "aaaa-aaaa")
                )
                .andExpect(status().isFound());
        // repository로 STATUS 확인하기
    }

    @Test
    void 사용자는_내_정보를_불러올_때_주소도_가지고올수있다() throws Exception {
        //given

        // when

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/me")
                        .header("EMAIL", "a@naver.com")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("a@naver.com"))
                .andExpect(jsonPath("$.nickname").value("jamey"))
                .andExpect(jsonPath("$.address").value("Seoul"));
    }

    @Test
    void 사용자는_특정유저의_정보를_전달_받을_수있다() throws Exception {
        //given

        // when

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/me")
                        .header("EMAIL", "a@naver.com")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("a@naver.com"))
                .andExpect(jsonPath("$.nickname").value("jamey"))
                .andExpect(jsonPath("$.address").value("Seoul"));
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() throws Exception {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("camey")
                        .address("busan")
                                .build();

        // when

        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/me")
                        .header("EMAIL", "a@naver.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("a@naver.com"))
                .andExpect(jsonPath("$.nickname").value("camey"))
                .andExpect(jsonPath("$.address").value("busan"));
    }

}