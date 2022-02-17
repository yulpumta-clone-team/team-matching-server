package com.projectmatching.app.constant;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum ResponseTemplateStatus {

    /**    private final Boolean isSuccess;
     private final String message;
     private final int code; //내부 코드
     private T data;

     *
     * 1000: 요청 성공
     */
    SUCCESS(true,"요청 성공", 1000),



    /**
     *
     * 2000 : Request 오류

     */
    EMPTY_JWT(false,"JWT를 입력해주세요.",2001),
    INVALID_JWT(false, "유효하지 않은 JWT입니다.",2002),
    EMAIL_DUPLICATE(false,"중복된 이메일입니다",2003),
    NAME_DUPLICATE(false,"중복된 이름입니다.",2004),


    /**
     * 3000: UserController  회원 가입, 로그인, 탈퇴 관련 오류
     */

    WITHDRAWAL_USER_ERROR(false,"회원탈퇴 실패",3000),
    LOGIN_USER_ERROR(false,"로그인에 실패하였습니다",3001);



    private final boolean isSuccess;
    private final int code;
    private final String message;

    private ResponseTemplateStatus(boolean isSuccess, String message, int code) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}