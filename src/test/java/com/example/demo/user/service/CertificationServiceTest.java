package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CertificationServiceTest {

    @Test
    public void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지() {
        //given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);

        //when
        certificationService.send("f@naver.com", 2, "aaaa-aaaa");

        //then
        assertThat(fakeMailSender.email).isEqualTo("f@naver.com");
        assertThat(fakeMailSender.title).isEqualTo("Please your email address");
        assertThat(fakeMailSender.content).isEqualTo("Please Clock the following link to certify your email address: http://localhost:8080/api/users/2/verify?certificationCode=aaaa-aaaa");
    }

}