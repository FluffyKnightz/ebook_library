package com.fluffyknightz.ebook_library.modules.file.service_implement;

import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.file.dto.FileDTO;
import com.fluffyknightz.ebook_library.modules.file.entity.File;
import com.fluffyknightz.ebook_library.modules.file.repository.FileRepository;
import com.fluffyknightz.ebook_library.modules.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final S3Client s3Client;

    @Value("${aws.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.s3.region}")
    private String region;

    @Override
    public List<File> save(List<MultipartFile> files) throws IOException {

        List<String> keys = new ArrayList<>();
        List<File> filesList = new ArrayList<>();
        for (MultipartFile f : files) {
            try {
                String key = UUID.randomUUID() + "_" + f.getOriginalFilename() + "_" + LocalDateTime.now();

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                    .bucket(bucket)
                                                                    .contentType(f.getContentType())
                                                                    .key(key)
                                                                    .serverSideEncryption(ServerSideEncryption.AWS_KMS)
                                                                    .bucketKeyEnabled(true)
                                                                    .build();
                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(f.getInputStream(), f.getSize()));
                keys.add(key);
            } catch (Exception ex) {
                deleteS3Object(keys);
                throw new IOException("Error saving file: " + ex.getMessage());
            }

            File file = new File(f.getOriginalFilename(), f.getContentType(), null, null, false);
            filesList.add(file);
        }

        return fileRepository.insert(filesList);
    }

    @Override
    public List<File> findAll() {
        return fileRepository.findAll();
    }

    @Override
    public File findById(String id) {
        return fileRepository.findById(id)
                             .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));
    }

    @Override
    public void delete(String id) {
        File file = findById(id);
        fileRepository.delete(file);
    }

    @Override
    public File update(FileDTO fileDTO) {
        File file = findById(fileDTO.id());
        file.setFileName(fileDTO.fileName());
        file.setFileType(null);
        return fileRepository.save(file);
    }

    public void deleteS3Object(List<String> keys) throws IOException {
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