package org.example.weneedbe.domain.article.domain;

import jakarta.persistence.Embeddable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ContentData {
  private UUID id = UUID.randomUUID();
  private String type;
  private String data;
}