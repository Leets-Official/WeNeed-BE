package org.example.weneedbe.domain.user.dto.response.mypage;

import lombok.Getter;
import org.example.weneedbe.domain.application.domain.Application;
import org.example.weneedbe.domain.article.domain.Article;

import java.util.List;

@Getter
public class MyPageArticleInfoResponse {

  private final Long articleId;
  private final String thumbnail;
  private final String title;
  private final int viewCount;
  private final int heartCount;
  private final List<String> teamProfiles;

  public MyPageArticleInfoResponse(Article article, int heartCount, List<String> teamProfiles) {
    this.articleId = article.getArticleId();
    this.thumbnail = article.getThumbnail();
    this.title = article.getTitle();
    this.viewCount = article.getViewCount();
    this.heartCount = heartCount;
    this.teamProfiles = teamProfiles;
  }
}