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
    EXPIRED_TOKEN(401,"UNAUTHORIZED", "만료된 토큰입니다."),
    INVALID_INPUT_VALUE(400, "INVALID_INPUT_VALUE", "유효하지 않은 입력값입니다.");

    private final int httpStatus;
    private final String code;
    private final String message;
}