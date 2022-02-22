package com.test.s3uploader.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class Client {

  private final AmazonS3 s3client;

  public Client(
      @Value("${s3.accessKey}") final String accessKey,
      @Value("${s3.secretKey}") final String secretKey,
      @Value("${s3.region}") final String region
  ) {
    this.s3client = AmazonS3ClientBuilder
        .standard()
        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
            accessKey,
            secretKey
        )))
        .withRegion(Regions.fromName(region))
        .build();
  }

  public List<Bucket> getBuckets() {
    return s3client.listBuckets();
  }

  public List<S3ObjectSummary> getObjects(final String bucketName) {
    return s3client.listObjects(bucketName).getObjectSummaries();
  }

  public S3Object getObject(final String bucketName, final String objectName) {
    return s3client.getObject(bucketName, objectName);
  }

  public void putObject(final String bucketName, final String objectName, final File file) {
    s3client.putObject(
        bucketName,
        objectName,
        file
    );
  }

}
