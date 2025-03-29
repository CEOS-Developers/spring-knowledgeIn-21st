package com.knowledgein.springboot.web.dto.postDTO;

import com.knowledgein.springboot.domain.enums.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class resultDto {
        @Schema(description = "게시물 id", example = "1")
        private Long postId;

        @Schema(description = "게시물 생성 시간", example = "2021-08-01T16:26:39.098")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PreviewDto {
        @Schema(description = "게시물 id", example = "1")
        private Long postId;

        @Schema(description = "유저 id", example = "1")
        private Long userId;

        @Schema(description = "질문 id (질문글이면 null)", example = "1")
        private Long questionId;

        @Schema(description = "질문글 / 대답글", example = "QUESTION / ANSWER")
        private PostType postType;

        @Schema(description = "게시물 제목", example = "날씨가 궁금해요")
        private String title;

        @Schema(description = "게시물 생성 시간", example = "2021-08-01T16:26:39.098")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PreviewListDto {
        @Schema(description = "게시물 preview 리스트")
        private List<PreviewDto> previewList;

        @Schema(description = "현재 페이지 게시물 수", example = "10")
        private Integer listSize;

        @Schema(description = "페이지 총 수", example = "2")
        private Integer totalPage;

        @Schema(description = "게시물 총 개수", example = "15")
        private Long totalElements;

        @Schema(description = "현재 페이지가 첫 페이지인지 여부")
        private Boolean isFirst;

        @Schema(description = "현재 페이지가 마지막 페이지인지 여부")
        private Boolean isLast;
    }
}
