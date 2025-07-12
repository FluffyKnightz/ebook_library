package com.fluffyknightz.ebook_library.modules.genre.repository;

import com.fluffyknightz.ebook_library.modules.genre.entity.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface GenreRepository extends MongoRepository<Genre, String> {

    List<Genre> findAllByNameIn(Collection<String> names);
}
