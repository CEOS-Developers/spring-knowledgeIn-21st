package com.ceos21.spring_boot.domain.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;

    
    public Posts createPost(Posts post) {
        return postsRepository.save(post);
    }

    
    public List<Posts> getAllPosts() {
        return postsRepository.findAll();
    }


    public Posts getPostById(Long id) {
        return postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 게시글이 없습니다 : " + id));
    }

   
    public void deletePost(Long id) {
        Posts post = getPostById(id);
        postsRepository.delete(post);
    }
}
