package org.example.weneedbe.global.s3.exception;

import org.example.weneedbe.global.error.ErrorCode;
import org.example.weneedbe.global.error.exception.ServiceException;

public class FileUploadErrorException extends ServiceException {
  public FileUploadErrorException() {
    super(ErrorCode.FILE_UPLOAD_ERROR);
  }
}
