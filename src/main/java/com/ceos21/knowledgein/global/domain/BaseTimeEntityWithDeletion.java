package com.ceos21.knowledgein.global.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class BaseTimeEntityWithDeletion extends BaseTimeEntity {
    private LocalDateTime deletedAt;
}
