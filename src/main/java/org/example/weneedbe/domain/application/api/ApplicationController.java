package org.example.weneedbe.domain.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.application.application.ApplicationService;
import org.example.weneedbe.domain.application.dto.request.RecruitFormRequest;
import org.example.weneedbe.domain.application.dto.response.RecruitFormResponse;
import org.example.weneedbe.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Application Controller", description = "지원서 관련 API입니다.")
@RestController
@RequiredArgsConstructor
public class ApplicationController {

  private final ApplicationService applicationService;

  @Operation(summary = "게시물 모집 지원서 작성", description = "사용자가 모집 지원서 폼을 작성합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "201"),
      @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping("/recruitForms/{articleId}")
  public ResponseEntity<Void> createRecruitForm(
      @RequestHeader("Authorization") String authorizationHeader, @PathVariable Long articleId,
      @RequestBody RecruitFormRequest request) {

    applicationService.createRecruitForm(authorizationHeader, articleId, request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "게시물 모집 지원서 조회", description = "사용자가 게시물 모집 지원서를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200"),
      @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/recruitmentForms/{articleId}")
  public ResponseEntity<RecruitFormResponse> getRecruitForm(
      @RequestHeader("Authorization") String authorizationHeader, @PathVariable Long articleId) {

    return ResponseEntity.ok(applicationService.getRecruitForm(authorizationHeader, articleId));
  }
}
