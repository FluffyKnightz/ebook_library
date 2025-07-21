package com.fluffyknightz.ebook_library.modules.genre.service;

import com.fluffyknightz.ebook_library.modules.genre.dto.GenreDTO;
import com.fluffyknightz.ebook_library.modules.genre.entity.Genre;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenreService {
    Genre save(User user, GenreDTO genreDTO);

    Page<Genre> findForTable(String search, Pageable pageable);

    Genre findById(String id);

    void delete(String id);

    Genre update(User user, GenreDTO genreDTO);
}
