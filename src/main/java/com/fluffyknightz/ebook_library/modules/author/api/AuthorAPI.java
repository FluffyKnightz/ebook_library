package com.fluffyknightz.ebook_library.modules.author.api;

import com.fluffyknightz.ebook_library.config.s3_object.S3ObjectUsage;
import com.fluffyknightz.ebook_library.config.security.MyUserDetails;
import com.fluffyknightz.ebook_library.modules.author.dto.AuthorCreateDTO;
import com.fluffyknightz.ebook_library.modules.author.dto.AuthorUpdateDTO;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;
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
    private final S3ObjectUsage s3ObjectUsage;

    @PostMapping
    public ResponseEntity<Void> create(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                       @Valid AuthorCreateDTO authorCreateDTO) throws IOException {
        authorService.save(myUserDetails.user(), authorCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // url?search=[optional]&page=0(start from 0)&size=10&&sort=propertyName[,asc(or)desc -> optional]
    @GetMapping
    public ResponseEntity<Page<AuthorView>> getForTable(@RequestParam(required = false) String search,
                                                        Pageable pageable) {
        Page<AuthorView> authors = authorService.findForTable(search, pageable);
        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(authors, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getById(@PathVariable String id) {
        Author author = authorService.findById(id);
        return new ResponseEntity<>(author, HttpStatus.FOUND);
    }

    @PutMapping
    public ResponseEntity<Void> update(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                       @Valid AuthorUpdateDTO authorUpdateDTO) throws IOException {
        authorService.update(myUserDetails.user(), authorUpdateDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        authorService.delete(id);
        return ResponseEntity.ok().build();
    }

}
