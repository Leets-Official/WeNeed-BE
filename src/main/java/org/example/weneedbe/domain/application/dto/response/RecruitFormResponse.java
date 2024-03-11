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

  private String nickname;
  private Long userId;
  private boolean sameUser;
  private UserDetailDto recruitUser;
  private ArticleDetailDto article;
  private RecruitFormDto recruitForm;

  public RecruitFormResponse(User recruitUser, Article article, int heartCount, int bookmarkCount,
      Recruit recruit, User loggedInUser) {
    this.nickname = loggedInUser.getNickname();
    this.userId = loggedInUser.getUserId();
    this.sameUser = recruitUser.getUserId() == loggedInUser.getUserId();
    this.recruitUser = new UserDetailDto(recruitUser);
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
    private String taskNeed;
    private Integer memberCount;
    private String phone;
    private List<String> crewQuestions;
    private String content;
    private List<String> keywords;

    public RecruitFormDto(Recruit recruit) {
      this.category = recruit.getCategory();
      this.detailTags = recruit.getDetailTags();
      this.deadline = recruit.getDeadline();
      this.description = recruit.getDescription();
      this.taskNeed = recruit.getTaskNeed();
      this.memberCount = recruit.getMemberCount();
      this.phone = recruit.getPhone();
      this.crewQuestions = recruit.getCrewQuestions();
      this.content = recruit.getContent();
      this.keywords = recruit.getKeywords();
    }
  }
}
