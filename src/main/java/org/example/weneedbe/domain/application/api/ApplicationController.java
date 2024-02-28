package org.example.weneedbe.domain.application.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.application.application.ApplicationService;
import org.example.weneedbe.domain.application.dto.request.RecruitFormRequest;
import org.example.weneedbe.domain.application.dto.response.RecruitFormResponse;
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

  @PostMapping("/recruitForms/{articleId}")
  public ResponseEntity<Void> createRecruitForm(
      @RequestHeader("Authorization") String authorizationHeader, @PathVariable Long articleId,
      @RequestBody RecruitFormRequest request) {

    applicationService.createRecruitForm(authorizationHeader, articleId, request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/recruitmentForms/{articleId}")
  public ResponseEntity<RecruitFormResponse> getRecruitForm(
      @RequestHeader("Authorization") String authorizationHeader, @PathVariable Long articleId) {

    return ResponseEntity.ok(applicationService.getRecruitForm(authorizationHeader, articleId));
  }
}
