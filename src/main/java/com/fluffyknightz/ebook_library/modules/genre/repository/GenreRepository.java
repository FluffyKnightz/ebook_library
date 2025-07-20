package com.fluffyknightz.ebook_library.modules.genre.repository;

import com.fluffyknightz.ebook_library.modules.genre.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface GenreRepository extends MongoRepository<Genre, String> {

    @Query("{$and: [ " +
            "{is_deleted :  false } , " +
            "{$or: [ " +
            "{ name:  { $regex: ?0, $options: 'i' } }, " +
            "{ $expr: { $regexMatch: { input: { $dateToString: " +
            "{ format: '%Y-%m-%d', date: '$created_date' } }, regex: ?0, options: 'i' } }, }, " +
            "{ $expr: { $regexMatch: { input: { $dateToString: " +
            "{ format: '%Y-%m-%d', date: '$updated_date' } }, regex: ?0, options: 'i' } }, } " +
            "] }] }")
        // date‐as‐string match:
    Page<Genre> findAllForTable(String searchTerm, Pageable pageable);

    List<Genre> findAllByNameIn(Collection<String> names);
}
