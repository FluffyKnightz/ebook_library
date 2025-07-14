package com.fluffyknightz.ebook_library.modules.file.api;

import com.fluffyknightz.ebook_library.modules.file.dto.FileDTO;
import com.fluffyknightz.ebook_library.modules.file.entity.File;
import com.fluffyknightz.ebook_library.modules.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileAPI {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<List<File>> addFile(FileDTO fileDTO) throws IOException {
        return new ResponseEntity<>(fileService.save(fileDTO.files()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<File>> getAllFiles() {
        return ResponseEntity.ok(fileService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<File> getFileById(@PathVariable String id) {
        return ResponseEntity.ok(fileService.findById(id));
    }

    @PutMapping
    public ResponseEntity<File> updateFile(FileDTO fileDTO) {
        return ResponseEntity.ok(fileService.update(fileDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable String id) throws IOException {
        fileService.delete(id);
        return ResponseEntity.noContent()
                             .build();
    }
}
