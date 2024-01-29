package org.example.weneedbe.domain.article.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.weneedbe.domain.article.dto.request.ArticleRequest;
import org.example.weneedbe.domain.bookmark.domain.Bookmark;
import org.example.weneedbe.domain.comment.domain.Comment;
import org.example.weneedbe.domain.file.domain.File;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.domain.user.domain.UserArticle;
import org.example.weneedbe.global.shared.entity.BaseTimeEntity;

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

    @Column(name = "view_count")
    private int viewCount;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    @ElementCollection
    @CollectionTable(name = "detail_skills", joinColumns = @JoinColumn(name = "article_id"))
    private List<String> detailSkills;

    @ElementCollection
    @CollectionTable(name = "detail_tags", joinColumns = @JoinColumn(name = "article_id"))
    private List<String> detailTags; //추후 Tag enum 으로 구현

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<UserArticle> userArticles = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<File> files;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleLike> likeList = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarkList = new ArrayList<>();

    public static Article of(String thumbnail, List<String> images, List<String> files,
        ArticleRequest request, User user) {

        Article article = Article.builder()
            .user(user)
            .articleType(request.getArticleType())
            .thumbnail(thumbnail)
            .title(request.getTitle())
            .detailSkills(request.getSkills())
            .detailTags(request.getTags())
            .build();

        article.setContent(convertImagesToContent(images, request));

        article.setFiles(convertFilesToEntity(files, article));

        return article;
    }

    public void plusViewCount(int view){
        this.viewCount = view;
    }

    public void updatePortfolio(String thumbnail, List<String> images, List<String> files, ArticleRequest request,
        List<UserArticle> userArticles) {
        this.articleType = request.getArticleType();
        this.thumbnail = thumbnail;
        this.title = request.getTitle();
        this.detailSkills = request.getSkills();
        this.detailTags = request.getTags();
        this.content = convertImagesToContent(images, request);
        this.files = convertFilesToEntity(files, this);
        this.userArticles = userArticles;
    }

    public void updateRecruit(String thumbnail, List<String> images, List<String> files,
        ArticleRequest request) {
        this.articleType = request.getArticleType();
        this.thumbnail = thumbnail;
        this.title = request.getTitle();
        this.detailSkills = request.getSkills();
        this.detailTags = request.getTags();
        this.content = convertImagesToContent(images, request);
        this.files = convertFilesToEntity(files, this);
    }

    private static List<ContentData> convertImagesToContent(List<String> images,
        ArticleRequest request) {

        List<ContentData> contentDatas = request.getContent();
        Iterator<String> imagesIterator = images.iterator();

        for (ContentData contentData : contentDatas) {
            if ("image".equals(contentData.getType()) && imagesIterator.hasNext()) {
                contentData.setData(imagesIterator.next());
            }
        }
        return contentDatas;
    }

    private static List<File> convertFilesToEntity(List<String> files, Article article) {

        List<File> fileList = new ArrayList<>();

        for (String fileUrl : files) {
            File file = new File(fileUrl, article);
            fileList.add(file);
        }
        return fileList;
    }
}