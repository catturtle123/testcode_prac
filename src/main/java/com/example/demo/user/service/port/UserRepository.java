package com.example.demo.user.service.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infastructure.UserEntity;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByIdAndStatus(long id, UserStatus userStatus);

    Optional<User> findByEmailAndStatus(String email, UserStatus userStatus);

    User save(User userEntity);

    Optional<User> findById(long id);

    User getById(long writerId);
}
