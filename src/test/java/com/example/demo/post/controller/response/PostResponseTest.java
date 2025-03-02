package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostResponseTest {

    @Test
    void Post로_응답을_만들수있다() {
        //given
        Post post = Post.builder()
                .content("helloworld")
                .writer(User.builder()
                        .id(1L)
                        .email("k@naver.com")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .nickname("k")
                        .certificationCode("aaaa-aaaa")
                        .build())
                .build();

        //when
        PostResponse postResponse = PostResponse.from(post);

        //then
        Assertions.assertThat(postResponse.getContent()).isEqualTo("helloworld");
        Assertions.assertThat(postResponse.getWriter().getEmail()).isEqualTo("k@naver.com");
        Assertions.assertThat(postResponse.getWriter().getNickname()).isEqualTo("k");
    }

}