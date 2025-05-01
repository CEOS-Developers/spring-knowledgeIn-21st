package com.ceos21.knowledgein.redis.repository;

import com.ceos21.knowledgein.redis.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
