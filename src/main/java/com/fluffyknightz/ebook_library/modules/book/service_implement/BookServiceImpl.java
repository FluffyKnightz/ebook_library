package com.fluffyknightz.ebook_library.modules.book.service_implement;

import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import com.fluffyknightz.ebook_library.modules.author.repository.AuthorRepository;
import com.fluffyknightz.ebook_library.modules.book.dto.BookDTO;
import com.fluffyknightz.ebook_library.modules.book.entity.Book;
import com.fluffyknightz.ebook_library.modules.book.repository.BookRepository;
import com.fluffyknightz.ebook_library.modules.book.service.BookService;
import com.fluffyknightz.ebook_library.modules.file.entity.File;
import com.fluffyknightz.ebook_library.modules.file.repository.FileRepository;
import com.fluffyknightz.ebook_library.modules.genre.entity.Genre;
import com.fluffyknightz.ebook_library.modules.genre.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final FileRepository fileRepository;

    @Override
    public Book save(BookDTO bookDTO) {
        List<Author> authors = bookDTO.authors() != null ? authorRepository.findAllById(bookDTO.authors()) : Collections.emptyList();
        List<Genre> genres = bookDTO.genres() != null ? genreRepository.findAllById(bookDTO.genres()) : Collections.emptyList();
        List<File> files = bookDTO.files() != null ? fileRepository.findAllById(bookDTO.files()) : Collections.emptyList();

        Book book = new Book();
        book.setTitle(bookDTO.title());
        book.setPublishedDate(bookDTO.publishedDate());
        book.setSynopsis(bookDTO.synopsis());
        book.setAuthors(authors);
        book.setGenres(genres);
        book.setFiles(files);
        book.setCreatedDate(LocalDate.now().toString());
        book.setUpdatedDate(LocalDate.now().toString());
        book.setDeleted(false);
        book.setS3Key(null);
        book.setObjectURL(null);
        book.setCoverImageName(null);
        book.setCoverImageType(null);
        book.setCreatedUser(null);
        book.setUpdatedUser(null);


        return bookRepository.save(book);
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
    public Book update(BookDTO bookDTO) {
        Book book = findById(bookDTO.id());

        List<Author> authors = bookDTO.authors() != null ? authorRepository.findAllById(bookDTO.authors()) : Collections.emptyList();
        List<Genre> genres = bookDTO.genres() != null ? genreRepository.findAllById(bookDTO.genres()) : Collections.emptyList();
        List<File> files = bookDTO.files() != null ? fileRepository.findAllById(bookDTO.files()) : Collections.emptyList();

        book.setTitle(bookDTO.title());
        book.setPublishedDate(bookDTO.publishedDate());
        book.setSynopsis(bookDTO.synopsis());
        book.setAuthors(authors);
        book.setGenres(genres);
        book.setFiles(files);
        book.setUpdatedDate(LocalDate.now().toString());
        book.setUpdatedUser(null);

        return bookRepository.save(book);
    }
}