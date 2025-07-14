package com.fluffyknightz.ebook_library.modules.book.api;

import com.fluffyknightz.ebook_library.config.security.MyUserDetails;
import com.fluffyknightz.ebook_library.modules.book.dto.BookDTO;
import com.fluffyknightz.ebook_library.modules.book.entity.Book;
import com.fluffyknightz.ebook_library.modules.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookAPI {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Book> addBook(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                        BookDTO bookDTO) throws IOException {

        return new ResponseEntity<>(bookService.save(bookDTO, myUserDetails.getUser()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PutMapping
    public ResponseEntity<Book> updateBook(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                           BookDTO bookDTO) throws IOException {
        return ResponseEntity.ok(bookService.update(bookDTO, myUserDetails.getUser()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.delete(id);
        return ResponseEntity.noContent()
                             .build();
    }
}
