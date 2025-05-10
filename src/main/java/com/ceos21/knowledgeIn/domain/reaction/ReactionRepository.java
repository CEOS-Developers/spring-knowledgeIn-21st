package com.ceos21.knowledgeIn.domain.reaction;

import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.domain.reaction.domain.Reaction;
import com.ceos21.knowledgeIn.domain.reaction.domain.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction,Long> {

    Reaction findByPostAndMember(Post post, Member member);
}
