package com.knowledgein.springboot.web.dto.postDTO;

import com.knowledgein.springboot.domain.enums.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PostRequestDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateDto {
        @Schema(description = "질문 id (질문글이면 null)", example = "1")
        private Long questionId;

        @Schema(description = "유저 아이디", example = "1")
        @NotNull
        private Long userId;

        @Schema(description = "질문글 / 대답글", example = "QUESTION / ANSWER")
        @NotNull
        private PostType postType;

        @Schema(description = "게시물 제목", example = "날씨가 궁금해요")
        @NotNull
        private String title;

        @Schema(description = "게시물 내용", example = "오늘 날씨는 뭘지 궁금해요 알려주세요")
        @NotNull
        private String content;

        @Schema(description = "해시태그", example = "['#날씨', '#궁금', '#긴급']")
        private List<String> hashtagList;

        @Schema(description = "이미지 URL", example = "['https://image1.png', ...]")
        private List<String> imageList;
    }
}
