package com.fluffyknightz.ebook_library.modules.file.service_implement;

import com.fluffyknightz.ebook_library.config.s3_object.S3ObjectUsage;
import com.fluffyknightz.ebook_library.config.s3_object.dto.S3UploadResult;
import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.file.dto.FileUpdateDto;
import com.fluffyknightz.ebook_library.modules.file.entity.File;
import com.fluffyknightz.ebook_library.modules.file.repository.FileRepository;
import com.fluffyknightz.ebook_library.modules.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final S3ObjectUsage s3ObjectUsage;

    @Value("${spring.cloud.aws.s3.region}")
    private String region;

    @Value("${aws.bucket}")
    private String bucket;

    @Override
    public List<File> save(List<MultipartFile> files) throws IOException {

        List<String> keys = new ArrayList<>();
        List<File> filesList = new ArrayList<>();
        for (MultipartFile f : files) {

            S3UploadResult s3UploadResult;
            try {
                s3UploadResult = s3ObjectUsage.create(f, bucket, region);
            } catch (Exception ex) {
                s3ObjectUsage.delete(keys, bucket);
                throw new IOException("Error saving files: " + ex.getMessage());
            }

            keys.add(s3UploadResult.s3Key());

            File file = new File(f.getOriginalFilename(), f.getContentType(), s3UploadResult.s3Key(),
                                 s3UploadResult.objectUrl(), false);
            filesList.add(file);
        }
        try {
            return fileRepository.insert(filesList);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateKeyException("File already exists");
        }
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
    public void delete(String id) throws IOException {
        File file = findById(id);
        s3ObjectUsage.delete(Collections.singletonList(file.getS3Key()), bucket);
        fileRepository.delete(file);
    }

    @Override
    public void update(FileUpdateDto fileUpdateDto) throws IOException {
        File file = findById(fileUpdateDto.id());
        s3ObjectUsage.delete(Collections.singletonList(file.getS3Key()), bucket);
        S3UploadResult s3UploadResult;
        try {
            s3UploadResult = s3ObjectUsage.create(fileUpdateDto.file(), bucket, region);
        } catch (Exception ex) {
            throw new IOException("Error saving files: " + ex.getMessage());
        }


        file = new File(fileUpdateDto.file()
                                     .getOriginalFilename(), fileUpdateDto.file()
                                                                          .getContentType(), s3UploadResult.s3Key(),
                        s3UploadResult.objectUrl(), false);
        try {
            fileRepository.save(file);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateKeyException("File already exists");
        }
    }
}