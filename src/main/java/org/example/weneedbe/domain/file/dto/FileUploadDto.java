package org.example.weneedbe.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileUploadDto {

  private final String fileName;
  private final String fileUrl;
}