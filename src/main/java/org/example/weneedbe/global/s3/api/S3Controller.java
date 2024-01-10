package org.example.weneedbe.global.s3.api;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.weneedbe.global.s3.application.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3Controller {

  private final S3Service s3Service;

  @PostMapping("/upload-image")
  public ResponseEntity<String> uploadImage(MultipartFile multipartFile) throws IOException {
    return ResponseEntity.ok(s3Service.uploadImage(multipartFile));
  }

  @PostMapping("/upload-file")
  public ResponseEntity<List<String>> uploadFile(List<MultipartFile> multipartFiles) {
    return ResponseEntity.ok(s3Service.uploadFile(multipartFiles));
  }
}