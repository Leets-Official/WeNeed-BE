package org.example.weneedbe.domain.article.dto.response.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendArticleDto extends BaseArticleDto{
    private String title;
    private Boolean isBookmarked;
}
