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
    public ResponseEntity<Void> create(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                       BookDTO bookDTO) throws IOException {
        bookService.save(bookDTO, myUserDetails.user());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getForTable() {
        List<Book> books = bookService.findAll();
        if (books.isEmpty()) {
            return ResponseEntity.noContent()
                                 .build();
        }
        return new ResponseEntity<>(books, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PutMapping
    public ResponseEntity<Void> update(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                       BookDTO bookDTO) throws IOException {
        bookService.update(bookDTO, myUserDetails.user());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        bookService.delete(id);
        return ResponseEntity.ok()
                             .build();
    }
}
