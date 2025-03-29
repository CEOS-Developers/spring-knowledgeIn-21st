package com.knowledgein.springboot.service.postService;

import com.knowledgein.springboot.domain.Post;
import org.springframework.data.domain.Page;

public interface PostQueryService {
    Page<Post> getPostPage(Integer pageNumber);
    Post getPost(Long postId);
}
