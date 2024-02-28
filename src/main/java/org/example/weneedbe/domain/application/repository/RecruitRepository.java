package org.example.weneedbe.domain.application.repository;

import org.example.weneedbe.domain.application.domain.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {

}
