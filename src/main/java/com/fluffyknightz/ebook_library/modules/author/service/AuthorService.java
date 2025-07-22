package com.fluffyknightz.ebook_library.modules.author.service;

import com.fluffyknightz.ebook_library.modules.author.dto.AuthorCreateDTO;
import com.fluffyknightz.ebook_library.modules.author.dto.AuthorUpdateDTO;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import com.fluffyknightz.ebook_library.modules.author.view.AuthorView;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface AuthorService {
    void save(User user, AuthorCreateDTO authorCreateDTO) throws IOException;

    Page<AuthorView> findForTable(String search, Pageable pageable);

    Author findById(String id);

    void delete(String id);

    void update(User user, AuthorUpdateDTO authorUpdateDTO) throws IOException;
}
