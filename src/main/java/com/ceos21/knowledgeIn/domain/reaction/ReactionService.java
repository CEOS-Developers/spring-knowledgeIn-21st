package com.ceos21.knowledgeIn.domain.reaction;

import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.domain.post.PostService;
import com.ceos21.knowledgeIn.domain.reaction.domain.Reaction;
import com.ceos21.knowledgeIn.domain.reaction.domain.ReactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReactionService {

    private final PostService postService;
    private final ReactionRepository reactionRepository;

    @Transactional
    public Reaction like(Member member, Long postId,ReactionType reactionType) {
        Post post = postService.getPost(postId);
        //이미 반응을 남긴적이 있는지
        Reaction reaction = reactionRepository.findByPostAndMember(post, member);

        //아예 레포에도 없는 경우 save 후 반환
        if(reaction==null) {
            reaction =  Reaction.builder()
                    .post(post)
                    .member(member)
                    .reactionType(reactionType)
                    .build();
            post.setReactionCnt(1,reactionType);
            reactionRepository.save(reaction);
        }
        //반응이 NONE으로 없었을 경우
        else if(reaction.getReactionType().equals(ReactionType.NONE)){
            reaction.setReactionType(reactionType);
            post.setReactionCnt(1,reactionType);
        }
        //이미 눌러둔 반응을 또 누르면
        else if(reaction.getReactionType().equals(reactionType)){
            reaction.setReactionType(ReactionType.NONE);
            post.setReactionCnt(-1,reactionType);
        }
        //이미 눌러둔 반응과 다른 반응을 누르면 해당 반응으로 옮겨줌
        else{
            ReactionType ogReactionType = reaction.getReactionType();
            reaction.setReactionType(reactionType);
            post.setReactionCnt(-1,ogReactionType);
            post.setReactionCnt(1,reactionType);
        }

        return reaction;
    }

}
