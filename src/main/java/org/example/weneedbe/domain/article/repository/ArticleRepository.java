package org.example.weneedbe.domain.article.repository;

import org.example.weneedbe.domain.article.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
