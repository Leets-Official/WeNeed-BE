package org.example.weneedbe.domain.article.dto.response.main;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.weneedbe.domain.user.domain.Department;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RecruitArticleDto {
    private Long articleId;
    private String nickname;
    private String profile;
    private Department major;
    private Integer grade;
    private LocalDateTime createdAt;
    private String title;
    private String thumbnail;
    private String content;
    private int commentCount;
    private int viewCount;
    private int heartCount;
    private int bookmarkCount;
}
