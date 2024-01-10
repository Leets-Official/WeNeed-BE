package org.example.weneedbe.domain.article.dto.request;

import java.util.List;
import lombok.Data;
import org.example.weneedbe.domain.article.domain.article.Type;
import org.example.weneedbe.domain.article.domain.content.ContentData;
import org.example.weneedbe.domain.user.domain.Fields;

@Data
public class AddArticleRequest {

  private Type articleType;
  private String thumbnail;
  private String title;
  private List<ContentData> content;
  private Fields articleField;
  private List<String> links;
  private List<String> skills;
  private List<String> tags;
  private List<String> files;
  private List<Long> teamMembersId;
}