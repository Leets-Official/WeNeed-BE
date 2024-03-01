package org.example.weneedbe.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다."),
    USER_NOT_FOUND(400, "USER_NOT_FOUND", "존재하지 않는 사용자입니다."),
    INVALID_IMAGE_FILE(400, "INVALID_IMAGE_FILE", "PNG, JPEG, JPG 이미지 파일만 가능합니다."),
    FILE_UPLOAD_ERROR(500, "FILE_UPLOAD_ERROR", "파일 업로드에 실패했습니다."),
    ARTICLE_NOT_FOUND(400, "ARTICLE_NOT_FOUND", "존재하지 않는 게시물입니다."),
    INVALID_TOKEN(401, "UNAUTHORIZED", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(401, "UNAUTHORIZED", "만료된 토큰입니다."),
    COMMENT_NOT_FOUND(400, "COMMENT_NOT_FOUND", "존재하지 않는 댓글입니다."),
    INVALID_SORT_ERROR(400, "INVALID_SORT_ERROR", "잘못된 정렬 값입니다."),
    INVALID_INPUT_VALUE(400, "INVALID_INPUT_VALUE", "유효하지 않은 입력값입니다."),
    INVALID_EDIT_VALUE(400, "INVALID_EDIT_VALUE", "유효하지 않은 변경값입니다."),
    USER_NOT_REGISTERED(400, "USER_NOT_REGISTERED","회원가입이 완료되지 않은 유저입니다."),
    AUTHOR_MISMATCH_ERROR(400, "AUTHOR_MISMATCH_ERROR", "작성자와 사용자가 일치하지 않습니다."),
    TOKEN_NOT_FOUND(400, "TOKEN_NOT_FOUND", "해당 유저의 리프레시 토큰을 찾을 수 없습니다."),
    RECRUIT_NOT_FOUND_EXCEPTION(400, "RECRUIT_NOT_FOUND_EXCEPTION", "존재하지 않는 모집 지원서입니다."),
    INVALID_FILE(400, "INVALID_FILE", "PDF 파일만 가능합니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}