package org.example.weneedbe.domain.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.domain.Type;
import org.example.weneedbe.domain.user.dto.request.EditMyInfoRequest;
import org.example.weneedbe.domain.user.dto.response.mypage.*;
import org.example.weneedbe.domain.user.service.UserService;
import org.example.weneedbe.global.error.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User MyPage Controller", description = "사용자의 마이페이지 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/myPage")
public class MyPageController {

    private final UserService userService;

    @Operation(summary = "마이페이지의 사용자 정보 및 Output 정보",
            description = "헤더와 userId를 비교해 상황에 맞는 사용자 정보 및 작성한 포트폴리오 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/basic-info/{userId}")
    public ResponseEntity<BasicInfoResponse> getInfo(@RequestParam int size, @RequestParam int page,
                                                     @RequestHeader("Authorization") String authorizationHeader,
                                                     @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getBasicInfo(size, page, authorizationHeader, userId, Type.PORTFOLIO));
    }

    @Operation(summary = "마이페이지 내 프로필 수정", description = "현재 로그인한 사용자의 프로필 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping(value = "/my-info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EditMyInfoResponse> editInfo(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestPart(required = false) MultipartFile profileImage,
                                                       @RequestPart EditMyInfoRequest request) throws IOException {
        return ResponseEntity.ok(userService.editInfo(authorizationHeader, profileImage, request));
    }

    @Operation(summary = "마이페이지의 관심 크루 조회", description = "사용자가 북마크한 팀원모집 게시물을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/interesting-crews")
    public ResponseEntity<MyPageArticleListResponse> getInterestingCrewInfo(
            @RequestParam int size, @RequestParam int page,
            @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(userService.getBookmarkInfo(size, page, authorizationHeader, Type.RECRUITING));
    }

    @Operation(summary = "마이페이지의 관심 게시글 조회", description = "사용자가 북마크한 포트폴리오 게시물을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/interesting-portfolios")
    public ResponseEntity<MyPageArticleListResponse> getInterestingPortfolioInfo(
            @RequestParam int size, @RequestParam int page,
            @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(userService.getBookmarkInfo(size, page, authorizationHeader, Type.PORTFOLIO));
    }

    @Operation(summary = "나의 지원 크루 조회", description = "사용자가 지원한(지원서) 리크루팅 게시물을 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/my-applied-crews")
    public ResponseEntity<MyPageApplicationListResponse> getAppliedCrewInfo(
            @RequestParam int size, @RequestParam int page,
            @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(userService.getAppliedCrewInfo(size, page, authorizationHeader));
    }

    @Operation(summary = "나의 모집 크루 조회", description = "사용자가 모집하는 리크루팅(지원서) 게시물을 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/my-recruiting-crews")
    public ResponseEntity<MyPageArticleListResponse> getRecruitingCrewInfo(
            @RequestParam int size, @RequestParam int page,
            @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(userService.getRecruitingCrewInfo(size, page, authorizationHeader, Type.RECRUITING));
    }
}
