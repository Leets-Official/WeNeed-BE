package org.example.weneedbe.domain.article.repository;

import org.example.weneedbe.domain.article.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = "SELECT * FROM article ar WHERE ar.article_field = 'PORTFOLIO' AND ar.detail_tags IN :detailTags ORDER BY ar.created_at DESC", nativeQuery = true)
//    @Query(value = "SELECT ar FROM Article ar WHERE ar.articleField = 'PORTFOLIO' " +
//            "AND ar.detailTags IN :detailTags ORDER BY ar.createdAt DESC")
    Page<Article> findPortfoliosByDetailTagsInOrderByCreatedAtDesc(@Param("detailTags") String[] detailTag, Pageable pageable);

    @Query(value = "SELECT * FROM article ar LEFT JOIN user_articles ua ON ar.article_id = ua.article_id WHERE ar.article_field = 'PORTFOLIO' AND ar.detail_tags IN :detailTags GROUP BY ar.article_id ORDER BY COUNT(ua.user_article_id) DESC", nativeQuery = true)
    //@Query(value = "SELECT ar FROM Article ar LEFT JOIN FETCH ar.userArticles ua WHERE ar.articleField = 'PORTFOLIO' AND ar.detailTags IN :detailTags GROUP BY ar ORDER BY COUNT(ua) DESC")
    Page<Article> findPortfoliosByDetailTagsOrderByLikesDesc(@Param("detailTags") String[] detailTag, Pageable pageable);

    @Query(value = "SELECT * FROM article ar WHERE ar.article_field = 'PORTFOLIO' AND ar.detail_tags IN :detailTags ORDER BY ar.view_count DESC", nativeQuery = true)
//    @Query(value = "SELECT ar FROM Article ar WHERE ar.articleField = 'PORTFOLIO' " +
//            "AND ar.detailTags IN :detailTags ORDER BY ar.viewCount DESC")
    Page<Article> findPortfoliosByDetailTagsInOrderByViewCountDesc(@Param("detailTags") String[] detailTag, Pageable pageable);

    @Query("SELECT ar FROM Article ar WHERE ar.articleField = 'PORTFOLIO' ORDER BY RANDOM()")
    List<Article> findRandomPortfolios();

    @Query("SELECT ar FROM Article ar WHERE ar.articleField = 'PORTFOLIO'")
    List<Article> findPortfolios();
}
