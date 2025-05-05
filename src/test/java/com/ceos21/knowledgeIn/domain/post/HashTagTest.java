package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.repository.HashTagRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class HashTagTest {
    @Autowired
    HashTagRepository repository;
    @Autowired
    EntityManager em;

    @Test
    void createHashTag() {
        String content = "hallo content";
        HashTag hashTag = HashTag.builder()
                .content(content)
                .build();

        //when
        Long id = repository.save(hashTag).getId();
        em.flush();

        //then
        Optional<HashTag> findHashTag = repository.findById(id);
        assertEquals(findHashTag.get().getContent(),content);
    }
    @Test
    void deleteHashTag() {
        String content = "hallo content";
        HashTag hashTag = HashTag.builder()
                .content(content)
                .build();

        //when
        Long id = repository.save(hashTag).getId();
        em.flush();
        repository.deleteById(id);
        //then
        Optional<HashTag> findHashTag = repository.findById(id);
        assertTrue(findHashTag.isEmpty());
    }
}