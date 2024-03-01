package org.example.weneedbe.domain.application.application;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.application.domain.Recruit;
import org.example.weneedbe.domain.application.dto.request.RecruitFormRequest;
import org.example.weneedbe.domain.application.dto.response.RecruitFormResponse;
import org.example.weneedbe.domain.application.exception.RecruitNotFoundException;
import org.example.weneedbe.domain.application.repository.RecruitRepository;
import org.example.weneedbe.domain.article.application.ArticleService;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.bookmark.service.BookmarkService;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

  private final RecruitRepository recruitRepository;
  private final UserService userService;
  private final ArticleService articleService;
  private final BookmarkService bookmarkService;

  public void createRecruitForm(String authorizationHeader, Long articleId, RecruitFormRequest request) {

    Article article = articleService.findArticle(articleId);
    User user = userService.findUser(authorizationHeader);

    Recruit recruit = Recruit.of(article, user, request);

    recruitRepository.save(recruit);
  }

  public RecruitFormResponse getRecruitForm(String authorizationHeader, Long articleId) {

    User user = userService.findUser(authorizationHeader);

    Article article = articleService.findArticle(articleId);
    int heartCount = articleService.countHeartByArticle(article);
    int bookmarkCount = bookmarkService.countBookmarkByArticle(article);

    Recruit recruit = recruitRepository.findByArticle_ArticleId(articleId).orElseThrow(RecruitNotFoundException::new);

    return new RecruitFormResponse(user, article, heartCount, bookmarkCount, recruit);
  }
}
