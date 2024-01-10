package org.example.weneedbe.domain.article.domain.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.weneedbe.domain.File.domain.File;
import org.example.weneedbe.domain.article.domain.content.ContentData;
import org.example.weneedbe.domain.article.dto.request.AddArticleRequest;
import org.example.weneedbe.domain.comment.domain.Comment;
import org.example.weneedbe.domain.user.domain.Fields;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.domain.UserArticle;
import org.example.weneedbe.global.shared.entity.BaseTimeEntity;
import org.hibernate.annotations.Where;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Table(name="article")
public class Article extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long articleId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "article_type", nullable = false)
    private Type articleType;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "title", nullable = false)
    private String title;

    @ElementCollection
    @CollectionTable(name = "content_data", joinColumns = @JoinColumn(name = "article_id"))
    @OrderColumn(name = "content_sequence")
    private List<ContentData> content;

    @Column(name = "heart_count")
    private int heartCount;

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "article_field", nullable = false)
    private Fields articleField;

    @ElementCollection
    @CollectionTable(name = "article_links", joinColumns = @JoinColumn(name = "article_id"))
    private List<String> articleLinks;

    @ElementCollection
    @CollectionTable(name = "detail_skills", joinColumns = @JoinColumn(name = "article_id"))
    private List<String> detailSkills;

    @ElementCollection
    @CollectionTable(name = "detail_tags", joinColumns = @JoinColumn(name = "article_id"))
    private List<String> detailTags; //추후 Tag enum 으로 구현

    @OneToMany(mappedBy = "article", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<UserArticle> userArticles = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<File> files;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "parent_id is null")
    private List<Comment> commentList = new ArrayList<>();

    public static Article of(AddArticleRequest request, User user) {

        Article article = Article.builder()
            .user(user)
            .articleType(request.getArticleType())
            .thumbnail(request.getThumbnail())
            .title(request.getTitle())
            .content(request.getContent())
            .articleField(request.getArticleField())
            .articleLinks(request.getLinks())
            .detailSkills(request.getSkills())
            .detailTags(request.getTags())
            .build();

        List<File> fileList = new ArrayList<>();
        for (String fileUrl : request.getFiles()) {
            fileList.add(new File(fileUrl, article));
        }
        article.setFiles(fileList);

        return article;
    }
}