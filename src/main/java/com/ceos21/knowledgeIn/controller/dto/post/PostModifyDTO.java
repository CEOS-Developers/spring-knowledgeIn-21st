package com.ceos21.knowledgeIn.controller.dto.post;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor  //List에 디폴트 값을 넣으려면 필수!!
@AllArgsConstructor
@Builder
@Data
public class PostModifyDTO {
    private String title;

    private String content;

    private List<Long> imageIds=new ArrayList<>();

    private List<Long> previousHashTagIds=new ArrayList<>();

    private List<String> newHashTagStrings=new ArrayList<>();
}
