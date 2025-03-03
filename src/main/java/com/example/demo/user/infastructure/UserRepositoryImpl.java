package com.example.demo.user.infastructure;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
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
    public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
        return userJPARepository.findByIdAndStatus(id, userStatus).map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return userJPARepository.findByEmailAndStatus(email, userStatus).map(UserEntity::toModel);
    }

    @Override
    public User save(User user) {
        return userJPARepository.save(UserEntity.from(user)).toModel();
    }

    @Override
    public Optional<User> findById(long id) {
        return userJPARepository.findById(id).map(UserEntity::toModel);
    }

    @Override
    public User getById(long id) {
        return userJPARepository.findById(id).map(UserEntity::toModel).orElseThrow(()-> new ResourceNotFoundException("Posts", id));
    }
}
