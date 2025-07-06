package com.fluffyknightz.ebook_library.modules.user.repository;

import com.fluffyknightz.ebook_library.modules.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}