package org.example.weneedbe.domain.article.dto.response;

import lombok.*;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.article.domain.ContentData;
import org.example.weneedbe.domain.user.domain.Department;
import org.example.weneedbe.domain.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class DetailPortfolioDto {
    private DetailUserDto user;
    private DetailPortfolioArticleDto portfolio;
    private List<WorkPortfolioArticleDto> workList;

    public DetailPortfolioDto(Article article, User user, int heartCount, int bookmarkCount, List<Article> userPortfolio) {
        this.user = new DetailUserDto(user, user.getUserId().equals(article.getUser().getUserId()));
        this.portfolio = new DetailPortfolioArticleDto(article, heartCount, bookmarkCount);
        this.workList = initializeWorkList(userPortfolio, article);
    }

    @Getter
    public class DetailUserDto {
        private String nickname;
        private boolean sameUser;

        public DetailUserDto(User user, boolean sameUser) {
            this.nickname = user.getNickname();
            this.sameUser = sameUser;
        }
    }

    @Getter
    public class DetailPortfolioArticleDto {
        private String thumbnail;
        private String title;
        private LocalDateTime createdAt;
        private int heatCount;
        private int viewCount;
        private int bookmarkCount;
        private DetailWriterPortfolioDto writer;
        private List<DetailPortfolioContentDto> contents;

        public DetailPortfolioArticleDto(Article article, int heatCount, int bookmarkCount) {
            this.thumbnail = article.getThumbnail();
            this.title = article.getTitle();
            this.createdAt = article.getCreatedAt();
            this.heatCount = heatCount;
            this.viewCount = article.getViewCount();
            this.bookmarkCount = bookmarkCount;
            this.writer = new DetailWriterPortfolioDto(article);
            this.contents = getPortfolioContents(article.getContent());
        }

    }

    private List<DetailPortfolioContentDto> getPortfolioContents(List<ContentData> contents) {
        List<DetailPortfolioContentDto> contentDtoList = new ArrayList<>();
        for (ContentData contentData : contents) {
            DetailPortfolioContentDto contentDto = new DetailPortfolioContentDto();
            contentDto.setId(contentData.getId());
            contentDto.setType(contentData.getType());
            if ("image".equals(contentData.getType())) {
                contentDto.setImageData(contentData.getImageData());
            } else if ("text".equals(contentData.getType())) {
                contentDto.setTextData(contentData.getTextData());
            }
            contentDtoList.add(contentDto);
        }
        return contentDtoList;
    }

    @Getter
    public class DetailWriterPortfolioDto {
        private Long userId;
        private String writerNickname;
        private Department major;
        private String profile;
        private Integer grade;

        public DetailWriterPortfolioDto(Article article) {
            this.userId = article.getUser().getUserId();
            this.writerNickname = article.getUser().getNickname();
            this.major = article.getUser().getMajor();
            this.profile = article.getUser().getProfile();
            this.grade = article.getUser().getGrade();
        }
    }

    @Getter
    @Setter
    public class DetailPortfolioContentDto {
        private Long id;
        private String type;
        private String textData;
        private String imageData;
    }

    @Getter
    public class WorkPortfolioArticleDto {
        private Long articleId;
        private String thumbnail;
        private String title;

        public WorkPortfolioArticleDto(Article article) {
            this.articleId = article.getArticleId();
            this.thumbnail = article.getThumbnail();
            this.title = article.getTitle();
        }
    }

    private List<WorkPortfolioArticleDto> initializeWorkList(List<Article> userPortfolio, Article article) {
        List<WorkPortfolioArticleDto> workList = new ArrayList<>();
        for (Article portfolio : userPortfolio) {
            /* 상세조회하는 게시물 제외하고 workList에 추가 */
            if (!portfolio.getArticleId().equals(article.getArticleId())) {
                workList.add(new WorkPortfolioArticleDto(portfolio));
            }
        }
        return workList;
    }
}