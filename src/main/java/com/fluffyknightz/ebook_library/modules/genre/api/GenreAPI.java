package com.fluffyknightz.ebook_library.modules.genre.api;

import com.fluffyknightz.ebook_library.config.security.MyUserDetails;
import com.fluffyknightz.ebook_library.modules.genre.dto.GenreDTO;
import com.fluffyknightz.ebook_library.modules.genre.entity.Genre;
import com.fluffyknightz.ebook_library.modules.genre.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreAPI {

    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<Void> createGenre(@AuthenticationPrincipal MyUserDetails myUserDetails, @RequestBody GenreDTO genreDTO) {
        genreService.save(myUserDetails.user(), genreDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // url?page=0&size=10&&sort=propertyName[,asc(or)desc -> optional]
    @GetMapping
    public ResponseEntity<Page<Genre>> getGenresForTable(@RequestParam(required = false) String search,
                                                         Pageable pageable) {
        Page<Genre> genres = genreService.findAll(search, pageable);
        if (genres.isEmpty()) {
            return ResponseEntity.noContent()
                                 .build();
        }
        return new ResponseEntity<>(genres, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable String id) {
        Genre genre = genreService.findById(id);
        return new ResponseEntity<>(genre, HttpStatus.FOUND);
    }

    @PutMapping
    public ResponseEntity<Genre> updateGenre(@AuthenticationPrincipal MyUserDetails myUserDetails, @RequestBody GenreDTO genreDTO) {
        genreService.update(myUserDetails.user(), genreDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable String id) {
        genreService.delete(id);
        return ResponseEntity.noContent()
                             .build();
    }
}
