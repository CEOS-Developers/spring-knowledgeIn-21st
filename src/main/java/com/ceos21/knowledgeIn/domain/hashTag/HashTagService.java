package com.ceos21.knowledgeIn.domain.hashTag;

import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.domain.postHashTag.PostHashTag;
import com.ceos21.knowledgeIn.domain.postHashTag.PostHashTagRepository;
import com.ceos21.knowledgeIn.domain.postHashTag.PostHashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;


    @Transactional
    public HashTag findOrSave(String tag) {
        //해시태그가 있을 경우 해당 해시 태그 반환, 없을 경우 새로 저장하여 반환
        return hashTagRepository.findByName(tag).orElseGet(()->hashTagRepository.save(HashTag.builder().name(tag).build()));

    }
}
