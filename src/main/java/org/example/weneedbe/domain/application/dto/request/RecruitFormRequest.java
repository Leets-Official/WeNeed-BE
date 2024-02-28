package org.example.weneedbe.domain.application.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class RecruitFormRequest {

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
}