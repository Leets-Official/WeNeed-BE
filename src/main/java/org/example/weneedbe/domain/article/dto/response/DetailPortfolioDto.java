package org.example.weneedbe.domain.article.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.weneedbe.domain.user.domain.Department;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DetailPortfolioDto {
    private DetailUserDto user;
    private DetailPortfolioArticleDto portfolio;
    private List<WorkPortfolioArticleDto> workList;

    @Getter
    public static class DetailUserDto {
        private String nickname;
        private boolean sameUser;
    }


    @Getter
    public static class DetailPortfolioArticleDto {
        private String thumbnail;
        private String title;
        private LocalDateTime createdAt;
        private int heatCount;
        private int viewCount;
        private int bookmarkCount;
        private DetailWriterPortfolioDto writer;
        private List<DetailPortfolioContentDto> contents;

    }

    @Getter
    public static class DetailWriterPortfolioDto {
        private Long userId;
        private String writerNickname;
        private Department major;
        private String profile;
        private Integer grade;
    }

    @Getter
    public static class DetailPortfolioContentDto{
        private Long id;
        private String type;
        private String textData;
        private String imageData;
    }

    @Getter
    public static class WorkPortfolioArticleDto{
        private Long articleId;
        private String thumbnail;
        private String title;
    }




}
