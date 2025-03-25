package com.ceos21.knowledgeIn.domain.hate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HatesRepository extends CrudRepository<Hates, Long> {
}
