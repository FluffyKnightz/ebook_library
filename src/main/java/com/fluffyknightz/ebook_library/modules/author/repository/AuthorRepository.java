package com.fluffyknightz.ebook_library.modules.author.repository;

import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends MongoRepository<Author, String> {
    Optional<Author> findByName(String name);

    List<Author> findAllByNameIn(Collection<String> names);
}
