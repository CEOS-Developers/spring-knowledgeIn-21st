package com.ceos21.knowledgeIn.serviceUnitTest;

import com.ceos21.knowledgeIn.domain.common.PostType;
import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.post.Post;
import com.ceos21.knowledgeIn.domain.post.PostService;
import com.ceos21.knowledgeIn.domain.post.dto.PostRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class PostServiceTest {

    @MockitoBean
    private PostService postService;

    @Test
    @DisplayName("질문글 생성 후 수정")
    public void createQuestion() {
        Member member = Member.builder().name("동영배").email("youngship@naver.com").password("1234").phone("01012341234").nickname("태양").build();
        List<String> hashTagNames = new ArrayList<>();
        hashTagNames.add("빅뱅");
        hashTagNames.add("가수");

        PostRequestDTO.QuestionCreateRequestDTO requestDTO = PostRequestDTO.QuestionCreateRequestDTO.builder()
                .title("빅뱅 컴백 할 수 있을까요?")
                .content("저는 하고싶은데..")
                .hashTags(hashTagNames)
                .isAnonymous(false)
                .build();

        Post post = postService.createQuestion(member,requestDTO,null);

        List<String> newHashTagNames = new ArrayList<>();
        hashTagNames.add("빅뱅컴백");

        List<Long> oldHashTagIds = post.getHashTags().stream().map(tag->tag.getHashTag().getId()).toList();

        PostRequestDTO.PostUpdateRequestDTO updateRequestDTO = PostRequestDTO.PostUpdateRequestDTO.builder()
                .newHashTags(newHashTagNames)
                .remainingHashTagIds(oldHashTagIds)
                .build();

        postService.updatePost(member, post.getId(),updateRequestDTO,null);

    }


}
