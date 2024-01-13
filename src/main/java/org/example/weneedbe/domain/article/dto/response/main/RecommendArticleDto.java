package org.example.weneedbe.domain.article.dto.response.main;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RecommendArticleDto {
    private Long articleId;
    private String thumbnail;
    private String title;
    private Boolean isBookmarked;
}
