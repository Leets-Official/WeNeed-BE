package org.example.weneedbe.domain.article.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.application.ArticleService;
import org.example.weneedbe.domain.article.dto.request.AddArticleRequest;
import org.example.weneedbe.domain.article.dto.response.MemberInfoResponse;
import org.example.weneedbe.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Article Controller", description = "게시물 관련 API입니다.")
@RestController
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

  @Operation(summary = "포트폴리오 게시물 작성", description = "사용자가 포트폴리오 게시물을 작성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201"),
      @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping("/portfolio")
  public ResponseEntity<Void> createPortfolio(@RequestPart MultipartFile thumbnail,
      @RequestPart List<MultipartFile> images,
      @RequestPart List<MultipartFile> files, @RequestPart AddArticleRequest request) throws IOException {

    articleService.createPortfolio(thumbnail, images, files, request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "리크루팅 게시물 작성", description = "사용자가 리크루팅 게시물을 작성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201"),
      @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping("/recruit")
  public ResponseEntity<Void> createRecruit(@RequestPart MultipartFile thumbnail,
      @RequestPart List<MultipartFile> images,
      @RequestPart List<MultipartFile> files, @RequestPart AddArticleRequest request)
      throws IOException {

    articleService.createRecruit(thumbnail, images, files, request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "팀원 조회", description = "포트폴리오 게시물에 팀원 추가시 사용합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200"),
      @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/portfolio/team-member")
  public ResponseEntity<List<MemberInfoResponse>> getTeamMember(@RequestParam String nickname) {
    return ResponseEntity.ok(articleService.getMemberList(nickname));
  }
}