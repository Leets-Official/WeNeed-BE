package org.example.weneedbe.domain.article.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.weneedbe.domain.File.domain.File;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.domain.UserArticle;
import org.example.weneedbe.global.shared.entity.BaseTimeEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="article")
public class Article extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long articleId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "article_type", nullable = false)
    private Type articleType;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "heart_count", nullable = false)
    private Integer heartCount;

    @Column(name = "view_count", nullable = false)
    private Integer view_count;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    @ElementCollection
    @CollectionTable(name = "related_links", joinColumns = @JoinColumn(name = "article_id"))
    private List<String> relatedLinks;

    @ElementCollection
    @CollectionTable(name = "detail_skills", joinColumns = @JoinColumn(name = "article_id"))
    private List<String> detailSkills;

    @ElementCollection
    @CollectionTable(name = "detail_tags", joinColumns = @JoinColumn(name = "article_id"))
    private List<String> detailTags;

    @OneToMany(mappedBy = "article")
    private List<UserArticle> userArticles = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<File> files;
}
