package org.example.weneedbe.domain.article.dto.response.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleDto extends BaseArticleDto {
    private String writerNickname;
    private int viewCount;
    private int heartCount;
    private Boolean isBookmarked;
}
