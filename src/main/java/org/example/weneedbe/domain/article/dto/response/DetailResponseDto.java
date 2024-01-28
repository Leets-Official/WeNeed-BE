package org.example.weneedbe.domain.article.dto.response;

import lombok.*;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.article.domain.ContentData;
import org.example.weneedbe.domain.comment.domain.Comment;
import org.example.weneedbe.domain.file.domain.File;
import org.example.weneedbe.domain.user.domain.Department;
import org.example.weneedbe.domain.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DetailResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class DetailPortfolioDto {

        private DetailUserDto user;
        private DetailArticleDto portfolio;
        private List<WorkPortfolioArticleDto> workList;
        private List<CommentResponseDto> comments;


        public DetailPortfolioDto(Article article, User user, int heartCount, int bookmarkCount, List<WorkPortfolioArticleDto> workList,
                                  boolean isHearted, boolean isBookmarked, List<CommentResponseDto> commentList) {
            this.user = new DetailUserDto(user, user.getUserId().equals(article.getUser().getUserId()), isHearted, isBookmarked);
            this.portfolio = new DetailArticleDto(article, heartCount, bookmarkCount);
            this.workList = workList;
            this.comments = commentList;
        }
    }

    @Getter
    public static class DetailUserDto {
        private String nickname;
        private String profile;
        private boolean sameUser;
        private boolean isHearted;
        private boolean isBookmarked;

        public DetailUserDto(User user, boolean sameUser, boolean isHearted, boolean isBookmarked) {
            this.nickname = user.getNickname();
            this.profile = user.getProfile();
            this.sameUser = sameUser;
            this.isHearted = isHearted;
            this.isBookmarked = isBookmarked;
        }
    }

    @Getter
    public static class DetailArticleDto {
        private String thumbnail;
        private String title;
        private LocalDateTime createdAt;
        private int heatCount;
        private int viewCount;
        private int bookmarkCount;
        private int commentCount;
        private List<String> skills;
        private List<String> links;
        private List<String> tags;
        private List<String> files;
        private DetailWriterDto writer;
        private List<DetailPortfolioContentDto> contents;
        private List<DetailTeamMemberDto> teamMembers;

        public DetailArticleDto(Article article, int heatCount, int bookmarkCount) {
            this.thumbnail = article.getThumbnail();
            this.title = article.getTitle();
            this.createdAt = article.getCreatedAt();
            this.heatCount = heatCount;
            this.viewCount = article.getViewCount();
            this.bookmarkCount = bookmarkCount;
            this.commentCount = article.getCommentList().size();
            this.skills = article.getDetailSkills();
            this.links = article.getArticleLinks();
            this.tags = article.getDetailTags();
            this.files = article.getFiles().stream()
                    .map(File::getFileUrl)
                    .collect(Collectors.toList());
            this.writer = new DetailWriterDto(article);
            this.contents = getPortfolioContents(article.getContent());
            this.teamMembers = article.getUserArticles().stream()
                    .map(userArticle -> new DetailTeamMemberDto(userArticle.getUser()))
                    .collect(Collectors.toList());
        }
    }

    private static List<DetailPortfolioContentDto> getPortfolioContents(List<ContentData> contents) {
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
    public static class DetailWriterDto {
        private Long userId;
        private String writerNickname;
        private Department major;
        private String profile;
        private Integer grade;

        public DetailWriterDto(Article article) {
            this.userId = article.getUser().getUserId();
            this.writerNickname = article.getUser().getNickname();
            this.major = article.getUser().getMajor();
            this.profile = article.getUser().getProfile();
            this.grade = article.getUser().getGrade();
        }
    }

    @Getter
    @Setter
    public static class DetailPortfolioContentDto {
        private Long id;
        private String type;
        private String textData;
        private String imageData;
    }

    @Getter
    public static class WorkPortfolioArticleDto {
        private Long articleId;
        private String thumbnail;
        private String title;
        private boolean bookmarked;

        public WorkPortfolioArticleDto(Article article, boolean bookmarked) {
            this.articleId = article.getArticleId();
            this.thumbnail = article.getThumbnail();
            this.title = article.getTitle();
            this.bookmarked = bookmarked;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class DetailRecruitDto {
        private DetailUserDto user;
        private DetailArticleDto recruit;
        private List<CommentResponseDto> comments;

        public DetailRecruitDto(Article article, User user, int heartCount, int bookmarkCount, boolean isHearted, boolean isBookmarked, List<CommentResponseDto> commentList) {
            this.user = new DetailUserDto(user, user.getUserId().equals(article.getUser().getUserId()), isHearted, isBookmarked);
            this.recruit = new DetailArticleDto(article, heartCount, bookmarkCount);
            this.comments = commentList;
        }

    }

    @Getter
    @Setter
    public static class CommentResponseDto {
        private Long commentId;
        private String content;
        private Long userId;
        private String nickname;
        private Department major;
        private Integer grade;
        private LocalDateTime createdAt;
        private String profile;
        private List<CommentResponseDto> children;

        public CommentResponseDto(Comment comment, List<Comment> commentList) {
            this.commentId = comment.getCommentId();
            this.content = comment.getContent();
            this.userId = comment.getWriter().getUserId();
            this.nickname = comment.getWriter().getNickname();
            this.major = comment.getWriter().getMajor();
            this.grade = comment.getWriter().getGrade();
            this.createdAt = comment.getCreatedAt();
            this.profile = comment.getWriter().getProfile();
            if (!comment.getChildren().isEmpty()) {
                this.children = comment.getChildren().stream()
                        .map(child -> new CommentResponseDto(child, commentList))
                        .collect(Collectors.toList());
            } else {
                this.children = new ArrayList<>();
            }
        }
    }

    @Getter
    public static class DetailTeamMemberDto{
        private Long userId;
        private String nickname;
        private String profile;

        public DetailTeamMemberDto(User user){
            this.userId = user.getUserId();
            this.nickname = user.getNickname();
            this.profile = user.getProfile();
        }
    }
}