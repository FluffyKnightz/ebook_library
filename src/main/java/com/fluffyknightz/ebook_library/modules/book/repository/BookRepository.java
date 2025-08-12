package com.fluffyknightz.ebook_library.modules.book.repository;

import com.fluffyknightz.ebook_library.modules.book.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {}
