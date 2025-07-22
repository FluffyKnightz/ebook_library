package com.fluffyknightz.ebook_library.config.s3_object;

import com.fluffyknightz.ebook_library.config.s3_object.dto.S3UploadResult;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ObjectUsage {

    private final S3Client s3Client;

    public S3UploadResult create(MultipartFile file, String bucket, String region) throws IOException {

        String key = UUID.randomUUID() + "_" + file.getOriginalFilename() + "_" + LocalDateTime.now();
        String objectUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                            .bucket(bucket)
                                                            .contentType(file.getContentType())
                                                            .key(key)
                                                            .serverSideEncryption(ServerSideEncryption.AWS_KMS)
                                                            .bucketKeyEnabled(true)
                                                            .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return new S3UploadResult(key, objectUrl);
    }

    public void delete(List<String> keys, String bucket) {

            List<ObjectIdentifier> objectsToDelete = keys.stream()
                                                         .map(key -> ObjectIdentifier.builder()
                                                                                     .key(key)
                                                                                     .build())
                                                         .toList();

            s3Client.deleteObjects(builder -> builder.bucket(bucket)
                                                     .bucket(bucket)
                                                     .delete(d -> d.objects(objectsToDelete)
                                                                   .quiet(false)));// set true to only response error, false response list for success at delete

    }
}
