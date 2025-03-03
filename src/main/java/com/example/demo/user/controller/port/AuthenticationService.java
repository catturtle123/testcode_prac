package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;

public interface AuthenticationService {

    void login(long id);

    void certificate(long id, String certificationCode);
}
