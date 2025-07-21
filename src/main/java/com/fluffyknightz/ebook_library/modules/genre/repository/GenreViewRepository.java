package com.fluffyknightz.ebook_library.modules.genre.repository;

import com.fluffyknightz.ebook_library.modules.genre.view.GenreView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface GenreViewRepository extends MongoRepository<GenreView, String> {

    @Query("{$and: [ " + "{is_deleted :  false } , " +
            "{$or: [ " + "{ name:  { $regex: ?0, $options: 'i' } }, " +
            "{ created_user_username: { $regex: ?0, $options: 'i' } }, " +
            "{ updated_user_username: { $regex: ?0, $options: 'i' } }, " +
            // date‐as‐string match:
            "{ $expr: { $regexMatch: { input: { $dateToString: { format: '%Y-%m-%d', date: '$created_date' } }, regex: ?0, options: 'i' } }, }, " +
            "{ $expr: { $regexMatch: { input: { $dateToString: { format: '%Y-%m-%d', date: '$updated_date' } }, regex: ?0, options: 'i' } }, } " +
            "] }] }")
    Page<GenreView> findForTable(String searchTerm, Pageable pageable);


}
