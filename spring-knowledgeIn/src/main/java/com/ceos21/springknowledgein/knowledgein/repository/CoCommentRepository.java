package com.ceos21.springknowledgein.knowledgein.repository;

import com.ceos21.springknowledgein.knowledgein.domain.CoComment;
import org.springframework.data.jpa.repository.JpaRepository;

interface CoCommentRepository extends JpaRepository<CoComment, Long> {}
