package com.fluffyknightz.ebook_library.modules.author.repository;

import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends MongoRepository<Author, String> {
}
