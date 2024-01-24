package org.example.weneedbe.domain.user.dto.response.mypage;

import lombok.Getter;
import org.example.weneedbe.domain.article.domain.Article;

import java.util.List;

@Getter
public class MyPageArticleInfoResponse {

  private Long articleId;
  private String thumbnail;
  private String title;
  private int viewCount;
  private int heartCount;
  private List<String> teamProfiles;

  public MyPageArticleInfoResponse(Article article, int heartCount, List<String> teamProfiles) {
    this.articleId = article.getArticleId();
    this.thumbnail = article.getThumbnail();
    this.title = article.getTitle();
    this.viewCount = article.getViewCount();
    this.heartCount = heartCount;
    this.teamProfiles = teamProfiles;
  }
}