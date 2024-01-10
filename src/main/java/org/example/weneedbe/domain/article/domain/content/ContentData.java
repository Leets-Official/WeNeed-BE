package org.example.weneedbe.domain.article.domain.content;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ContentData {
  private Long id;
  private String type;

  @Embedded
  private ParagraphData paragraph; // paragraph 데이터를 위한 클래스

  @Embedded
  private ImageData image; // image 데이터를 위한 클래스
}