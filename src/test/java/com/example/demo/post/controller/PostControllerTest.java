package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class PostControllerTest {

    @Test
    public void id로_게시물을_조회할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(()->10L)
                .build();
        User writer = testContainer.userRepository.save(User.builder()
                .email("ak@naver.com")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaa-aaa")
                .nickname("jamey")
                .lastLoginAt(100L)
                .id(1L)
                .build()
        );
        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("content")
                .writer(writer)
                .createdAt(10L)
                .modifiedAt(10L)
                .build()
        );


        //when
        ResponseEntity<PostResponse> result = testContainer.postController.getPostById(1L);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("content");
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("ak@naver.com");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("jamey");
        assertThat(result.getBody().getWriter().getLastLoginAt()).isEqualTo(100L);
        assertThat(result.getBody().getCreatedAt()).isEqualTo(10L);
        assertThat(result.getBody().getId()).isNotNull();
    }

    @Test
    public void 게시물을_수정할_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(()->10L)
                .build();
        User writer = testContainer.userRepository.save(User.builder()
                .email("ak@naver.com")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaa-aaa")
                .nickname("jamey")
                .lastLoginAt(100L)
                .id(1L)
                .build()
        );
        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("content")
                .writer(writer)
                        .createdAt(10L)
                        .modifiedAt(10L)
                .build()
        );

        PostUpdate postUpdate = PostUpdate.builder()
                .content("a")
                .build();

        //when
        ResponseEntity<PostResponse> result = testContainer.postController.updatePost(1L, postUpdate);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("a");
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("ak@naver.com");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("jamey");
        assertThat(result.getBody().getWriter().getLastLoginAt()).isEqualTo(100L);
        assertThat(result.getBody().getCreatedAt()).isEqualTo(10L);
        assertThat(result.getBody().getId()).isNotNull();
    }
}