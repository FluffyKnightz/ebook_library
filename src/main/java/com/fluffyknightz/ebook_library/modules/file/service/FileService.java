package com.fluffyknightz.ebook_library.modules.file.service;

import com.fluffyknightz.ebook_library.modules.file.dto.FileUpdateDto;
import com.fluffyknightz.ebook_library.modules.file.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<File> save(List<MultipartFile> files) throws IOException;

    List<File> findAll();

    File findById(String id);

    void delete(String id) throws IOException;

    void update(FileUpdateDto fileUpdateDto) throws IOException;
}
