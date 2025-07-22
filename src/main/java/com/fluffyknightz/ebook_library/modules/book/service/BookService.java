package com.fluffyknightz.ebook_library.modules.book.service;

import com.fluffyknightz.ebook_library.modules.book.dto.BookCreateDTO;
import com.fluffyknightz.ebook_library.modules.book.dto.BookUpdateDTO;
import com.fluffyknightz.ebook_library.modules.book.entity.Book;
import com.fluffyknightz.ebook_library.modules.user.entity.User;

import java.io.IOException;
import java.util.List;

public interface BookService {
    void save(BookCreateDTO bookCreateDTO, User user) throws IOException;

    List<Book> findAll();

    Book findById(String id);

    void delete(String id);

    void update(BookUpdateDTO bookUpdateDTO, User user) throws IOException;
}
