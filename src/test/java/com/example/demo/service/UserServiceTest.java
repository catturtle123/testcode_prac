package com.example.demo.service;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("file:src/test/java/resources/application.properties")
@SqlGroup({
        @Sql(scripts = "file:src/test/java/resources/sql/user-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
                @Sql(scripts = "file:src/test/java/resources/sql/delete_all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저만_가져올수_있다() {
        // given
        String email = "a@naver.com";

        // when
        UserEntity result = userService.getByEmail(email);

        // then
        assertEquals(result.getNickname(), "jamey");
    }

    @Test
    void getByEmail은_PENTDING_상태인_유저는_가져올수_앖다() {
        // given
        String email = "b@naver.com";

        // when

        // then
        assertThrows(ResourceNotFoundException.class, () -> userService.getByEmail(email));
    }

    @Test
    void getById는_ACTIVE_상태인_유저만_가져올수_있다() {
        // given
        String email = "a@naver.com";

        // when
        UserEntity result = userService.getById(2);

        // then
        assertEquals(result.getNickname(), "jamey");
    }

    @Test
    void getById는_PENTDING_상태인_유저는_가져올수_앖다() {
        // given
        Long id = 3L;

        // when

        // then
        assertThrows(ResourceNotFoundException.class, () -> userService.getById(id));
    }

    @Test
    void create_가_user를_생성_할_수_있다() {
        // given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("a@lalap.com")
                .nickname("kono")
                .address("kik202-k")
                .build();

        Mockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // when
        UserEntity result = userService.create(userCreateDto);

        // then
        assertNotNull(result.getId());
        assertEquals(result.getStatus(), UserStatus.PENDING);
//        assertEquals(result.getCertificationCode(), "??");
    }

    @Test // 다른 값이 안 변하는 지도 확인하면 좋음
    void update_가_user를_수정_할_수_있다() {
        // given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .nickname("su")
                .address("root")
                .build();


        // when
        UserEntity result = userService.update(2, userUpdateDto);

        // then
        UserEntity userEntity = userService.getById(2);
        assertNotNull(userEntity.getId());
        assertEquals(userEntity.getNickname(), "su");
        assertEquals(userEntity.getAddress(), "root");
    }

    @Test
    void login_이_인증_할_수_있다() {
        // given


        // when
        userService.login(2);

        // then
        UserEntity userEntity = userService.getById(2);
        Assertions.assertThat(userEntity.getLastLoginAt()).isGreaterThan(0);
//        assertEquals(userEntity.getLastLoginAt(), ㅠㅠ);
    }

    @Test
    void PENDING상태의_사용자는_인증코드로_ACTIVE_시킬_수_있다() {
        // given


        // when
        userService.verifyEmail(3, "aaaa-aaaaa");

        // then
        UserEntity userEntity = userService.getById(3);
        Assertions.assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING상태의_사용자는_잘못된_인증코드는_거부_할수_있다() {
        // given


        // when


        // then
        assertThrows(CertificationCodeNotMatchedException.class, () -> userService.verifyEmail(3, "aaaa-aaaaas"));
    }

}