package org.example.weneedbe.domain.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.domain.Type;
import org.example.weneedbe.domain.user.dto.request.EditMyInfoRequest;
import org.example.weneedbe.domain.user.dto.response.mypage.EditMyInfoResponse;
import org.example.weneedbe.domain.user.dto.response.mypage.GetMyInfoResponse;
import org.example.weneedbe.domain.user.dto.response.mypage.MyPageArticleInfoResponse;
import org.example.weneedbe.domain.user.service.UserService;
import org.example.weneedbe.global.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User MyPage Controller", description = "사용자의 마이페이지 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/myPage")
public class MyPageController {

    private final UserService userService;

    @Operation(summary = "마이페이지의 내 정보", description = "현재 로그인한 사용자의 정보를 마이페이지 내 불러옵니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/my-info")
    public ResponseEntity<GetMyInfoResponse> getInfo(@RequestHeader("Authorization") String authorizationHeader) throws IOException {
        return ResponseEntity.ok(userService.getInfo(authorizationHeader));
    }

    @Operation(summary = "마이페이지 내 프로필 수정", description = "현재 로그인한 사용자의 프로필 정보를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/my-info")
    public ResponseEntity<EditMyInfoResponse> editInfo(@RequestHeader("Authorization") String authorizationHeader,
        @RequestPart MultipartFile profileImage,
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
    public ResponseEntity<List<MyPageArticleInfoResponse>> getInterestingCrewInfo(
        @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(userService.getBookmarkInfo(authorizationHeader, Type.RECRUITING));
    }

    @Operation(summary = "마이페이지의 관심 게시글 조회", description = "사용자가 북마크한 포트폴리오 게시물을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/interesting-portfolios")
    public ResponseEntity<List<MyPageArticleInfoResponse>> getInterestingPortfolioInfo(
            @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(userService.getBookmarkInfo(authorizationHeader, Type.PORTFOLIO));
    }

    @Operation(summary = "마이페이지의 마이 아웃풋 조회", description = "사용자가 작성한 포트폴리오 게시물을 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/my-outputs")
    public ResponseEntity<List<MyPageArticleInfoResponse>> getMyOutputInfo(
            @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(userService.getArticleInfo(authorizationHeader, Type.PORTFOLIO));
    }

    @Operation(summary = "마이페이지의 마이 크루 조회", description = "사용자가 작성한 리크루팅 게시물을 가져옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/my-crews")
    public ResponseEntity<List<MyPageArticleInfoResponse>> getMyCrewInfo(
            @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(userService.getArticleInfo(authorizationHeader, Type.RECRUITING));
    }

    @Operation(summary = "다른 사용자의 마이페이지 내 세부정보 조회", description = "userId를 통한 다른 사용자의 세부 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{userId}/info")
    public ResponseEntity<GetMyInfoResponse> getInfoFromUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getInfoFromUserId(userId));
    }

    @Operation(summary = "다른 사용자의 마이페이지 내 아웃풋 조회", description = "userId를 통한 다른 사용자가 작성한 포트폴리오 게시물을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{userId}/outputs")
    public ResponseEntity<List<MyPageArticleInfoResponse>> getOutputFromUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getOutputFromUserId(userId, Type.PORTFOLIO));
    }
}
