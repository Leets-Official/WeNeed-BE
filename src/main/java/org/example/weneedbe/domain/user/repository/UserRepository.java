package org.example.weneedbe.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.example.weneedbe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

   List<User> findAllByNicknameStartingWith(String nickname);

   Optional<User> findByEmail(String email);

   boolean existsByNickname(String email);
}
