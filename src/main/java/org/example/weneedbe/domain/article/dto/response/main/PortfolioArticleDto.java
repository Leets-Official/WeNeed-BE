package org.example.weneedbe.domain.article.dto.response.main;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PortfolioArticleDto {
    private Long articleId;
    private String thumbnail;
    private String writerNickname;
    private String profile;
    private int viewCount;
    private int heartCount;
    private boolean isBookmarked;

}
