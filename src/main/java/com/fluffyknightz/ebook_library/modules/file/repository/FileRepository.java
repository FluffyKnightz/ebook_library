package com.fluffyknightz.ebook_library.modules.file.repository;

import com.fluffyknightz.ebook_library.modules.file.entity.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends MongoRepository<File, String> {


}
