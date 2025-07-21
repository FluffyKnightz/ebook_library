package com.fluffyknightz.ebook_library.modules.file.api;

import com.fluffyknightz.ebook_library.modules.file.dto.FileDTO;
import com.fluffyknightz.ebook_library.modules.file.dto.FileUpdateDto;
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
    public ResponseEntity<Void> create(FileDTO fileDTO) throws IOException {
        fileService.save(fileDTO.files());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<File>> getForTable() {
        List<File> files = fileService.findAll();
        if (files.isEmpty()) {
            return ResponseEntity.noContent()
                                 .build();
        }
        return new ResponseEntity<>(files, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<File> getById(@PathVariable String id) {
        return ResponseEntity.ok(fileService.findById(id));
    }

    @PutMapping
    public ResponseEntity<Void> update(FileUpdateDto fileUpdateDto) throws IOException {
        fileService.update(fileUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws IOException {
        fileService.delete(id);
        return ResponseEntity.ok()
                             .build();
    }
}
