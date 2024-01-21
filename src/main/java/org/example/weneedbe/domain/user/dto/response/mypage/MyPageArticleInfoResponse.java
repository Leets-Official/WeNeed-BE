package org.example.weneedbe.domain.user.dto.response.mypage;

import lombok.Getter;
import org.example.weneedbe.domain.bookmark.domain.Bookmark;

@Getter
public class MyPageArticleInfoResponse {

  private Long articleId;
  private String thumbnail;
  private String title;
  private int viewCount;
  private int heartCount;

  public MyPageArticleInfoResponse(Bookmark bookmark, int heartCount) {
    this.articleId = bookmark.getBookmarkId();
    this.thumbnail = bookmark.getArticle().getThumbnail();
    this.title = bookmark.getArticle().getTitle();
    this.viewCount = bookmark.getArticle().getViewCount();
    this.heartCount = heartCount;
  }
}