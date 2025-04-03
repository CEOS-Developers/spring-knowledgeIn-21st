package com.ceos21.spring_boot.domain.likes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;

    public Likes createLike(Likes like) {
        return likesRepository.save(like);
    }

    public List<Likes> getAllLikes() {
        return likesRepository.findAll();
    }

    public Likes getLikeById(Long id) {
        return likesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 좋아요가 없습니다 : " + id));
    }

    public void deleteLike(Long id) {
        Likes like = getLikeById(id);
        likesRepository.delete(like);
    }
}
