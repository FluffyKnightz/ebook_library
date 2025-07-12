package com.fluffyknightz.ebook_library.modules.book.service_implement;

import com.fluffyknightz.ebook_library.config.security.MyUserDetails;
import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import com.fluffyknightz.ebook_library.modules.author.repository.AuthorRepository;
import com.fluffyknightz.ebook_library.modules.book.dto.BookDTO;
import com.fluffyknightz.ebook_library.modules.book.entity.Book;
import com.fluffyknightz.ebook_library.modules.book.repository.BookRepository;
import com.fluffyknightz.ebook_library.modules.book.service.BookService;
import com.fluffyknightz.ebook_library.modules.file.entity.File;
import com.fluffyknightz.ebook_library.modules.file.service.FileService;
import com.fluffyknightz.ebook_library.modules.genre.entity.Genre;
import com.fluffyknightz.ebook_library.modules.genre.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final S3Client s3Client;
    private final FileService fileService;

    @Value("${aws.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.s3.region}")
    private String region;

    private final Authentication authentication = SecurityContextHolder.getContext()
                                                                       .getAuthentication();
    private final MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();


    @Override
    public Book save(BookDTO bookDTO) throws IOException {

        // image save in s3
        String key = UUID.randomUUID() + "_" + bookDTO.coverImage()
                                                      .getOriginalFilename() + "_" + LocalDateTime.now();

        String objectUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;

        try {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                .bucket(bucket)
                                                                .key(key)
                                                                .contentType(bookDTO.coverImage()
                                                                                    .getContentType())
                                                                .bucketKeyEnabled(true)
                                                                .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(bookDTO.coverImage()
                                                                                    .getInputStream(),
                                                                             bookDTO.coverImage()
                                                                                    .getSize()));

        } catch (IOException ex) {
            throw new IOException("Error saving book: " + ex.getMessage());
        }

        try {
            List<Author> authors = bookDTO.authors() != null ? authorRepository.findAllByNameIn(
                    bookDTO.authors()) : Collections.emptyList();
            List<Genre> genres = bookDTO.genres() != null ? genreRepository.findAllByNameIn(
                    bookDTO.genres()) : Collections.emptyList();
            List<File> files = fileService.save(bookDTO.files());

            Book book = new Book(bookDTO.title(), bookDTO.publishedDate(), bookDTO.synopsis(), bookDTO.coverImage()
                                                                                                      .getOriginalFilename(),
                                 bookDTO.coverImage()
                                        .getContentType(), key, objectUrl, files, genres, authors, LocalDate.now(),
                                 myUserDetails.getUser(), LocalDate.now(), myUserDetails.getUser(), false);

            return bookRepository.save(book);
        } catch (DataIntegrityViolationException ex) {
            deleteS3Object(key);
            throw new DuplicateKeyException(
                    "Book with title: " + bookDTO.title() + "with published date: " + bookDTO.publishedDate() + "  already exists");
        } catch (Exception ex) {
            deleteS3Object(key);
            throw new IOException("Error saving book: " + ex.getMessage());
        }
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(String id) {
        return bookRepository.findById(id)
                             .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Override
    public void delete(String id) {
        Book book = findById(id);
        book.setDeleted(true);
        bookRepository.save(book);
    }

    @Override
    public Book update(BookDTO bookDTO) throws IOException {

        String key = UUID.randomUUID() + "_" + bookDTO.coverImage()
                                                      .getOriginalFilename();
        try {

            Book book = findById(bookDTO.id());

            if (!bookDTO.coverImage()
                        .isEmpty()) {

                // delete previous image
                deleteS3Object(book.getS3Key());

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                    .bucket(bucket)
                                                                    .key(key)
                                                                    .contentType(bookDTO.coverImage()
                                                                                        .getContentType())
                                                                    .bucketKeyEnabled(true)
                                                                    .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(bookDTO.coverImage()
                                                                                        .getInputStream(),
                                                                                 bookDTO.coverImage()
                                                                                        .getSize()));

                String objectUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;

                book.setCoverImageName(bookDTO.coverImage().getOriginalFilename());
                book.setCoverImageType(bookDTO.coverImage().getContentType());
                book.setSynopsis(key);
                book.setObjectURL(objectUrl);
            }

            List<Author> authors = bookDTO.authors() != null ? authorRepository.findAllByNameIn(
                    bookDTO.authors()) : Collections.emptyList();
            List<Genre> genres = bookDTO.genres() != null ? genreRepository.findAllByNameIn(
                    bookDTO.genres()) : Collections.emptyList();
            List<File> files = fileService.save(bookDTO.files());

            book.setTitle(bookDTO.title());
            book.setPublishedDate(bookDTO.publishedDate());
            book.setSynopsis(bookDTO.synopsis());
            book.setAuthors(authors);
            book.setGenres(genres);
            book.setFiles(files);
            book.setUpdatedDate(LocalDate.now());
            book.setUpdatedUser(myUserDetails.getUser());

            return bookRepository.save(book);

        } catch (DataIntegrityViolationException ex) {
            deleteS3Object(key);
            throw new DuplicateKeyException(
                    "Book with title: " + bookDTO.title() + "with published date: " + bookDTO.publishedDate() + "  already exists");
        } catch (Exception ex) {
            deleteS3Object(key);
            throw new IOException("Error updating book: " + ex.getMessage());
        }
    }

    public void deleteS3Object(String key) throws IOException {
        try {
            s3Client.deleteObject(builder -> builder.key(key)
                                                    .bucket(bucket));
        } catch (Exception e) {
            log.warn("Tried to delete S3 object {} but failed (likely not uploaded): {}", key, e.getMessage());
        }
    }
}