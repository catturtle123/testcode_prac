package com.example.demo.repository;

import com.example.demo.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(showSql = true)
@Sql(scripts = "file:src/test/java/resources/sql/user-repository-test-data.sql")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdAndStatus_가_데이터가_없으면_Optinal_empty가_나온다() {
        //given

        //when
        Optional<UserEntity> userId = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

        //then
        assertThat(userId.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus_가_데이터를_제대로_찾아올수_있다() {
        //given

        //when
        Optional<UserEntity> userId = userRepository.findByEmailAndStatus("a@naver.com", UserStatus.ACTIVE);

        //then
        assertThat(userId.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_가_데이터가_없으면_Optinal_empty가_나온다() {
        //given

        //when
        Optional<UserEntity> userId = userRepository.findByEmailAndStatus("a@naver.com", UserStatus.PENDING);

        //then
        assertThat(userId.isEmpty()).isTrue();
    }
}
