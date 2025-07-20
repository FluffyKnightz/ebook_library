package com.fluffyknightz.ebook_library.modules.author.service;

import com.fluffyknightz.ebook_library.modules.author.dto.AuthorDTO;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import com.fluffyknightz.ebook_library.modules.author.view.AuthorView;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface AuthorService {
    Author save(User user, AuthorDTO authorDTO) throws IOException;

    Page<AuthorView> findAll(String search, Pageable pageable);

    Author findById(String id);

    void delete(String id);

    Author update(User user, AuthorDTO authorDTO);
}
