package com.fluffyknightz.ebook_library.modules.genre.service;

import com.fluffyknightz.ebook_library.modules.genre.dto.GenreDTO;
import com.fluffyknightz.ebook_library.modules.genre.entity.Genre;

import java.util.List;

public interface GenreService {
    Genre save(GenreDTO genreDTO);

    List<Genre> findAll();

    Genre findById(String id);

    void delete(String id);

    Genre update(GenreDTO genreDTO);
}
