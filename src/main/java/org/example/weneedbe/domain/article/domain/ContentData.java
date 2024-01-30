package org.example.weneedbe.domain.article.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ContentData {
  private String id;
  private String type;
  private String data;
}