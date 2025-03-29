package com.ceos21.spring_boot.domain.tags;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagsService {

    private final TagsRepository tagsRepository;

    public Tags createTag(Tags tag) {
        return tagsRepository.save(tag);
    }

    public List<Tags> getAllTags() {
        return tagsRepository.findAll();
    }

    public Tags getTagById(Long id) {
        return tagsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 태그가 없습니다 : " + id));
    }

    public void deleteTag(Long id) {
        Tags tag = getTagById(id);
        tagsRepository.delete(tag);
    }
}
