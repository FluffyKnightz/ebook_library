package com.fluffyknightz.ebook_library.modules.author.api;

import com.fluffyknightz.ebook_library.modules.author.dto.AuthorDTO;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import com.fluffyknightz.ebook_library.modules.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorAPI {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody AuthorDTO authorDTO) throws IOException {
        Author author = authorService.save(authorDTO);
        if (author != null) {
            return ResponseEntity.ok(author);
        }
        throw new IOException("Failed to create new author.");
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable String id) {
        Author author = authorService.findById(id);
        return ResponseEntity.ok(author);
    }

    @PutMapping
    public ResponseEntity<Author> updateAuthor(@RequestBody AuthorDTO authorDTO) {
        Author author = authorService.update(authorDTO);
        return ResponseEntity.ok(author);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable String id) {
        authorService.delete(id);
        return ResponseEntity.noContent()
                             .build();
    }
}
