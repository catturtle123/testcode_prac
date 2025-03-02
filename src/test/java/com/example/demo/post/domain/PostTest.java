package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    public void PostCreate으로_게시물을_만들_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloworld")
                .build();

        User writer = User.builder()
                .id(1L)
                .email("k@naver.com")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .nickname("k")
                .certificationCode("aaaa-aaaa")
                .build();

        //when
        Post post = Post.from(postCreate, writer);

        //then
        Assertions.assertThat(post.getContent()).isEqualTo("helloworld");
        Assertions.assertThat(post.getWriter().getEmail()).isEqualTo("k@naver.com");
        Assertions.assertThat(post.getWriter().getNickname()).isEqualTo("k");
        Assertions.assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        Assertions.assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaa-aaaa");
    }

}