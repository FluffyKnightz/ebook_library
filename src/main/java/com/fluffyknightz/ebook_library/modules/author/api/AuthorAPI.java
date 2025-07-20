package com.fluffyknightz.ebook_library.modules.author.api;

import com.fluffyknightz.ebook_library.config.security.MyUserDetails;
import com.fluffyknightz.ebook_library.modules.author.dto.AuthorDTO;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import com.fluffyknightz.ebook_library.modules.author.repository.AuthorViewRepository;
import com.fluffyknightz.ebook_library.modules.author.service.AuthorService;
import com.fluffyknightz.ebook_library.modules.author.view.AuthorView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorAPI {

    private final AuthorService authorService;
    private final AuthorViewRepository authorViewRepository;

    @PostMapping
    public ResponseEntity<Void> createAuthor(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                             @RequestBody @Valid AuthorDTO authorDTO) throws IOException {
        authorService.save(myUserDetails.user(), authorDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // url?page=0&size=10&&sort=propertyName[,asc(or)desc -> optional]
    @GetMapping
    public ResponseEntity<Page<AuthorView>> getAuthorsForTable(@RequestParam(required = false) String search,
                                                               Pageable pageable) {
        Page<AuthorView> authors = authorService.findAll(search, pageable);
        if (authors.isEmpty()) {
            return ResponseEntity.noContent()
                                 .build();
        }
        return new ResponseEntity<>(authors, HttpStatus.FOUND);


    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable String id) {
        Author author = authorService.findById(id);
        return new ResponseEntity<>(author, HttpStatus.FOUND);
    }

    @PutMapping
    public ResponseEntity<Void> updateAuthor(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                             @RequestBody AuthorDTO authorDTO) {
        authorService.update(myUserDetails.user(), authorDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable String id) {
        authorService.delete(id);
        return ResponseEntity.noContent()
                             .build();
    }
}
