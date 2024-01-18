package org.example.weneedbe.domain.article.dto.response.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MainPortfolioDto {
    private MainUserDto user;
    private PageableDto pageable;
    private List<HotArticleDto> hotArticleList;
    private List<PortfolioArticleDto> articleList;
    private List<RecommendArticleDto> recommendArticleList;
}
