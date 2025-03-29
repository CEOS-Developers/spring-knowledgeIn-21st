package com.ceos21.knowledgeIn.domain.post;

import com.ceos21.knowledgeIn.domain.common.PostType;
import com.ceos21.knowledgeIn.domain.hashTag.HashTag;
import com.ceos21.knowledgeIn.domain.hashTag.HashTagRepository;
import com.ceos21.knowledgeIn.domain.hashTag.HashTagService;
import com.ceos21.knowledgeIn.domain.image.Image;
import com.ceos21.knowledgeIn.domain.image.ImageService;
import com.ceos21.knowledgeIn.domain.member.Member;
import com.ceos21.knowledgeIn.domain.post.dto.PostRequestDTO;
import com.ceos21.knowledgeIn.domain.postHashTag.PostHashTag;
import com.ceos21.knowledgeIn.domain.postHashTag.PostHashTagRepository;
import com.ceos21.knowledgeIn.domain.postHashTag.PostHashTagService;
import com.ceos21.knowledgeIn.global.exceptionHandler.Status;
import com.ceos21.knowledgeIn.global.exceptionHandler.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;
    private final PostHashTagRepository postHashTagRepository;
    private final ImageService imageService;
    private final HashTagService hashTagService;
    private final PostHashTagService postHashTagService;

    @Transactional
    public Post createQuestion(Member member, PostRequestDTO.QuestionCreateRequestDTO requestDTO, List<MultipartFile> images) {

        //이미지 저장
        List<Image> imageList = new ArrayList<>();
        if(images != null&&!images.isEmpty()){
            imageList = images.stream().map(imageService::createFile).collect(Collectors.toList());
        }

        //HashTag DTO List->Entity List
        List<HashTag> hashTags = requestDTO.getHashTags().stream().map(hashTagService::findOrSave).toList();

        //Post DTO->Entity->save
        Post post = postRepository.save(Post.builder()
                .member(member)
                .postType(PostType.QUESTION)
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .isAnonymous(requestDTO.getIsAnonymous())
                .images(imageList)
                .build());

        //각 해시태그와 게시글을 이용해 postHashTag 생성하고 저장
        List<PostHashTag> postHashTags = postHashTagService.createPostHashTagList(post,hashTags);

        //post 의 tag 연관관계 리스트 갱신 로직 필요
        return post.setHashTags(postHashTags);

    }


    public Page<Post> getPostList(Integer page, Integer size, String sort, String search) {

        Sort sorted;

        switch (sort){
            case "ANS_DESC": sorted = Sort.by(Sort.Direction.DESC, "answerCnt"); break;
            case "ANS_ASC": sorted = Sort.by(Sort.Direction.ASC, "answerCnt"); break;
            case "RECENT":
            default: sorted = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        return postRepository.findAll(PageRequest.of(page,size, sorted));
    }

    @Transactional
    public Post createAnswer(Long postId, Member member, PostRequestDTO.AnswerCreateRequestDTO requestDTO, List<MultipartFile> images) {


        //이미지 저장
        List<Image> imageList = new ArrayList<>();
        if(images != null&&!images.isEmpty()){
            imageList = images.stream().map(imageService::createFile).collect(Collectors.toList());
        }

        //해시태그 엔티티 리스트화
        List<HashTag> hashTags = requestDTO.getHashTags().stream().map(hashTagService::findOrSave).toList();

        Post parentPost = postRepository.findById(postId).orElseThrow(()->new GeneralException(Status.NOT_FOUND));

        //DTO->Entity->save
        Post post = postRepository.save(Post.builder()
                .member(member)
                .postType(PostType.ANSWER)
                .parent(parentPost)
                .content(requestDTO.getContent())
                .images(imageList)
                .isAnonymous(requestDTO.getIsAnonymous())
                .build());

        parentPost.setAnswerCnt(true);

        //각 해시태그와 게시글을 이용해 postHashTag 생성
        List<PostHashTag> postHashTags = postHashTagService.createPostHashTagList(post, hashTags);

        //post 의 tag 연관관계 리스트 갱신 로직->DTO
        return post.setHashTags(postHashTags);
    }

    @Transactional
    public Post updatePost(Post post, PostRequestDTO.PostUpdateRequestDTO requestDTO, List<MultipartFile> newImageFiles) {
        List<PostHashTag> newHashTags = new ArrayList<>();
        List<PostHashTag> oldHashTags = new ArrayList<>();
        List<Image> newImages = new ArrayList<>();
        List<Image> oldImages = new ArrayList<>();

        //새로운 해시태그 DTO List->Entity List
        if(requestDTO.getNewHashTags()!=null&&!requestDTO.getNewHashTags().isEmpty()){
            newHashTags = requestDTO.getNewHashTags().stream().map(tag->{
                HashTag hashTag = hashTagService.findOrSave(tag);
                return postHashTagService.createPostHashTag(post,hashTag);
            }).toList();
        }

        //유지할 해시태그 DTO List->Entity List
        if(requestDTO.getRemainingHashTagIds()!=null&&!requestDTO.getRemainingHashTagIds().isEmpty()){
            oldHashTags = requestDTO.getRemainingHashTagIds().stream().map(id->{
                        HashTag hashTag = hashTagRepository.findById(id).orElseThrow(()->new GeneralException(Status.NOT_FOUND));
                        return postHashTagRepository.findByPostAndHashTag(post,hashTag).orElseThrow(()->new GeneralException(Status.NOT_FOUND));
                    }).toList();
        }

        //새로운 이미지 file List -> Entity List
        if(newImageFiles!=null&&!newImageFiles.isEmpty()){
            newImages = newImageFiles.stream().map(imageService::createFile).toList();
        }


        //유지할 이미지 url List -> Entity List
        if(requestDTO.getRemainingImageList()!=null&&!requestDTO.getRemainingImageList().isEmpty()){
            oldImages = requestDTO.getRemainingImageList().stream().map(imageService::getImageByUrl).toList();
        }



        return post.update(requestDTO,newHashTags,oldHashTags, newImages,oldImages);
    }

    @Transactional
    public void deletePost(Post post) {

        if(post.getPostType()==PostType.ANSWER){
            post.getParent().setAnswerCnt(false);
        }
        postRepository.delete(post);
    }
}
