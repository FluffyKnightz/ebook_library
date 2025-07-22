package com.fluffyknightz.ebook_library.modules.book.service_implement;

import com.fluffyknightz.ebook_library.config.s3_object.S3ObjectUsage;
import com.fluffyknightz.ebook_library.config.s3_object.dto.S3UploadResult;
import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import com.fluffyknightz.ebook_library.modules.author.repository.AuthorRepository;
import com.fluffyknightz.ebook_library.modules.book.dto.BookCreateDTO;
import com.fluffyknightz.ebook_library.modules.book.dto.BookUpdateDTO;
import com.fluffyknightz.ebook_library.modules.book.entity.Book;
import com.fluffyknightz.ebook_library.modules.book.repository.BookRepository;
import com.fluffyknightz.ebook_library.modules.book.service.BookService;
import com.fluffyknightz.ebook_library.modules.file.entity.File;
import com.fluffyknightz.ebook_library.modules.file.service.FileService;
import com.fluffyknightz.ebook_library.modules.genre.entity.Genre;
import com.fluffyknightz.ebook_library.modules.genre.repository.GenreRepository;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final S3Client s3Client;
    private final FileService fileService;
    private final S3ObjectUsage s3ObjectUsage;

    @Value("${aws.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.s3.region}")
    private String region;

    @Override
    public void save(BookCreateDTO bookCreateDTO, User user) throws IOException {

        S3UploadResult s3UploadResult;
        try {
            s3UploadResult = s3ObjectUsage.create(bookCreateDTO.coverImage(), bucket, region);
        } catch (IOException ex) {
            throw new IOException("Error saving Cover Image: " + ex.getMessage());
        }

        try {
            List<Author> authors = authorRepository.findAllByNameIn(bookCreateDTO.authors());
            List<Genre> genres = genreRepository.findAllByNameIn(bookCreateDTO.genres());
            List<File> files = fileService.save(bookCreateDTO.files());

            Book book = new Book(bookCreateDTO.title(), bookCreateDTO.publishedDate(), bookCreateDTO.synopsis(),
                                 bookCreateDTO.coverImage()
                                              .getOriginalFilename(), bookCreateDTO.coverImage()
                                                                                   .getContentType(),
                                 s3UploadResult.s3Key(), s3UploadResult.objectUrl(), 0, files, genres, authors,
                                 LocalDate.now(), user, LocalDate.now(), user, false);

            bookRepository.insert(book);

        } catch (DataIntegrityViolationException ex) {
            //delete if fail
            s3ObjectUsage.delete(Collections.singletonList(s3UploadResult.s3Key()), bucket);
            throw new DuplicateKeyException(
                    "Book with title: " + bookCreateDTO.title() + "with published date: " + bookCreateDTO.publishedDate() + "  already exists");

        } catch (Exception ex) {
            //delete if fail
            s3ObjectUsage.delete(Collections.singletonList(s3UploadResult.s3Key()), bucket);
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
    public void update(BookUpdateDTO bookUpdateDTO, User user) throws IOException {


        Book book = findById(bookUpdateDTO.id());
        S3UploadResult s3UploadResult;

        if (!bookUpdateDTO.coverImage()
                          .isEmpty()) {

            try {
                // create new image 1st
                s3UploadResult = s3ObjectUsage.create(bookUpdateDTO.coverImage(), bucket, region);
            } catch (IOException ex) {
                throw new IOException("Error saving Cover Image: " + ex.getMessage());
            }

            try {
                // delete previous image
                s3ObjectUsage.delete(Collections.singletonList(book.getS3Key()), bucket);
            } catch (Exception e) {
                log.warn("Tried to delete S3 objects but failed (likely not uploaded): {}", e.getMessage());
            }

            book.setCoverImageName(bookUpdateDTO.coverImage()
                                                .getOriginalFilename());
            book.setCoverImageType(bookUpdateDTO.coverImage()
                                                .getContentType());
            book.setSynopsis(s3UploadResult.s3Key());
            book.setObjectURL(s3UploadResult.objectUrl());
        }

        try {

            List<Author> authors = authorRepository.findAllByNameIn(bookUpdateDTO.authors());
            List<Genre> genres = genreRepository.findAllByNameIn(bookUpdateDTO.genres());
            List<File> files = fileService.save(bookUpdateDTO.files());

            book.setTitle(bookUpdateDTO.title());
            book.setPublishedDate(bookUpdateDTO.publishedDate());
            book.setSynopsis(bookUpdateDTO.synopsis());
            book.setAuthors(authors);
            book.setGenres(genres);
            book.setFiles(files);
            book.setUpdatedDate(LocalDate.now());
            book.setUpdatedUser(user);
            bookRepository.save(book);

        } catch (DataIntegrityViolationException ex) {
            //delete if fail
            s3ObjectUsage.delete(Collections.singletonList(s3UploadResult), bucket);
            throw new DuplicateKeyException(
                    "Book with title: " + bookUpdateDTO.title() + "with published date: " + bookUpdateDTO.publishedDate() + "  already exists");

        } catch (Exception ex) {
            //delete if fail
            s3ObjectUsage.delete(Collections.singletonList(key), bucket);
            throw new IOException("Error updating book: " + ex.getMessage());
        }
    }
}