package com.test.s3uploader.uploader;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class Controller {

  private final UploaderService service;

  public Controller(UploaderService service) {
    this.service = service;
  }

  @GetMapping("/buckets")
  public List<Bucket> getBuckets() {
    return service.getBuckets();
  }

  @GetMapping("/buckets/{bucketName}")
  public Bucket getBucket(@PathVariable final String bucketName) {
    return service.getBucket(bucketName);
  }

  @GetMapping("/buckets/{bucketName}/objects")
  public List<S3ObjectSummary> getBucketObjects(@PathVariable final String bucketName) {
    return service.getBucketObjects(bucketName);
  }

  @GetMapping("/buckets/{bucketName}/objects/{objectName}")
  public ResponseEntity<byte[]> getBucketObjectContent(@PathVariable final String bucketName,
      @PathVariable final String objectName) throws IOException {
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + objectName + "\"").body(service.getBucketObjectContent(bucketName, objectName));
  }

  @PostMapping("/buckets/{bucketName}/objects")
  public void uploadObject(@PathVariable final String bucketName, @RequestParam("file") final MultipartFile file,
      @RequestParam(required = false) final String subFolderPath)
      throws IOException {
    service.uploadObject(bucketName, file, subFolderPath);
  }

}
