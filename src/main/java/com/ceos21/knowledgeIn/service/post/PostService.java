package com.ceos21.knowledgeIn.service.post;

import com.ceos21.knowledgeIn.controller.dto.post.PostCreateDTO;
import com.ceos21.knowledgeIn.controller.dto.post.PostModifyDTO;
import com.ceos21.knowledgeIn.controller.dto.post.PostResponseDTO;
import com.ceos21.knowledgeIn.domain.post.Image;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.domain.post.PostType;
import com.ceos21.knowledgeIn.domain.user.User;
import com.ceos21.knowledgeIn.repository.ImageRepository;
import com.ceos21.knowledgeIn.repository.post.PostRepository;
import com.ceos21.knowledgeIn.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final HashTagService hashTagService;
    private final ImageRepository imageRepository;

    //create
    @Transactional
    public Long createPost(PostCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        Long postId = null;

        if (dto.getPostType().equals(PostType.QUESTION)) {      //질문글 생성
            postId = postRepository.save(Post.createQuestion(dto.getTitle(), dto.getContent(), user)).getId();
        } else if (dto.getPostType().equals(PostType.ANSWER)) { //답변글 생성
            Post parentPost = postRepository.findById(dto.getParentPostId()).orElseThrow(() -> new IllegalArgumentException("parent post not exists"));

            postId = postRepository.save(Post.createAnswer(dto.getTitle(), dto.getContent(), parentPost, user)).getId();
        }

        return postId;
    }

    //read
    public PostResponseDTO findPost(Long id) {
        Post findPost = postRepository.findById(id).orElseThrow();
        return PostResponseDTO.from(findPost);
    }

    public List<PostResponseDTO> findAllPosts() {
        List<PostResponseDTO> dtos = postRepository.findAll()
                .stream()
                .map(post -> PostResponseDTO.from(post))
                .collect(Collectors.toList());

        return dtos;
    }

    //update
    @Transactional
    public PostResponseDTO modifyPost(PostModifyDTO dto, Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow();

        findPost.updatePost(dto.getTitle(), dto.getContent());

        //HashTag 수정
        hashTagService.createOrFindHashTags(dto.getNewHashTagStrings(), postId);

        //Image 수정
        List<Image> findImages = dto.getImageIds().isEmpty() ? new ArrayList<>() : imageRepository.findAllById(dto.getImageIds());
        findPost.getImages().clear();

        findImages.forEach(image -> findPost.getImages().add(image));

        postRepository.save(findPost);

        return PostResponseDTO.from(findPost);
    }
    //delete
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }


}
