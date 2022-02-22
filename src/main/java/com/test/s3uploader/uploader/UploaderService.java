package com.test.s3uploader.uploader;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.test.s3uploader.s3.Client;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class UploaderService {

  private final Client client;

  public UploaderService(Client client) {
    this.client = client;
  }

  public List<Bucket> getBuckets() {
    return client.getBuckets();
  }

  public Bucket getBucket(final String bucketName) {
    return client.getBuckets().stream().filter(bucket -> bucket.getName().equals(bucketName)).findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(String.format("No bucket exists with the name '%s'", bucketName)));
  }

  public List<S3ObjectSummary> getBucketObjects(final String bucketName) {
    return client.getObjects(bucketName);
  }

  public byte[] getBucketObjectContent(final String bucketName, final String objectName) throws IOException {
    S3Object s3object = client.getObject(bucketName, objectName);
    S3ObjectInputStream inputStream = s3object.getObjectContent();
    return inputStream.readAllBytes();
  }

  public void uploadObject(final String bucketName, final MultipartFile multipartFile, final String subFolderPath)
      throws IOException {
    final File file = copyToFile(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
    client.putObject(
        bucketName,
        getObjectName(subFolderPath, multipartFile),
        file
    );
    Files.deleteIfExists(file.toPath());
  }

  private String getObjectName(final String subFolderPath, final MultipartFile multipartFile) {
    return getObjectPrefix(subFolderPath) + multipartFile.getOriginalFilename();
  }

  private String getObjectPrefix(final String subFolderPath) {
    String prefix = "";
    if (StringUtils.hasText(subFolderPath)) {
      if (subFolderPath.startsWith("/")) {
        prefix = subFolderPath.substring(1);
      }
      if (!prefix.endsWith("/")) {
        prefix = prefix.concat("/");
      }
    }
    return prefix;
  }

  private static File copyToFile(final InputStream inputStream, final String fileName) throws IOException {
    final File targetFile = new File("/tmp/" + fileName);
    java.nio.file.Files.copy(
        inputStream,
        targetFile.toPath(),
        StandardCopyOption.REPLACE_EXISTING);
    IOUtils.closeQuietly(inputStream);
    return targetFile;
  }
}
