package org.example.weneedbe.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileUploadDto {

  private String fileName;
  private String fileUrl;
}
