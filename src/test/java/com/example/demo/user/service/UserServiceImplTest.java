package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class UserServiceImplTest {

    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.userServiceImpl = UserServiceImpl.builder()
                .certificationService(new CertificationService(fakeMailSender))
                .clockHolder(new TestClockHolder(12312321))
                .userRepository(fakeUserRepository)
                .uuidHolder(new TestUuidHolder("aaaa-aaaa"))
                .build();
        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("a@naver.com")
                .nickname("jamey")
                .address("Seoul")
                .certificationCode("aaaa-aaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build());

        fakeUserRepository.save(User.builder()
                .id(3L)
                .email("b@naver.com")
                .nickname("jamey1")
                .address("Seoul1")
                .certificationCode("aaaa-aaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저만_가져올수_있다() {
        // given
        String email = "a@naver.com";

        // when
        User result = userServiceImpl.getByEmail(email);

        // then
        assertEquals(result.getNickname(), "jamey");
    }

    @Test
    void getByEmail은_PENTDING_상태인_유저는_가져올수_앖다() {
        // given
        String email = "b@naver.com";

        // when

        // then
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.getByEmail(email));
    }

    @Test
    void getById는_ACTIVE_상태인_유저만_가져올수_있다() {
        // given
        String email = "a@naver.com";

        // when
        User result = userServiceImpl.getById(2);

        // then
        assertEquals(result.getNickname(), "jamey");
    }

    @Test
    void getById는_PENTDING_상태인_유저는_가져올수_앖다() {
        // given
        Long id = 3L;

        // when

        // then
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.getById(id));
    }

    @Test
    void create_가_user를_생성_할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("a@lalap.com")
                .nickname("kono")
                .address("kik202-k")
                .build();

        // when
        User result = userServiceImpl.create(userCreate);

        // then
        assertNotNull(result.getId());
        assertEquals(result.getStatus(), UserStatus.PENDING);
        assertEquals(result.getCertificationCode(), "aaaa-aaaa");
    }

    @Test // 다른 값이 안 변하는 지도 확인하면 좋음
    void update_가_user를_수정_할_수_있다() {
        // given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("su")
                .address("root")
                .build();


        // when
        User result = userServiceImpl.update(2, userUpdate);

        // then
        User userEntity = userServiceImpl.getById(2);
        assertNotNull(userEntity.getId());
        assertEquals(userEntity.getNickname(), "su");
        assertEquals(userEntity.getAddress(), "root");
    }

    @Test
    void login_이_인증_할_수_있다() {
        // given


        // when
        userServiceImpl.login(2);

        // then
        User userEntity = userServiceImpl.getById(2);
        Assertions.assertThat(userEntity.getLastLoginAt()).isGreaterThan(0);
        assertEquals(userEntity.getLastLoginAt(), 12312321);
    }

    @Test
    void PENDING상태의_사용자는_인증코드로_ACTIVE_시킬_수_있다() {
        // given


        // when
        userServiceImpl.certificate(3, "aaaa-aaaaa");

        // then
        User userEntity = userServiceImpl.getById(3);
        Assertions.assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING상태의_사용자는_잘못된_인증코드는_거부_할수_있다() {
        // given


        // when


        // then
        assertThrows(CertificationCodeNotMatchedException.class, () -> userServiceImpl.certificate(3, "aaaa-aaaaas"));
    }

}