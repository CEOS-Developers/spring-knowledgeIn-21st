package com.ceos21.ceos21BE.repositoryTest;

import com.ceos21.ceos21BE.config.dummy.DummyObject;
import com.ceos21.ceos21BE.domain.Member;
import com.ceos21.ceos21BE.domain.Post;
import com.ceos21.ceos21BE.repository.MemberRepository;
import com.ceos21.ceos21BE.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostRepositoryTest extends DummyObject {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    public void setUp() {
        lenient().when(memberRepository.save(newMockMember("ceos21", "pw1212", "ceos@gmail.com", "ceos", "ceos21")))
                .thenReturn(newMockMember("ceos21", "pw1212", "ceos@gmail.com", "ceos", "ceos21"));

        lenient().when(postRepository.save(newMockPost("제목1", "내용1", "해시태그1", null)))
                .thenReturn(newMockPost("제목1", "내용1", "해시태그1", null));

        lenient().when(postRepository.save(newMockPost("제목2", "내용2", "해시태그2", null)))
                .thenReturn(newMockPost("제목2", "내용2", "해시태그2", null));
    }


    @Test
    public void 게시글저장및조회_성공test() throws Exception {
        // given
        Member member1 = newMockMember("ceos21", "pw1212", "ceos@gmail.com", "ceos", "ceos21");
        Post post1 = newMockPost("제목1", "내용1", "해시태그1", member1);
        Post post2 = newMockPost("제목2", "내용2", "해시태그2", member1);

        // when: save 호출 및 post 저장
        when(postRepository.save(post1)).thenReturn(post1);
        when(postRepository.save(post2)).thenReturn(post2);

        Post savedPost1 = postRepository.save(post1);
        Post savedPost2 = postRepository.save(post2);

        // then: save가 호출되어 결과가 반환되는지 확인
        assertThat(savedPost1.getTitle()).isEqualTo("제목1");
        assertThat(savedPost2.getTitle()).isEqualTo("제목2");

        // verify save 호출 확인
        verify(postRepository).save(post1);
        verify(postRepository).save(post2);
    }

    @Test
    public void 게시글삭제_테스트() throws Exception {
        // given: 게시글 저장
        Member member1 = newMockMember("ceos21", "pw1212", "ceos@gmail.com", "ceos", "ceos21");
        Post post1 = newMockPost("제목1", "내용1", "해시태그1", member1);
        when(postRepository.save(post1)).thenReturn(post1);

        // 게시글 저장
        Post savedPost1 = postRepository.save(post1);

        // when: 삭제 호출
        postRepository.delete(savedPost1);

        // then: 삭제 후 조회 시 예외가 발생해야 함
        assertThrows(IllegalArgumentException.class, () -> {
            postRepository.findById(savedPost1.getPostId()).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        });

        // verify: 삭제가 호출되었는지 확인
        verify(postRepository).delete(savedPost1);
    }



}
