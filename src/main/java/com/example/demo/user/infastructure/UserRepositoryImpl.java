package com.example.demo.user.infastructure;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJPARepository userJPARepository;


    @Override
    public Optional<UserEntity> findByIdAndStatus(long id, UserStatus userStatus) {
        return userJPARepository.findByIdAndStatus(id, userStatus);
    }

    @Override
    public Optional<UserEntity> findByEmailAndStatus(String email, UserStatus userStatus) {
        return userJPARepository.findByEmailAndStatus(email, userStatus);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userJPARepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> findById(long id) {
        return userJPARepository.findById(id);
    }
}
