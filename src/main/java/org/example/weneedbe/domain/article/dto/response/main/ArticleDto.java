package org.example.weneedbe.domain.article.dto.response.main;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ArticleDto {
    private Long articleId;
    private String thumbnail;
    private String writerNickname;
    private int viewCount;
    private int heartCount;
    private Boolean isBookmarked;

}
