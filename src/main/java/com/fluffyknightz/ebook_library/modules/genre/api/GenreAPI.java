package com.fluffyknightz.ebook_library.modules.genre.api;

import com.fluffyknightz.ebook_library.modules.genre.dto.GenreDTO;
import com.fluffyknightz.ebook_library.modules.genre.entity.Genre;
import com.fluffyknightz.ebook_library.modules.genre.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreAPI {

    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody GenreDTO genreDTO) {
        Genre genre = genreService.save(genreDTO);
        return ResponseEntity.ok(genre);
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        return ResponseEntity.ok(genreService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable String id) {
        Genre genre = genreService.findById(id);
        return ResponseEntity.ok(genre);
    }

    @PutMapping
    public ResponseEntity<Genre> updateGenre(@RequestBody GenreDTO genreDTO) {
        Genre genre = genreService.update(genreDTO);
        return ResponseEntity.ok(genre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable String id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
