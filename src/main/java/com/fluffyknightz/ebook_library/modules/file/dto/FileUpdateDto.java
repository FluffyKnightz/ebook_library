package com.fluffyknightz.ebook_library.modules.file.dto;

import org.springframework.web.multipart.MultipartFile;

public record FileUpdateDto(String id, MultipartFile file) {}
