package org.example.weneedbe.domain.article.application;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.domain.article.Article;
import org.example.weneedbe.domain.article.dto.request.AddArticleRequest;
import org.example.weneedbe.domain.article.dto.response.MemberInfoResponse;
import org.example.weneedbe.domain.article.repository.ArticleRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.domain.UserArticle;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;

  public void createPortfolio(AddArticleRequest request) {
    /* 토큰을 통한 user 객체를 불러옴 */
    /* 아직 토큰이 없기 때문에 임시 객체를 사용 */
    User mockUser = userRepository.findById(1L).orElseThrow();
    Article article = Article.of(request, mockUser);

    List<UserArticle> userArticles = new ArrayList<>();
    /* 개인 프로젝트일 경우, 본인만 추가한다. */
    userArticles.add(new UserArticle(mockUser, article));

    /* 팀원 존재시 작성자를 제외한 팀원만 넣는다. */
    if (!request.getTeamMembersId().isEmpty()) {
      for (Long memberId : request.getTeamMembersId()) {
        User user = userRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        userArticles.add(new UserArticle(user, article));
      }
    }
    article.setUserArticles(userArticles);
    articleRepository.save(article);
  }

  public void createRecruit(AddArticleRequest request) {
    /* 토큰을 통한 user 객체를 불러옴 */
    /* 아직 토큰이 없기 때문에 임시 객체를 사용 */
    User mockUser = userRepository.findById(1L).get();
    Article article = Article.of(request, mockUser);
    article.setUserArticles(List.of(new UserArticle(mockUser, article)));

    articleRepository.save(article);
  }

  public List<MemberInfoResponse> getMemberList(String nickname) {
    return userRepository.findAllByNicknameStartingWith(nickname).stream()
        .map(MemberInfoResponse::new).toList();
  }
}
