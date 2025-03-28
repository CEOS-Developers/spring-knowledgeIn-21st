package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.global.exceptionHandler.GeneralException;
import com.ceos21.knowledgeIn.global.exceptionHandler.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/post")
    public String postCreate(){
        if(true) {throw new GeneralException(ErrorStatus.ALREADY_EXISTS);}
        postService.createPost();

        return "ok";
    }

    @GetMapping("/memberPost")
    public String memberPost(){
        postService.memberCheck();

        return "ok";
    }
}
