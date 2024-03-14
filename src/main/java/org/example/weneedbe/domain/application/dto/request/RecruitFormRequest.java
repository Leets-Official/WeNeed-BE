package org.example.weneedbe.domain.application.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class RecruitFormRequest {

  private String category;
  private String deadline;
  private String description;
  private String taskNeed;
  private Integer memberCount;
  private String phone;
  private List<String> crewQuestions;
  private String content;
  private List<String> keywords;
}