package com.fluffyknightz.ebook_library.modules.author.service_implement;

import com.fluffyknightz.ebook_library.config.s3_object.S3ObjectUsage;
import com.fluffyknightz.ebook_library.config.s3_object.dto.S3UploadResult;
import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.author.dto.AuthorCreateDTO;
import com.fluffyknightz.ebook_library.modules.author.dto.AuthorUpdateDTO;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import com.fluffyknightz.ebook_library.modules.author.repository.AuthorRepository;
import com.fluffyknightz.ebook_library.modules.author.repository.AuthorViewRepository;
import com.fluffyknightz.ebook_library.modules.author.service.AuthorService;
import com.fluffyknightz.ebook_library.modules.author.view.AuthorView;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorViewRepository authorViewRepository;
    private final S3ObjectUsage s3ObjectUsage;

    @Value("${spring.cloud.aws.s3.region}")
    private String region;

    @Value("${aws.bucket}")
    private String bucket;

    public void save(User user, AuthorCreateDTO authorCreateDTO) throws IOException {

        S3UploadResult s3UploadResult = s3ObjectUsage.create(authorCreateDTO.image(), bucket, region);

        //to check db commit is success;
        boolean dbOk = false;

        try {
            Author author = new Author(authorCreateDTO.name(), authorCreateDTO.description(),
                                       authorCreateDTO.nationality(), authorCreateDTO.image()
                                                                                     .getOriginalFilename(),
                                       authorCreateDTO.image()
                                                      .getContentType(), s3UploadResult.s3Key(),
                                       s3UploadResult.objectUrl(), authorCreateDTO.birthedDate(), LocalDate.now(), user,
                                       LocalDate.now(), user, false);
            authorRepository.insert(author);
            dbOk = true;

        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateKeyException(
                    String.format("An author with name '%s', nationality '%s', and birth date '%s' already exists",
                                  authorCreateDTO.name(), authorCreateDTO.nationality(),
                                  authorCreateDTO.birthedDate()));

        } finally {
            if (!dbOk) {
                s3ObjectUsage.delete(Collections.singletonList(s3UploadResult.s3Key()), bucket);
            }
        }
    }


    @Override
    public Page<AuthorView> findForTable(String search, Pageable pageable) {
        return authorViewRepository.findForTable(search, pageable);
    }

    @Override
    public Author findById(String id) {
        return authorRepository.findById(id)
                               .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
    }

    @Override
    public void delete(String id) {
        Author author = findById(id);
        author.setDeleted(true);
        authorRepository.save(author);
    }

    @Override
    public void update(User user, AuthorUpdateDTO authorUpdateDTO) throws IOException {

        Author author = findById(authorUpdateDTO.id());

        S3UploadResult s3UploadResult = null;
        if (authorUpdateDTO.image() != null && !authorUpdateDTO.image()
                                                               .isEmpty()) {
            s3UploadResult = s3ObjectUsage.create(authorUpdateDTO.image(), bucket, region);
            author.setImageName(authorUpdateDTO.image()
                                               .getOriginalFilename());
            author.setImageType(authorUpdateDTO.image()
                                               .getContentType());
            author.setS3Key(s3UploadResult.s3Key());
            author.setObjectURL(s3UploadResult.objectUrl());
        }

        //to check db commit is success;
        boolean dbOk = false;

        try {
            author.setName(authorUpdateDTO.name());
            author.setDescription(authorUpdateDTO.description());
            author.setNationality(authorUpdateDTO.nationality());
            author.setBirthedDate(authorUpdateDTO.birthedDate());
            author.setUpdatedDate(LocalDate.now());
            author.setUpdatedUser(user);
            authorRepository.save(author);
            dbOk = true;

        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateKeyException(
                    String.format("An author with name '%s', nationality '%s', and birth date '%s' already exists",
                                  authorUpdateDTO.name(), authorUpdateDTO.nationality(),
                                  authorUpdateDTO.birthedDate()));

        } finally {
            if (!dbOk && s3UploadResult != null) {
                s3ObjectUsage.delete(Collections.singletonList(s3UploadResult.s3Key()), bucket);
            }
        }
    }
}
