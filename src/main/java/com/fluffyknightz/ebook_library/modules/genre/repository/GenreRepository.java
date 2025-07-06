package com.fluffyknightz.ebook_library.modules.genre.repository;

import com.fluffyknightz.ebook_library.modules.genre.entity.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends MongoRepository<Genre, String> {
}
