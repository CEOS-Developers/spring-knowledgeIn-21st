package com.knowledgein.springboot.service.postService;

import com.knowledgein.springboot.apiPayload.code.status.ErrorStatus;
import com.knowledgein.springboot.apiPayload.exception.GeneralException;
import com.knowledgein.springboot.domain.Post;
import com.knowledgein.springboot.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {
    private final PostRepository postRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostPage(Integer pageNumber) {
        Page<Post> postPage = postRepository.findAll(PageRequest.of(pageNumber, 5));
        return postPage;
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        return post;
    }
}
