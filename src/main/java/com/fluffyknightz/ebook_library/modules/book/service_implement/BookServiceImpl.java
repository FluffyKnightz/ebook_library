package com.fluffyknightz.ebook_library.modules.book.service_implement;

import com.fluffyknightz.ebook_library.config.s3_object.S3ObjectUsage;
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
    private final S3ObjectUsage s3ObjectUsage;

    @Value("${aws.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.s3.region}")
    private String region;

    @Override
    public void save(BookDTO bookDTO, User user) throws IOException {

        // image save in s3
        String key = UUID.randomUUID() + "_" + bookDTO.coverImage()
                                                      .getOriginalFilename() + "_" + LocalDateTime.now();

        try {
            s3ObjectUsage.create(bookDTO.coverImage(), key, bucket);
        } catch (IOException ex) {
            throw new IOException("Error saving Cover Image: " + ex.getMessage());
        }

        //create object url
        String objectUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;


        try {
            List<Author> authors = authorRepository.findAllByNameIn(bookDTO.authors());
            List<Genre> genres = genreRepository.findAllByNameIn(bookDTO.genres());
            List<File> files = fileService.save(bookDTO.files());

            Book book = new Book(bookDTO.title(), bookDTO.publishedDate(), bookDTO.synopsis(), bookDTO.coverImage()
                                                                                                      .getOriginalFilename(),
                                 bookDTO.coverImage()
                                        .getContentType(), key, objectUrl, 0, files, genres, authors, LocalDate.now(),
                                 user, LocalDate.now(), user, false);

            bookRepository.insert(book);

        } catch (DataIntegrityViolationException ex) {
            //delete if fail
            s3ObjectUsage.delete(Collections.singletonList(key), bucket);
            throw new DuplicateKeyException(
                    "Book with title: " + bookDTO.title() + "with published date: " + bookDTO.publishedDate() + "  already exists");

        } catch (Exception ex) {
            //delete if fail
            s3ObjectUsage.delete(Collections.singletonList(key), bucket);
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
    public void update(BookDTO bookDTO, User user) throws IOException {

        String key = UUID.randomUUID() + "_" + bookDTO.coverImage()
                                                      .getOriginalFilename();

        Book book = findById(bookDTO.id());
        try {
            if (!bookDTO.coverImage()
                        .isEmpty()) {

                // delete previous image
                s3ObjectUsage.delete(Collections.singletonList(book.getS3Key()), bucket);

                //create new image
                s3ObjectUsage.create(bookDTO.coverImage(), key, bucket);

                //create object url
                String objectUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;

                book.setCoverImageName(bookDTO.coverImage()
                                              .getOriginalFilename());
                book.setCoverImageType(bookDTO.coverImage()
                                              .getContentType());
                book.setSynopsis(key);
                book.setObjectURL(objectUrl);
            }

            List<Author> authors = authorRepository.findAllByNameIn(bookDTO.authors());
            List<Genre> genres = genreRepository.findAllByNameIn(bookDTO.genres());
            List<File> files = fileService.save(bookDTO.files());

            book.setTitle(bookDTO.title());
            book.setPublishedDate(bookDTO.publishedDate());
            book.setSynopsis(bookDTO.synopsis());
            book.setAuthors(authors);
            book.setGenres(genres);
            book.setFiles(files);
            book.setUpdatedDate(LocalDate.now());
            book.setUpdatedUser(user);
            bookRepository.save(book);

        } catch (DataIntegrityViolationException ex) {
            //delete if fail
            s3ObjectUsage.delete(Collections.singletonList(key), bucket);
            throw new DuplicateKeyException(
                    "Book with title: " + bookDTO.title() + "with published date: " + bookDTO.publishedDate() + "  already exists");

        } catch (Exception ex) {
            //delete if fail
            s3ObjectUsage.delete(Collections.singletonList(key), bucket);
            throw new IOException("Error updating book: " + ex.getMessage());
        }
    }
}