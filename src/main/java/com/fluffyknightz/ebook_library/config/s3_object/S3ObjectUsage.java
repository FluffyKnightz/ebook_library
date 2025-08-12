package com.fluffyknightz.ebook_library.config.s3_object;

import com.fluffyknightz.ebook_library.config.s3_object.dto.S3UploadResult;
import io.awspring.cloud.s3.S3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ObjectUsage {

    private final S3Client s3Client;

    public S3UploadResult create(MultipartFile file, String bucket, String region) throws IOException {

        String key = UUID.randomUUID() + "_" + file.getOriginalFilename() + "_" + LocalDateTime.now();

        String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8);
        String objectUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + encodedKey;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucket).contentType(file.getContentType())
                                                            .key(key)
                                                            // cant use object url if encryption and bucket key are enabled
//                                                            .serverSideEncryption(ServerSideEncryption.AWS_KMS)
//                                                            .bucketKeyEnabled(true)
                                                            .build();
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (Exception ex) {
            throw new S3Exception("Failed to Upload Files", ex);
        }
        return new S3UploadResult(key, objectUrl);
    }

    public void delete(List<String> keys, String bucket) {

        if (keys == null || keys.stream().allMatch(Objects::isNull)) {
            return;
        }

        List<ObjectIdentifier> objectsToDelete = keys.stream().filter(key -> key != null && !key.isEmpty()).map(
                key -> ObjectIdentifier.builder().key(key).build()).toList();

        try {
            s3Client.deleteObjects(builder -> builder.bucket(bucket).delete(d -> d.objects(objectsToDelete)
                                                                                  .quiet(false)));// set true to only response error, false response list for success at delete
        } catch (Exception ex) {
            throw new S3Exception("Failed to Delete Files", ex);
        }
    }
}
