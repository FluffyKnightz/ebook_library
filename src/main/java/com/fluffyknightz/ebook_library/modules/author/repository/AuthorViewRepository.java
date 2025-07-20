package com.fluffyknightz.ebook_library.modules.author.repository;

import com.fluffyknightz.ebook_library.modules.author.view.AuthorView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorViewRepository extends MongoRepository<AuthorView, String> {

    @Query("{$and: [ " + "{is_deleted :  false } , " +
            "{$or: [ " + "{ name:  { $regex: ?0, $options: 'i' } }, " +
            "{ description: { $regex: ?0, $options: 'i' } }, " +
            "{ nationality: { $regex: ?0, $options: 'i' } }, " +
            "{ created_user_username: { $regex: ?0, $options: 'i' } }, " +
            "{ updated_user_username: { $regex: ?0, $options: 'i' } }, " +
            "{ $expr: { $regexMatch: { input: { $dateToString: { format: '%Y-%m-%d', date: '$birthed_date' } }, regex: ?0, options: 'i' } }, }, " +
            "{ $expr: { $regexMatch: { input: { $dateToString: { format: '%Y-%m-%d', date: '$created_date' } }, regex: ?0, options: 'i' } }, }, " +
            "{ $expr: { $regexMatch: { input: { $dateToString: { format: '%Y-%m-%d', date: '$updated_date' } }, regex: ?0, options: 'i' } }, } " +
            "] }] }")
        // date‐as‐string match:
    Page<AuthorView> findAllForTable(String searchTerm, Pageable pageable);

}
