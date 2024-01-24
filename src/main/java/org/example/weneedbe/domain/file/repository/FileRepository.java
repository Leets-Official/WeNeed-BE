package org.example.weneedbe.domain.file.repository;

import org.example.weneedbe.domain.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

  void deleteAllByArticle_ArticleId(Long article_articleId);
}
