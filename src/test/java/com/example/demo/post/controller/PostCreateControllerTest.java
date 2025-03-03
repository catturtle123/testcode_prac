package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class PostCreateControllerTest {

    @Test
    public void 게시물을_생성_할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(()->10L)
                .build();
        testContainer.userRepository.save(User.builder()
                .email("ak@naver.com")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaa-aaa")
                .nickname("jamey")
                .lastLoginAt(100L)
                .id(1L)
                .build()
        );

        PostCreate postCreate = PostCreate.builder()
                .content("")
                .writerId(1L)
                .build();

        //when
        ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(postCreate);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("");
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("ak@naver.com");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("jamey");
        assertThat(result.getBody().getWriter().getLastLoginAt()).isEqualTo(100L);
        assertThat(result.getBody().getCreatedAt()).isEqualTo(10L);
        assertThat(result.getBody().getId()).isNotNull();
    }
}