package org.example.weneedbe.domain.application.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.application.application.ApplicationService;
import org.example.weneedbe.domain.application.dto.request.ApplicationFormRequest;
import org.example.weneedbe.domain.application.dto.request.ApplicationResultRequest;
import org.example.weneedbe.domain.application.dto.request.RecruitFormRequest;
import org.example.weneedbe.domain.application.dto.response.ApplicationFormResponse;
import org.example.weneedbe.domain.application.dto.response.ApplicationInfoResponse;
import org.example.weneedbe.domain.application.dto.response.RecruitFormResponse;
import org.example.weneedbe.global.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @Operation(summary = "지원서 작성", description = "게시물 모집에 대해 사용자가 지원합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/application-forms/{articleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createApplicationForm(
            @RequestHeader("Authorization") String authorizationHeader, @PathVariable Long articleId,
            @RequestPart ApplicationFormRequest request, @RequestPart MultipartFile appeal) throws IOException {

        applicationService.createApplicationForm(authorizationHeader, articleId, appeal, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "지원서 조회", description = "지원했던 지원서를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/application-forms/{applicationId}")
    public ResponseEntity<ApplicationFormResponse> getApplicationForm(
            @RequestHeader("Authorization") String authorizationHeader, @PathVariable Long applicationId) {
        return ResponseEntity.ok(applicationService.getApplicationForm(authorizationHeader, applicationId));
    }

    @Operation(summary = "지원서 상태 변경", description = "지원서의 상태 여부를 변경합니다. " +
            "PENDING, ACCEPTED, REFUSED 총 세 가지의 상태로 나뉩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/application-forms/{applicationId}")
    public ResponseEntity<Void> updateApplicationStatus(@PathVariable Long applicationId,
                                                        @RequestBody ApplicationResultRequest request) {
        applicationService.updateApplicationStatus(applicationId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "지원서 목록 조회", description = "내가 작성한 Recruit의 지원서 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/application-forms/applications/{recruitId}")
    public ResponseEntity<List<List<ApplicationInfoResponse>>> getApplications(@PathVariable Long recruitId) {
        return ResponseEntity.ok(applicationService.getApplications(recruitId));
    }

}
