package com.fluffyknightz.ebook_library.modules.file.api;

import com.fluffyknightz.ebook_library.modules.file.dto.FileDTO;
import com.fluffyknightz.ebook_library.modules.file.entity.File;
import com.fluffyknightz.ebook_library.modules.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileAPI {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<File> addFile(@RequestBody FileDTO fileDTO) {
        return new ResponseEntity<>(fileService.save(fileDTO), HttpStatus.CREATED);
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
    public ResponseEntity<File> updateFile(@RequestBody FileDTO fileDTO) {
        return ResponseEntity.ok(fileService.update(fileDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable String id) {
        fileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
