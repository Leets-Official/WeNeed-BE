package org.example.weneedbe.domain.application.application;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.application.domain.Recruit;
import org.example.weneedbe.domain.application.dto.request.RecruitFormRequest;
import org.example.weneedbe.domain.application.repository.RecruitRepository;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.article.exception.ArticleNotFoundException;
import org.example.weneedbe.domain.article.repository.ArticleRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

  private final RecruitRepository recruitRepository;
  private final ArticleRepository articleRepository;
  private final UserService userService;

  public void createRecruitForm(String authorizationHeader, Long articleId, RecruitFormRequest request) {

    Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
    User user = userService.findUser(authorizationHeader);

    Recruit recruit = Recruit.of(article, user, request);

    recruitRepository.save(recruit);
  }
}
