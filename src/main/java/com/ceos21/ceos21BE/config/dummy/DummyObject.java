package com.ceos21.ceos21BE.config.dummy;

import com.ceos21.ceos21BE.domain.Member;
import com.ceos21.ceos21BE.domain.Post;

public class DummyObject {

    protected Member newMockMember(String loginId, String password, String email, String name, String nickname) {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .email(email)
                .name(name)
                .nickname(nickname)
                .build();
    }

    protected Post newMockPost(String title, String content,String hashtag, Member member) {
        return Post.builder()
                .title(title)
                .content(content)
                .hashtag(hashtag)
                .member(member)
                .build();
    }
}
