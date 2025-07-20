package com.fluffyknightz.ebook_library.modules.genre.service_implement;

import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.genre.dto.GenreDTO;
import com.fluffyknightz.ebook_library.modules.genre.entity.Genre;
import com.fluffyknightz.ebook_library.modules.genre.repository.GenreRepository;
import com.fluffyknightz.ebook_library.modules.genre.service.GenreService;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public Genre save(User user, GenreDTO genreDTO) {
        Genre genre = new Genre(genreDTO.name(), LocalDate.now(), user, LocalDate.now(), user, false);
        try {
            return genreRepository.save(genre);
        } catch (DataIntegrityViolationException d) {
            throw new DuplicateKeyException("Genre with name: " + genreDTO.name() + " already exists");
        }
    }

    @Override
    public Page<Genre> findAll(String search, Pageable pageable) {
        return genreRepository.findAllForTable(search, pageable);
    }

    @Override
    public Genre findById(String id) {
        return genreRepository.findById(id)
                              .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));
    }

    @Override
    public void delete(String id) {
        Genre genre = findById(id);
        genre.setDeleted(true);
        genreRepository.save(genre);
    }

    @Override
    public Genre update(User user, GenreDTO genreDTO) {
        Genre genre = findById(genreDTO.id());
        genre.setName(genreDTO.name());
        genre.setUpdatedDate(LocalDate.now());
        genre.setUpdatedUser(user);
        return genreRepository.save(genre);
    }
}
