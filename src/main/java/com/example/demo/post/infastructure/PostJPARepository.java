package com.example.demo.post.infastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJPARepository extends JpaRepository<PostEntity, Long> {

}