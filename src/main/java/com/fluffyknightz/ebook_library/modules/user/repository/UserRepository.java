package com.fluffyknightz.ebook_library.modules.user.repository;

import com.fluffyknightz.ebook_library.modules.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);
}