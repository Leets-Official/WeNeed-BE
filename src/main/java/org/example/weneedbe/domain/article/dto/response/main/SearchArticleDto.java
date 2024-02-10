package org.example.weneedbe.domain.article.dto.response.main;

import lombok.Getter;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.user.domain.Department;

import java.util.List;

@Getter
public class SearchArticleDto {
    private Long articleId;
    private String thumbnail;
    private String title;
    private List<String> detailTags;
    private String profile;
    private String writerNickname;
    private Department major;
    private Integer grade;
    private int viewCount;
    private int heartCount;
    private int bookmarkCount;
    private boolean isBookmarked;
    private boolean isHearted;

    public SearchArticleDto(Article article, int heartCount, int bookmarkCount, boolean isBookmarked, boolean isHearted){
        this.articleId = article.getArticleId();
        this.thumbnail = article.getThumbnail();
        this.title = article.getTitle();
        this.detailTags = article.getDetailTags();
        this.profile = article.getUser().getProfile();
        this.writerNickname = article.getUser().getNickname();
        this.major = article.getUser().getMajor();
        this.grade = article.getUser().getGrade();
        this.viewCount = article.getViewCount();
        this.heartCount = heartCount;
        this.bookmarkCount = bookmarkCount;
        this.isBookmarked = isBookmarked;
        this.isHearted = isHearted;
    }
}
