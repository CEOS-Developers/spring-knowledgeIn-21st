package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.common.PostType;
import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.member.MemberRepository;
import com.ceos21.knowledgeIn.domain.post.dto.PostRequestDTO;
import com.ceos21.knowledgeIn.domain.post.dto.PostResponseDTO;
import com.ceos21.knowledgeIn.global.exceptionHandler.ApiResponse;
import com.ceos21.knowledgeIn.global.exceptionHandler.GeneralException;
import com.ceos21.knowledgeIn.global.exceptionHandler.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시글")
@RequestMapping("api/posts")
public class PostController {

    private final PostService postService;
    private final MemberRepository memberRepository;

    //질문글 생성
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "질문 생성 API", description = "질문 생성 API")
    public ApiResponse<PostResponseDTO> createQuestion(@RequestPart PostRequestDTO.QuestionCreateRequestDTO requestDTO,
                                                           @RequestPart(required = false) List<MultipartFile> images){
       Member member = memberRepository.findById(1L).orElseThrow(()->new GeneralException(Status.NOT_FOUND));

       PostResponseDTO body = PostResponseDTO.from(postService.createQuestion(member, requestDTO, images));

       return ApiResponse.onSuccess(body);


    }

    //질문글 목록 조회
    @GetMapping("/")
    @Operation(summary = "질문 목록 조회 API", description = "홈에서 질문 목록을 조회하는 API")
    public ApiResponse<Page<PostResponseDTO>> getPostList(@RequestParam Integer page, @RequestParam Integer size,
                                                          @RequestParam(defaultValue = "DESC") String sort,
                                                          @RequestParam(required = false) String search){

        Page<PostResponseDTO> body = postService.getPostList(page, size, sort, search).map(PostResponseDTO::from);
        return ApiResponse.onSuccess(body);

    }

    //질문글 단건 조회
    @GetMapping("/{postId}")
    @Operation(summary = "질문 단건 조회 API", description = "질문 단건 조회 API")
    public ApiResponse<PostResponseDTO> getPost(@PathVariable Long postId){
        Post post = postService.getQuestion(postId);

        PostResponseDTO questionResponseDTO = PostResponseDTO.from(post);

        return ApiResponse.onSuccess(questionResponseDTO);
    }

    //특정 질문글의 답변 목록 조회
    @GetMapping("/{postId}/answers")
    @Operation(summary = "답변 조회 API", description = "특정 질문에 대한 답변 목록을 조회하는 API")
    public ApiResponse<Page<PostResponseDTO>> getAnswers(@PathVariable Long postId, @RequestParam Integer page,
                                                   @RequestParam Integer size, @RequestParam String sort){

        Page<Post> postList = postService.getQuestionAnswerList(postId,page,size,sort);

        Page<PostResponseDTO> body = postList.map(PostResponseDTO::from);

        return ApiResponse.onSuccess(body);
    }

    //답변 생성
    @PostMapping(value = "/{postId}/answers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "답변 생성 API", description = "특정 질문에 대해 답변을 생성하는 API")
    public ApiResponse<PostResponseDTO> createAnswer(@PathVariable Long postId, @RequestPart PostRequestDTO.AnswerCreateRequestDTO requestDTO,
                                                     @RequestPart(required = false) List<MultipartFile> images){

        Member member = memberRepository.findById(1L).orElseThrow(()->new GeneralException(Status.NOT_FOUND));

        PostResponseDTO body = PostResponseDTO.from(postService.createAnswer(postId,member,requestDTO,images));

        return ApiResponse.onSuccess(body);

    }


    //게시글 수정(질문, 답변 공통)
    @PatchMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 수정 API", description = "질문이나 답변을 수정하는 API")
    public ApiResponse<PostResponseDTO> updatePost(@PathVariable Long postId, @RequestPart PostRequestDTO.PostUpdateRequestDTO requestDTO,
                                                   @RequestPart(required = false) List<MultipartFile> newImages){

        PostResponseDTO body = PostResponseDTO.from(postService.updatePost(postId,requestDTO,newImages));

        return ApiResponse.onSuccess(body);

    }

    //게시글 삭제(질문, 답변 공통)
    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제 API", description = "질문이나 답변을 삭제하는 API")
    public ApiResponse<Object> deletePost(@PathVariable Long postId){

        //삭제 로직
        postService.deletePost(postId);

        return new ApiResponse<>(true, Status.SUCCESS.getCode(),"삭제에 성공했습니다.", null);
    }
}
