package org.example.weneedbe.domain.comment.repository;

import org.example.weneedbe.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
