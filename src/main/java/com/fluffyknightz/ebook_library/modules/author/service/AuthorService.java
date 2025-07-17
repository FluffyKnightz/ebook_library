package com.fluffyknightz.ebook_library.modules.author.service;

import com.fluffyknightz.ebook_library.modules.author.dto.AuthorDTO;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    Author save(User user, AuthorDTO authorDTO);

    Page<Author> findAll(Pageable pageable);

    Author findById(String id);

    void delete(String id);

    Author update(User user, AuthorDTO authorDTO);
}
