package org.example.weneedbe.domain.article.repository;

import org.example.weneedbe.domain.article.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT ar FROM Article ar WHERE ar.articleField = 'PORTFOLIO' " +
            "AND ar.detailTags IN :detailTags ORDER BY ar.createdAt DESC")
    Page<Article> findPortfoliosByDetailTagsInOrderByCreatedAtDesc(List<String> detailTags, Pageable pageable);

    @Query("SELECT ar FROM Article ar LEFT JOIN FETCH ar.userArticles ua WHERE ar.articleField = 'PORTFOLIO' AND ar.detailTags IN :detailTags GROUP BY ar ORDER BY COUNT(ua) DESC")
    Page<Article> findPortfoliosByDetailTagsOrderByLikesDesc(List<String> detailTags, Pageable pageable);

    @Query("SELECT ar FROM Article ar WHERE ar.articleField = 'PORTFOLIO' " +
            "AND ar.detailTags IN :detailTags ORDER BY ar.viewCount DESC")
    Page<Article> findPortfoliosByDetailTagsInOrderByViewCountDesc(List<String> detailTags, Pageable pageable);

    @Query("SELECT ar FROM Article ar WHERE ar.articleField = 'PORTFOLIO' ORDER BY RANDOM()")
    List<Article> findRandomPortfolios();

    @Query("SELECT ar FROM Article ar WHERE ar.articleField = 'PORTFOLIO'")
    List<Article> findPortfolios();
}
