package org.example.weneedbe.domain.application.repository;

import org.example.weneedbe.domain.application.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
