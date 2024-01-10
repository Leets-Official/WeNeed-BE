package org.example.weneedbe.domain.article.api;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.application.ArticleService;
import org.example.weneedbe.domain.article.dto.request.AddArticleRequest;
import org.example.weneedbe.domain.article.dto.response.MemberInfoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

  @PostMapping("/portfolio")
  public ResponseEntity<Void> createPortfolio(@RequestPart MultipartFile thumbnail,
      @RequestPart List<MultipartFile> images,
      @RequestPart List<MultipartFile> files, @RequestPart AddArticleRequest request) throws IOException {

    articleService.createPortfolio(thumbnail, images, files, request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

    @PostMapping("/recruit")
  public ResponseEntity<Void> createRecruit(@RequestPart MultipartFile thumbnail,
        @RequestPart List<MultipartFile> images,
        @RequestPart List<MultipartFile> files, @RequestPart AddArticleRequest request)
        throws IOException {

    articleService.createRecruit(thumbnail, images, files, request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/portfolio/team-member")
  public ResponseEntity<List<MemberInfoResponse>> getTeamMember(@RequestParam String nickname) {
    return ResponseEntity.ok(articleService.getMemberList(nickname));
  }
}