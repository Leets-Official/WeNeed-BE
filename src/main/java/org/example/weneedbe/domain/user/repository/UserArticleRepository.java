package org.example.weneedbe.domain.user.repository;

import org.example.weneedbe.domain.article.domain.Type;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.domain.UserArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserArticleRepository extends JpaRepository<UserArticle, Long> {

    List<UserArticle> findAllByUserAndArticle_ArticleTypeOrderByArticle_CreatedAtDesc(User user, Type articleType);

}
