package com.ceos21.spring_knowledgeIn_21st.domain.user.dao;

import com.ceos21.spring_knowledgeIn_21st.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
