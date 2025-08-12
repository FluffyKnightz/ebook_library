package com.fluffyknightz.ebook_library.modules.file.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record FileCreateDTO(String bookId, List<MultipartFile> files) {}