package com.fluffyknightz.ebook_library.modules.file.service;

import com.fluffyknightz.ebook_library.modules.file.dto.FileDTO;
import com.fluffyknightz.ebook_library.modules.file.entity.File;

import java.util.List;

public interface FileService {
    File save(FileDTO fileDTO);
    List<File> findAll();
    File findById(String id);
    void delete(String id);
    File update(FileDTO fileDTO);
}
