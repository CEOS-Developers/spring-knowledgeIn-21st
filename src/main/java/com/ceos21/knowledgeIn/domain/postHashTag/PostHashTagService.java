package com.ceos21.knowledgeIn.domain.postHashTag;

import com.ceos21.knowledgeIn.domain.hashTag.HashTag;
import com.ceos21.knowledgeIn.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostHashTagService {
    private final PostHashTagRepository postHashTagRepository;


    @Transactional
    public List<PostHashTag> createPostHashTagList(Post post, List<HashTag> hashTags) {
        return hashTags.stream().map(hashTag-> postHashTagRepository.save(
                PostHashTag.builder()
                    .post(post)
                    .hashTag(hashTag)
                    .build())
        ).toList();
    }

    @Transactional
    public PostHashTag createPostHashTag(Post post, HashTag hashTag) {
        return postHashTagRepository.save(PostHashTag.builder()
                .hashTag(hashTag)
                .post(post)
                .build());
    }


}
