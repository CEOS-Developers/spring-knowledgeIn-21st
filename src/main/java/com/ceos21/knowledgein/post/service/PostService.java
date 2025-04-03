package com.ceos21.knowledgein.post.service;

import com.ceos21.knowledgein.post.config.FileStore;
import com.ceos21.knowledgein.post.domain.HashTag;
import com.ceos21.knowledgein.post.domain.Image;
import com.ceos21.knowledgein.post.domain.Post;
import com.ceos21.knowledgein.post.dto.PostDto;
import com.ceos21.knowledgein.post.dto.request.RequestCreatePost;
import com.ceos21.knowledgein.post.dto.request.RequestUpdatePost;
import com.ceos21.knowledgein.post.exception.PostException;
import com.ceos21.knowledgein.post.repository.PostRepository;
import com.ceos21.knowledgein.user.domain.UserEntity;
import com.ceos21.knowledgein.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ceos21.knowledgein.post.exception.PostErrorCode.POST_NOT_FOUND;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final FileStore fileStore;
    private final UserService userService;

    @Transactional
    public PostDto createPost(RequestCreatePost request, Long userId) {

        List<Image> images = storeImage(request.images());
        List<HashTag> hashTags = createHashTagEntity(request.hashTags());
        UserEntity user = userService.findUserByIdReturnEntity(userId);

        Post post = createNewPostEntity(request, hashTags, images, user);
        postRepository.save(post);

        return PostDto.from(post);
    }

    public List<PostDto> findAllPost() {
        List<Post> allPosts = postRepository.findAll();
        return PostDto.from(allPosts);
    }

    public PostDto findOnePost(Long postId) {
        Post post = findPostOrThrow(postId);
        return PostDto.from(post);
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = findPostOrThrow(postId);
        postRepository.delete(post);
    }

    @Transactional
    public PostDto updatePost(Long postId, RequestUpdatePost request) {
        // TODO: 로직 개선 - request 검증 로직 추가
        Post post = findPostOrThrow(postId);

        List<Image> images = storeImage(request.images());
        List<HashTag> hashTags = createHashTagEntity(request.hashTags());

        Post updated = post.update(request.title(), request.content(), hashTags, images);
        return PostDto.from(updated);
    }


    protected Post findPostOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    private Post createNewPostEntity(RequestCreatePost request, List<HashTag> hashTags, List<Image> images, UserEntity user) {
        return Post.of(request.title(),
                       request.content(),
                       request.nicknamePublic(),
                       hashTags,
                       images,
                       user
        );
    }

    protected List<HashTag> createHashTagEntity(List<String> tags) {
        return HashTag.of(ofNullable(tags)
                                  .orElse(List.of()));
    }

    protected List<Image> storeImage(List<MultipartFile> inputImages) {
        return fileStore.storeFiles(ofNullable(inputImages)
                                            .orElse(List.of()));
    }

}
