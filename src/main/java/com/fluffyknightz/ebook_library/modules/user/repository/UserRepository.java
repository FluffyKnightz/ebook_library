package com.fluffyknightz.ebook_library.modules.user.repository;

import com.fluffyknightz.ebook_library.modules.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{$and: [ " + "{is_deleted :  false } , " + "{$or: [ " + "{ username:  { $regex: ?0, $options: 'i' } }, " + "{ email: { $regex: ?0, $options: 'i' } }, " + "{ role: { $regex: ?0, $options: 'i' } }, " + "] }] }")
    Page<User> findForTable(String searchTerm, Pageable pageable);

    Optional<User> findByUsername(String username);
}