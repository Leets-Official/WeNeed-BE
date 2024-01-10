package org.example.weneedbe.domain.article.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.article.domain.article.Article;
import org.example.weneedbe.domain.article.dto.request.AddArticleRequest;
import org.example.weneedbe.domain.article.dto.response.MemberInfoResponse;
import org.example.weneedbe.domain.article.repository.ArticleRepository;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.domain.UserArticle;
import org.example.weneedbe.domain.user.exception.UserNotFoundException;
import org.example.weneedbe.domain.user.repository.UserRepository;
import org.example.weneedbe.global.s3.application.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;
  private final S3Service s3Service;

  public void createPortfolio(MultipartFile thumbnail, List<MultipartFile> images,
      List<MultipartFile> files, AddArticleRequest request) throws IOException {

    String thumbnailUrl = s3Service.uploadImage(thumbnail);
    List<String> imageUrls = s3Service.uploadImages(images);
    List<String> fileUrls = s3Service.uploadFile(files);

    /* 토큰을 통한 user 객체를 불러옴 */
    /* 아직 토큰이 없기 때문에 임시 객체를 사용 */
    User mockUser = userRepository.findById(1L).orElseThrow();

    Article article = Article.of(thumbnailUrl, imageUrls, fileUrls, request, mockUser);

    List<UserArticle> userArticles = new ArrayList<>();
    /* 개인 프로젝트일 경우, 본인만 추가한다. */
    userArticles.add(new UserArticle(mockUser, article));

    /* 팀원 존재시 작성자를 제외한 팀원만 넣는다. */
    if (!request.getTeamMembersId().isEmpty()) {
      for (Long memberId : request.getTeamMembersId()) {
        User user = userRepository.findById(memberId)
            .orElseThrow(UserNotFoundException::new);
        userArticles.add(new UserArticle(user, article));
      }
    }
    article.setUserArticles(userArticles);
    articleRepository.save(article);
  }

  public void createRecruit(MultipartFile thumbnail, List<MultipartFile> images,
      List<MultipartFile> files, AddArticleRequest request) throws IOException {

    String thumbnailUrl = s3Service.uploadImage(thumbnail);
    List<String> imageUrls = s3Service.uploadImages(images);
    List<String> fileUrls = s3Service.uploadFile(files);

    /* 토큰을 통한 user 객체를 불러옴 */
    /* 아직 토큰이 없기 때문에 임시 객체를 사용 */
    User mockUser = userRepository.findById(1L).orElseThrow();

    Article article = Article.of(thumbnailUrl, imageUrls, fileUrls, request, mockUser);
    article.setUserArticles(List.of(new UserArticle(mockUser, article)));

    articleRepository.save(article);
  }

  public List<MemberInfoResponse> getMemberList(String nickname) {
    return userRepository.findAllByNicknameStartingWith(nickname).stream()
        .map(MemberInfoResponse::new).toList();
  }
}
