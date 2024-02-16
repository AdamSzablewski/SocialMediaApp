package com.adamszablewski.SocialMediaApp.s3;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@AllArgsConstructor
public class S3service {
    private final S3Client s3Client;
    private S3buckets s3buckets;


    public void putObject(String bucketName, String key, byte[] byteFile){
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(s3buckets.getCustomer())
                .key(key)
                .build();
        s3Client.putObject(objectRequest, RequestBody.fromBytes(byteFile));
    }
    public byte[] getObject(String bucketName, String key){
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(s3buckets.getCustomer())
                .key(key)
                .build();
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(objectRequest);
        try {
            return response.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
