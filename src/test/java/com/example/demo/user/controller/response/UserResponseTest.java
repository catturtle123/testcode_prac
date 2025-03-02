package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    public void User으로_응답을_생성할_수_있다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("a@naver.com")
                .nickname("jamey")
                .address("busan")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaa")
                .lastLoginAt(100L)
                .build();

        //when
        UserResponse userResponse = UserResponse.from(user);

        //then
        Assertions.assertThat(userResponse.getId()).isEqualTo(1L);
        Assertions.assertThat(userResponse.getEmail()).isEqualTo("a@naver.com");
        Assertions.assertThat(userResponse.getNickname()).isEqualTo("jamey");
        Assertions.assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
    }

}