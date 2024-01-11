package org.example.weneedbe.global.s3.exception;

import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.exception.ServiceException;

public class InvalidImageFileException extends ServiceException {
  public InvalidImageFileException() {
    super(ErrorCode.INVALID_IMAGE_FILE);
  }
}
