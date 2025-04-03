package com.ceos21.spring_boot.Repository;

import com.ceos21.spring_boot.Domain.Answer;
import com.ceos21.spring_boot.Domain.Image;
import com.ceos21.spring_boot.Domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPost(Post post);
    List<Image> findByAnswer(Answer answer);
}
