package org.example.weneedbe.domain.comment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.comment.dto.request.AddCommentRequest;
import org.example.weneedbe.domain.user.domain.User;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    @JsonIgnore
    private Article article;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "parent_id")
    private Long parentId;

    @OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    public static Comment of(AddCommentRequest request, Article article, User user) {
        return Comment.builder()
            .writer(user)
            .article(article)
            .content(request.getContent())
            .parentId(request.getParentId())
            .build();
    }

    public void addChild(Comment comment) {
        children.add(comment);
    }

    public void deleteChild() {
        children.clear();
    }
}