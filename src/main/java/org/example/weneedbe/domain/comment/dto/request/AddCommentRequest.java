package org.example.weneedbe.domain.comment.dto.request;

import lombok.Getter;

@Getter
public class AddCommentRequest {

  private long parentId;
  private String content;
}
