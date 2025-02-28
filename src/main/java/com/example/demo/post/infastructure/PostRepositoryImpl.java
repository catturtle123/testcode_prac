package com.example.demo.post.infastructure;

import com.example.demo.post.service.port.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJPARepository postJPARepository;

    @Override
    public Optional<PostEntity> findById(long id) {
        return postJPARepository.findById(id);
    }

    @Override
    public PostEntity save(PostEntity postEntity) {
        return postJPARepository.save(postEntity);
    }
}
