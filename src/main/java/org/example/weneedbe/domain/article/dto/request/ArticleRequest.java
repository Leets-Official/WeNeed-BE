package org.example.weneedbe.domain.article.dto.request;

import java.util.List;
import lombok.Data;
import org.example.weneedbe.domain.article.domain.ContentData;
import org.example.weneedbe.domain.article.domain.Type;

@Data
public class ArticleRequest {

  private Type articleType;
  private String title;
  private List<ContentData> content;
  private List<String> skills;
  private List<String> tags;
  private List<Long> teamMembersId;
}