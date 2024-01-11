package org.example.weneedbe.domain.article.dto.response.main;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseArticleDto {
    private Long articleId;
    private String thumbnail;
}
