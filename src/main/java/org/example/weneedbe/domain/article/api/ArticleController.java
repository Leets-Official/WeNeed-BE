package org.example.weneedbe.domain.article.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.application.ArticleService;
import org.example.weneedbe.domain.article.dto.request.AddArticleRequest;
import org.example.weneedbe.domain.article.dto.response.MemberInfoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

  @PostMapping("/portfolio")
  public ResponseEntity<Void> createPortfolio(@RequestBody AddArticleRequest request) {
    articleService.createPortfolio(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/recruit")
  public ResponseEntity<Void> createRecruit(@RequestBody AddArticleRequest request) {
    articleService.createRecruit(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/portfolio/team-member")
  public ResponseEntity<List<MemberInfoResponse>> getTeamMember(@RequestParam String nickname) {
    return ResponseEntity.ok(articleService.getMemberList(nickname));
  }
}