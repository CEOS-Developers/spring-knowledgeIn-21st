package com.ceos21.knowledgeIn.global.exceptionHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T>{

    @JsonProperty(namespace = "isSuccess") //JSON에서 해당 속성의 이름을 정의
    private Boolean isSuccess;

    private String code;    //API 응답 코드 (ex "COMMON2000")
    private String message; //API 응답 메시지(ex "요청에 성공했습니다.")

    @JsonInclude(JsonInclude.Include.NON_NULL) //JSON에 해당 속성을 어떨 때 포함시킬지 정의
    private T result;   //결과 DTO


    //성공 시 응답 생성
    public static <T> ApiResponse<T> onSuccess(T result){
        return new ApiResponse<>(true, Status.SUCCESS.getCode(), Status.SUCCESS.getMessage(), result);
    }

    //특정 코드가 필요한 경우? 응답 생성
    public static <T> ApiResponse<T> of(Status code, T result){
        return new ApiResponse<>(true, code.getCode(), code.getMessage(),result);
    }

    //실패 시 응답 생성
    public static <T> ApiResponse<T> onFailure(String code, String message, T result){
        return new ApiResponse<>(false, code, message,result);
    }
}
