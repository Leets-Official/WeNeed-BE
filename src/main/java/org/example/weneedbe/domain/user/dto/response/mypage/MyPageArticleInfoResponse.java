package org.example.weneedbe.domain.user.dto.response.mypage;

import lombok.Getter;
import org.example.weneedbe.domain.article.domain.Article;

@Getter
public class MyPageArticleInfoResponse {

  private Long articleId;
  private String thumbnail;
  private String title;
  private int viewCount;
  private int heartCount;

  public MyPageArticleInfoResponse(Article article, int heartCount) {
    this.articleId = article.getArticleId();
    this.thumbnail = article.getThumbnail();
    this.title = article.getTitle();
    this.viewCount = article.getViewCount();
    this.heartCount = heartCount;
  }
}