package com.example.demo.post.infastructure;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJPARepository postJPARepository;

    @Override
    public Optional<Post> findById(long id) {
        return postJPARepository.findById(id).map(PostEntity::toModel);
    }

    @Override
    public Post save(Post post) {
        return postJPARepository.save(PostEntity.from(post)).toModel();
    }
}
