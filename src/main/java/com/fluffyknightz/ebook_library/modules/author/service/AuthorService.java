package com.fluffyknightz.ebook_library.modules.author.service;

import com.fluffyknightz.ebook_library.modules.author.dto.AuthorDTO;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;

import java.util.List;

public interface AuthorService {
    Author save(AuthorDTO authorDTO);

    List<Author> findAll();

    Author findById(String id);

    void delete(String id);

    Author update(AuthorDTO authorDTO);
}
