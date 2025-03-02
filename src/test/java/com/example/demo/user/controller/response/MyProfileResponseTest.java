package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MyProfileResponseTest {

    @Test
    void User로_프로필_응답을_만들_수_있다() {
        //given
        User user = User.builder()
                .email("a@naver.com")
                .nickname("jamey")
                .address("busan")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaa")
                .build();


        //when
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        //then
        Assertions.assertThat(myProfileResponse.getEmail()).isEqualTo("a@naver.com");
        Assertions.assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(myProfileResponse.getAddress()).isEqualTo("busan");
        Assertions.assertThat(myProfileResponse.getNickname()).isEqualTo("jamey");
    }
}