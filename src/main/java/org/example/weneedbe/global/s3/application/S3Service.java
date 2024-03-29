package org.example.weneedbe.global.s3.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.weneedbe.domain.file.dto.FileUploadDto;
import org.example.weneedbe.global.s3.exception.FileUploadErrorException;
import org.example.weneedbe.global.s3.exception.InvalidFileException;
import org.example.weneedbe.global.s3.exception.InvalidImageFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

  private final AmazonS3 amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName; //버킷 이름

  public String uploadImage(MultipartFile multipartFile) throws IOException {
    String fileName = createFileName(multipartFile.getOriginalFilename());

    String fileExtension = getFileExtension(fileName);
    if (fileExtension.equalsIgnoreCase(".png") || fileExtension.equalsIgnoreCase(".jpeg")
        || fileExtension.equalsIgnoreCase(".jpg")) {

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentLength(multipartFile.getSize());
      objectMetadata.setContentType(multipartFile.getContentType());

      amazonS3Client.putObject(bucketName, fileName, multipartFile.getInputStream(),
          objectMetadata);
      return getS3(bucketName, fileName);
    } else {
      throw new InvalidImageFileException();
    }
  }

  public List<String> uploadImages(List<MultipartFile> multipartFiles) throws IOException {

    List<String> fileNameList = new ArrayList<>();

    multipartFiles.forEach(file -> {
      String fileName = createFileName(file.getOriginalFilename());
      String fileExtension = getFileExtension(fileName);

      if (fileExtension.equalsIgnoreCase(".png") || fileExtension.equalsIgnoreCase(".jpeg")
          || fileExtension.equalsIgnoreCase(".jpg")) {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
          amazonS3Client.putObject(
              new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                  .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
          throw new FileUploadErrorException();
        }
        fileNameList.add(getS3(bucketName, fileName));
      } else {
        throw new InvalidImageFileException();
      }
    });
    return fileNameList;
  }

  public List<FileUploadDto> uploadFiles(List<MultipartFile> multipartFiles){
    List<FileUploadDto> uploadResults = new ArrayList<>();

    // forEach 구문을 통해 multipartFiles 리스트로 넘어온 파일들을 순차적으로 fileNameList 에 추가
    multipartFiles.forEach(file -> {
      String originalFileName = file.getOriginalFilename();
      String fileName = createFileName(originalFileName);

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentLength(file.getSize());
      objectMetadata.setContentType(file.getContentType());

      try (InputStream inputStream = file.getInputStream()) {
        amazonS3Client.putObject(
            new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
      } catch (IOException e) {
        throw new FileUploadErrorException();
      }

      FileUploadDto uploadDto = new FileUploadDto(originalFileName, getS3(bucketName, fileName));
      uploadResults.add(uploadDto);
    });
    return uploadResults;
  }

  public String uploadFile(MultipartFile file){
    String fileName = createFileName(file.getOriginalFilename());
    String fileExtension = getFileExtension(fileName);

    if(fileExtension.equalsIgnoreCase(".pdf")){
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentLength(file.getSize());
      objectMetadata.setContentType(file.getContentType());

      try (InputStream inputStream = file.getInputStream()) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
      } catch (IOException e) {
        throw new FileUploadErrorException();
      }
      return getS3(bucketName, fileName);
    }
    throw new InvalidFileException();
  }

  public void deleteFile(String fileUrl) {
    String splitStr = "files/";
    String fileName = fileUrl.substring(fileUrl.lastIndexOf(splitStr) + splitStr.length());

    amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
  }

  public String createFileName(String fileName){
    return UUID.randomUUID().toString().concat(getFileExtension(fileName));
  }

  private String getFileExtension(String fileName){
    try{
      return fileName.substring(fileName.lastIndexOf("."));
    } catch (StringIndexOutOfBoundsException e){
      throw new InvalidImageFileException();
    }
  }

  private String getS3(String bucket, String fileName) {
    return amazonS3Client.getUrl(bucket, fileName).toString();
  }
}
