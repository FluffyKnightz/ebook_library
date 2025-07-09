package com.fluffyknightz.ebook_library.modules.file.service_implement;

import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.file.dto.FileDTO;
import com.fluffyknightz.ebook_library.modules.file.entity.File;
import com.fluffyknightz.ebook_library.modules.file.repository.FileRepository;
import com.fluffyknightz.ebook_library.modules.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public File save(FileDTO fileDTO) {
        File file = new File();
        file.setFileName(fileDTO.fileName());
        file.setFileType(null);
        file.setS3Key(null);
        file.setObjectURL(null);
        return fileRepository.insert(file);
    }

    @Override
    public List<File> findAll() {
        return fileRepository.findAll();
    }

    @Override
    public File findById(String id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));
    }

    @Override
    public void delete(String id) {
        File file = findById(id);
        fileRepository.delete(file);
    }

    @Override
    public File update(FileDTO fileDTO) {
        File file = findById(fileDTO.id());
        file.setFileName(fileDTO.fileName());
        file.setFileType(null);
        return fileRepository.save(file);
    }
}