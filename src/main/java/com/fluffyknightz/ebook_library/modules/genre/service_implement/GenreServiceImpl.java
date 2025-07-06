package com.fluffyknightz.ebook_library.modules.genre.service_implement;

import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.genre.dto.GenreDTO;
import com.fluffyknightz.ebook_library.modules.genre.entity.Genre;
import com.fluffyknightz.ebook_library.modules.genre.repository.GenreRepository;
import com.fluffyknightz.ebook_library.modules.genre.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public Genre save(GenreDTO genreDTO) {
        Genre genre = new Genre(genreDTO.name(), LocalDate.now(), null, LocalDate.now(), null, false);
        return genreRepository.save(genre);
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre findById(String id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));
    }

    @Override
    public void delete(String id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));
        genre.setDeleted(true);
        genreRepository.save(genre);
    }

    @Override
    public Genre update(GenreDTO genreDTO) {
        Genre genre = genreRepository.findById(genreDTO.id())
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + genreDTO.id()));
        genre.setName(genreDTO.name());
        genre.setUpdatedDate(LocalDate.now());
        genre.setUpdatedUser(null);
        return genreRepository.save(genre);
    }
}
