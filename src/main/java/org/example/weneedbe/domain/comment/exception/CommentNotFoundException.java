package org.example.weneedbe.domain.comment.exception;

import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.exception.ServiceException;

public class CommentNotFoundException extends ServiceException {
  public CommentNotFoundException() {
    super(ErrorCode.COMMENT_NOT_FOUND);
  }
}
