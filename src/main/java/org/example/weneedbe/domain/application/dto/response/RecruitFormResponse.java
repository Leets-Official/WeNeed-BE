package org.example.weneedbe.domain.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.example.weneedbe.domain.application.domain.Recruit;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.user.domain.Department;
import org.example.weneedbe.domain.user.domain.User;

@Getter
public class RecruitFormResponse {

  private UserDetailDto user;
  private ArticleDetailDto article;
  private RecruitFormDto recruitForm;

  public RecruitFormResponse(User user, Article article, int heartCount, int bookmarkCount,
      Recruit recruit) {
    this.user = new UserDetailDto(user);
    this.article = new ArticleDetailDto(article, heartCount, bookmarkCount);
    this.recruitForm = new RecruitFormDto(recruit);
  }

  @Getter
  public static class UserDetailDto {
    private Long userId;
    private String profile;
    private String nickname;
    private Department major;
    private Integer grade;

    public UserDetailDto(User user) {
      this.userId = user.getUserId();
      this.profile = user.getProfile();
      this.nickname = user.getNickname();
      this.major = user.getMajor();
      this.grade = user.getGrade();
    }
  }

  @Getter
  public static class ArticleDetailDto {
    private LocalDateTime createdAt;
    private int viewCount;
    private int heartCount;
    private int bookmarkCount;

    public ArticleDetailDto(Article article, int heartCount, int bookmarkCount) {
      this.createdAt = article.getCreatedAt();
      this.viewCount = article.getViewCount();
      this.heartCount = heartCount;
      this.bookmarkCount = bookmarkCount;
    }
  }

  @Getter
  public static class RecruitFormDto {
    private String category;
    private List<String> detailTags;
    private String deadline;
    private String description;
    private String task_need;
    private Integer member_count;
    private String phone;
    private List<String> crew_questions;
    private String content;
    private List<String> keywords;

    public RecruitFormDto(Recruit recruit) {
      this.category = recruit.getCategory();
      this.detailTags = recruit.getDetailTags();
      this.deadline = recruit.getDeadline();
      this.description = recruit.getDescription();
      this.task_need = recruit.getTaskNeed();
      this.member_count = recruit.getMemberCount();
      this.phone = recruit.getPhone();
      this.crew_questions = recruit.getCrewQuestions();
      this.content = recruit.getContent();
      this.keywords = recruit.getKeywords();
    }
  }
}
