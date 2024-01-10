package org.example.weneedbe.domain.article.domain.content;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ImageData {

  private String url;
}
