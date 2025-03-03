package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_전달받을_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();

        testContainer.userRepository.save(User.builder()
                .email("ak@naver.com")
                .nickname("jamey")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaa")
                .lastLoginAt(100L)
                .build()
        );

        // when
        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("ak@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jamey");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api를_호출할_경우_404에러() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();

        // when

        //then
        assertThatThrownBy(() -> testContainer.userController.getUserById(1L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();

        testContainer.userRepository.save(User.builder()
                .email("ak@naver.com")
                .nickname("jamey")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaa-aaaa")
                .lastLoginAt(100L)
                .build()
        );

        // when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1, "aaaa-aaaa");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.getById(1L).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_인증_코드가_일치하지_않을_때_에러를_내려준다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("ak@naver.com")
                .nickname("jamey")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaa-aaaa")
                .lastLoginAt(100L)
                .build()
        );

        // when

        //then
        assertThatThrownBy(()-> testContainer.userController.verifyEmail(1L, "a")).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 사용자는_내_정보를_불러올_때_주소도_가지고올수있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(()->123L)
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("ak@naver.com")
                .nickname("jamey")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaa")
                .lastLoginAt(100L)
                .build()
        );

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("ak@naver.com");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getEmail()).isEqualTo("ak@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("jamey");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(123L);
        assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
        assertThat(result.getBody().getId()).isEqualTo(1L);
    }

    @Test
    void 사용자는_특정유저의_정보를_전달_받을_수있다() {
        //given

        // when

        //then
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("ak@naver.com")
                .nickname("jamey")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaa-aaaa")
                .lastLoginAt(100L)
                .build()
        );

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo(
                "ak@naver.com",
                UserUpdate.builder()
                        .nickname("camey")
                        .address("busan")
                        .build()
        );

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("ak@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("camey");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
        assertThat(result.getBody().getAddress()).isEqualTo("busan");
        assertThat(result.getBody().getId()).isEqualTo(1L);
    }

}