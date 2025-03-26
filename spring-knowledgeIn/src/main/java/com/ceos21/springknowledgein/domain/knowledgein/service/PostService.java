package com.ceos21.springknowledgein.domain.knowledgein.service;

import com.ceos21.springknowledgein.domain.knowledgein.Dto.PostDto;
import com.ceos21.springknowledgein.domain.knowledgein.repository.Post;
import com.ceos21.springknowledgein.domain.knowledgein.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post createPost(PostDto dto) {
        Post post = new Post(dto.getTitle(), dto.getContent(), dto.getMember());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
