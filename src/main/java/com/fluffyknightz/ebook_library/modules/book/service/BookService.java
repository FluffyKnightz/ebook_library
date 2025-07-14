package com.fluffyknightz.ebook_library.modules.book.service;

import com.fluffyknightz.ebook_library.modules.book.dto.BookDTO;
import com.fluffyknightz.ebook_library.modules.book.entity.Book;
import com.fluffyknightz.ebook_library.modules.user.entity.User;

import java.io.IOException;
import java.util.List;

public interface BookService {
    Book save(BookDTO bookDTO, User user) throws IOException;

    List<Book> findAll();

    Book findById(String id);

    void delete(String id);

    Book update(BookDTO bookDTO, User user) throws IOException;
}
