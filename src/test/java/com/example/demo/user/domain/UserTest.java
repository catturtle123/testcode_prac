package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void User는_UserCreate_객체로_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("a@naver.com")
                .nickname("jamey")
                .address("suncheon")
                .build();

        //when
        User user = User.from(userCreate, new TestUuidHolder("aaaa-aaaa-aaaa"));

        //then
        Assertions.assertThat(user.getId()).isNull();
        Assertions.assertThat(user.getEmail()).isEqualTo("a@naver.com");
        Assertions.assertThat(user.getNickname()).isEqualTo("jamey");
        Assertions.assertThat(user.getAddress()).isEqualTo("suncheon");
        Assertions.assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        Assertions.assertThat(user.getCertificationCode()).isEqualTo("aaaa-aaaa-aaaa");
    }

    @Test
    public void User는_UserUpdate_객체로_수정할_수_있다() {
        //given
        User user = User.builder()
                .id(1L)
                .nickname("jamey2")
                .email("n@naver.com")
                .address("a")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaa-aaa")
                .build();

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("jamey")
                .address("Gwang-ju")
                .build();

        //when
        user = user.update(userUpdate);

        //then
        Assertions.assertThat(user.getId()).isEqualTo(1L);
        Assertions.assertThat(user.getEmail()).isEqualTo("n@naver.com");
        Assertions.assertThat(user.getNickname()).isEqualTo("jamey");
        Assertions.assertThat(user.getAddress()).isEqualTo("Gwang-ju");
        Assertions.assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        Assertions.assertThat(user.getLastLoginAt()).isEqualTo(100L);
        Assertions.assertThat(user.getCertificationCode()).isEqualTo("aaa-aaa");
    }

    @Test
    public void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
        //given
        User user = User.builder()
                .id(1L)
                .nickname("jamey2")
                .email("n@naver.com")
                .address("a")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaa-aaa")
                .build();

        //when
        user = user.login(new TestClockHolder(1000L));

        //then
        Assertions.assertThat(user.getLastLoginAt()).isEqualTo(1000L);
    }

    @Test
    public void 유효한_인증_코드로_계정_활성화_가능() {
        // given
        User user = User.builder()
                .id(1L)
                .nickname("jamey2")
                .email("n@naver.com")
                .address("a")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaa-aaa")
                .build();

        //when
        user = user.certificate("aaa-aaa");


        //then
        Assertions.assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void 잘못된_인증_코드로_활성화_하면_에러를_던진다() {
        // given
        User user = User.builder()
                .id(1L)
                .nickname("jamey2")
                .email("n@naver.com")
                .address("a")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaa-aaa")
                .build();

        //when

        //then
        Assertions.assertThatThrownBy(()-> user.certificate("aaa-aaaa")).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}