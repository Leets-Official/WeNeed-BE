package org.example.weneedbe.domain.article.domain.content;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ParagraphData {
  @Column(length = 1000)
  private String text;
}

