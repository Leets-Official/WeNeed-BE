package org.example.weneedbe.domain.application.repository;

import java.util.Optional;
import org.example.weneedbe.domain.application.domain.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {

  Optional<Recruit> findByArticle_ArticleId(Long article_articleId);
  boolean existsByArticle_ArticleId(Long article_articleId);

}
