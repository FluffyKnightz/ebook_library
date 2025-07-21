package com.fluffyknightz.ebook_library.config.s3_object;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ObjectUsage {

    private final S3Client s3Client;

    public void create(MultipartFile file, String key, String bucket) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                            .bucket(bucket)
                                                            .contentType(file.getContentType())
                                                            .key(key)
                                                            .serverSideEncryption(ServerSideEncryption.AWS_KMS)
                                                            .bucketKeyEnabled(true)
                                                            .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    public void delete(List<String> keys, String bucket) {
        try {
            List<ObjectIdentifier> objectsToDelete = keys.stream()
                                                         .map(key -> ObjectIdentifier.builder()
                                                                                     .key(key)
                                                                                     .build())
                                                         .toList();

            s3Client.deleteObjects(builder -> builder.bucket(bucket)
                                                     .bucket(bucket)
                                                     .delete(d -> d.objects(objectsToDelete)
                                                                   .quiet(false)));// set true to only response error, false response list for success at delete
        } catch (Exception e) {
            log.warn("Tried to delete S3 objects but failed (likely not uploaded): {}", e.getMessage());
        }
    }
}
