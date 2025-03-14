package com.example.demo.medium;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource("file:src/test/java/resources/application.properties")
@SqlGroup({
        @Sql(scripts = "file:src/test/java/resources/sql/user-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "file:src/test/java/resources/sql/post-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "file:src/test/java/resources/sql/delete_all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PostServiceImplTest {

    @Autowired
    private PostServiceImpl postServiceImpl;

    @Test
    public void 게시물을_id로_조회할_수_있다() {
        //given


        //when
        Post postById = postServiceImpl.getPostById(2L);


        //then
        assertThat(postById.getId()).isEqualTo(2L);
    }

    @Test
    public void 잘못된_게시물_조회_시_적절한_에러가_발생한다() {
        //given


        //when



        //then
        assertThatThrownBy(()->{
            Post postById = postServiceImpl.getPostById(31289398127398217L);
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
        Post postEntity = postServiceImpl.create(postCreate);

        //then
        assertThat(postEntity.getId()).isNotNull();
    }

    @Test
    public void 게시물을_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("안녕하세요!")
                .build();
        //when
        postServiceImpl.update(2L, postUpdate);

        //then
        Post postById = postServiceImpl.getPostById(2L);
        assertThat(postById.getContent()).isEqualTo("안녕하세요!");
    }
}