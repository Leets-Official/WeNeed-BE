package org.example.weneedbe.domain.article.repository;

import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = "SELECT a.* FROM article a LEFT JOIN detail_tags dt ON a.article_id = dt.article_id WHERE a.article_type = 'PORTFOLIO' AND (:detailTags IS NULL OR dt.detail_tags IN :detailTags) ORDER BY a.created_at DESC",
            countQuery = "SELECT count(*) FROM article a LEFT JOIN detail_tags dt ON a.article_id = dt.article_id WHERE a.article_type = 'PORTFOLIO' AND (:detailTags IS NULL OR dt.detail_tags IN :detailTags)",
            nativeQuery = true)
    Page<Article> findPortfoliosByDetailTagsInOrderByCreatedAtDesc(@Param("detailTags") String[] detailTags, Pageable pageable);

    @Query(value = "SELECT ar.* " +
            "FROM article ar " +
            "LEFT JOIN detail_tags dt ON ar.article_id = dt.article_id " +
            "LEFT JOIN article_like h ON ar.article_id = h.article_id " +
            "WHERE ar.article_type = 'PORTFOLIO' AND (:detailTags IS NULL OR dt.detail_tags IN :detailTags)" +
            "GROUP BY ar.article_id " +
            "ORDER BY COUNT(h.article_id) DESC",
            countQuery = "SELECT COUNT(DISTINCT ar.article_id) " +
                    "FROM article ar " +
                    "LEFT JOIN detail_tags dt ON ar.article_id = dt.article_id " +
                    "LEFT JOIN article_like h ON ar.article_id = h.article_id " +
                    "WHERE ar.article_type = 'PORTFOLIO' AND (:detailTags IS NULL OR dt.detail_tags IN :detailTags)",
            nativeQuery = true)
    Page<Article> findPortfoliosByDetailTagsOrderByLikesDesc(@Param("detailTags") String[] detailTags, Pageable pageable);

    @Query(value = "SELECT a.* FROM article a LEFT JOIN detail_tags dt ON a.article_id = dt.article_id AND a.article_type = 'PORTFOLIO' AND (:detailTags IS NULL OR dt.detail_tags IN :detailTags) ORDER BY a.view_count DESC",
            countQuery = "SELECT count(*) FROM article a LEFT JOIN detail_tags dt ON a.article_id = dt.article_id AND a.article_type = 'PORTFOLIO' AND (:detailTags IS NULL OR dt.detail_tags IN :detailTags)",
            nativeQuery = true)
    Page<Article> findPortfoliosByDetailTagsInOrderByViewCountDesc(@Param("detailTags") String[] detailTags, Pageable pageable);

    @Query("SELECT ar FROM Article ar WHERE ar.articleType = 'PORTFOLIO' ORDER BY RAND()")
    List<Article> findRandomPortfolios();

    @Query("SELECT ar FROM Article ar WHERE ar.articleType = 'PORTFOLIO'")
    List<Article> findPortfolios();

    @Query(value = "SELECT a.* FROM article a LEFT JOIN detail_tags dt ON a.article_id = dt.article_id WHERE a.article_type = 'RECRUITING' AND (:detailTags IS NULL OR dt.detail_tags IN :detailTags) ORDER BY a.created_at DESC",
            countQuery = "SELECT count(*) FROM article a LEFT JOIN detail_tags dt ON a.article_id = dt.article_id WHERE a.article_type = 'RECRUITING' AND (:detailTags IS NULL OR dt.detail_tags IN :detailTags)",
            nativeQuery = true)
    Page<Article> findRecruitingByDetailTagsInOrderByCreatedAtDesc(@Param("detailTags") String[] detailTags, Pageable pageable);

    @Query("SELECT ar FROM Article ar WHERE ar.user = :user AND ar.articleType = 'PORTFOLIO'")
    List<Article> findPortfolioArticlesByUser(@Param("user") User user);

    @Query("SELECT DISTINCT ar FROM Article ar JOIN ar.content c WHERE ar.title LIKE %:keyword% OR c.data LIKE %:keyword% ORDER BY ar.createdAt DESC")
    Page<Article> findAllByTitleOrTextDataContaining(@Param("keyword") String keyword, Pageable pageable);
}
