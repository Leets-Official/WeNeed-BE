package org.example.weneedbe.domain.article.dto.response.main;

import lombok.Getter;
import org.example.weneedbe.domain.article.domain.Article;

@Getter
public class WeightedArticleDto implements Comparable<WeightedArticleDto> {
    private Article article;
    private double weight;

    public WeightedArticleDto(Article article, double weight) {
        this.article = article;
        this.weight = weight;
    }

    @Override
    public int compareTo(WeightedArticleDto o) {
        return Double.compare(o.weight, this.weight);
    }
}
