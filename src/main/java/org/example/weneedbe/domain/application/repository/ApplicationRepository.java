package org.example.weneedbe.domain.application.repository;

import org.example.weneedbe.domain.application.domain.Application;
import org.example.weneedbe.domain.article.domain.Type;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.domain.UserArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Page<Application> findAllByUser(User user, Pageable pageable);

    List<Application> findAllByRecruit_RecruitId(Long recruitId);
}
