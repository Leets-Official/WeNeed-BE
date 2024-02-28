package org.example.weneedbe.domain.application.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.weneedbe.domain.application.dto.request.RecruitFormRequest;
import org.example.weneedbe.domain.article.domain.Article;
import org.example.weneedbe.domain.user.domain.User;
import org.example.weneedbe.global.shared.entity.BaseTimeEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recruit extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long recruitId;

  private String category;

  @ElementCollection
  @CollectionTable(name = "recruit_detail_tags", joinColumns = @JoinColumn(name = "recruit_id"))
  private List<String> detailTags = new ArrayList<>();

  private String deadline;

  private String description;

  private String taskNeed;

  private Integer memberCount;

  private String phone;

  @ElementCollection
  @CollectionTable(name = "crew_questions", joinColumns = @JoinColumn(name = "recruit_id"))
  private List<String> crewQuestions = new ArrayList<>();

  private String content;

  @ElementCollection
  @CollectionTable(name = "keywords", joinColumns = @JoinColumn(name = "recruit_id"))
  private List<String> keywords = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_id")
  private Article article;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public static Recruit of(Article article, User user, RecruitFormRequest request) {
    return Recruit.builder()
        .category(request.getCategory())
        .detailTags(request.getDetailTags())
        .deadline(request.getDeadline())
        .description(request.getDescription())
        .taskNeed(request.getTask_need())
        .memberCount(request.getMember_count())
        .phone(request.getPhone())
        .crewQuestions(request.getCrew_questions())
        .content(request.getContent())
        .keywords(request.getKeywords())
        .article(article)
        .user(user)
        .build();
  }
}
