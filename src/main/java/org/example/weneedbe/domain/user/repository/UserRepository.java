package org.example.weneedbe.domain.user.repository;

import org.example.weneedbe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
