package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.mock.*;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostServiceTest {

    private PostService postService;

    @BeforeEach
    void init() {
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.postService = PostService.builder()
                .clockHolder(new TestClockHolder(1234L))
                .postRepository(fakePostRepository)
                .userRepository(fakeUserRepository)
                .build();

        User user1 = fakeUserRepository.save(User.builder()
                .id(2L)
                .email("a@naver.com")
                .nickname("jamey")
                .address("Seoul")
                .certificationCode("aaaa-aaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build());

        User user2 = fakeUserRepository.save(User.builder()
                .id(3L)
                .email("b@naver.com")
                .nickname("jamey1")
                .address("Seoul1")
                .certificationCode("aaaa-aaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());

        fakePostRepository.save(Post.builder()
                .id(2L)
                .content("wow")
                .createdAt(1234L)
                .modifiedAt(1234L)
                .writer(user1)
                .build());

        fakePostRepository.save(Post.builder()
                .id(3L)
                .content("zoz")
                .createdAt(1234L)
                .modifiedAt(1234L)
                .writer(user2)
                .build());
    }

    @Test
    public void 게시물을_id로_조회할_수_있다() {
        //given


        //when
        Post postById = postService.getPostById(2L);


        //then
        assertThat(postById.getId()).isEqualTo(2L);
    }

    @Test
    public void 잘못된_게시물_조회_시_적절한_에러가_발생한다() {
        //given


        //when



        //then
        assertThatThrownBy(()->{
            Post postById = postService.getPostById(31289398127398217L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void 게시물을_생성할_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
                        .content("안녕하세요")
                        .writerId(2L)
                        .build();
        //when
        Post postEntity = postService.create(postCreate);

        //then
        assertThat(postEntity.getId()).isNotNull();
        assertThat(postEntity.getCreatedAt()).isEqualTo(1234L);
    }

    @Test
    public void 게시물을_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("안녕하세요!")
                .build();
        //when
        postService.update(2L, postUpdate);

        //then
        Post postById = postService.getPostById(2L);
        assertThat(postById.getContent()).isEqualTo("안녕하세요!");
        assertThat(postById.getModifiedAt()).isEqualTo(1234L);
    }
}