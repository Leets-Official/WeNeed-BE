package org.example.weneedbe.domain.article.exception;

import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.exception.ServiceException;

public class ArticleNotFoundException extends ServiceException {
  public ArticleNotFoundException() {
    super(ErrorCode.ARTICLE_NOT_FOUND);
  }
}
