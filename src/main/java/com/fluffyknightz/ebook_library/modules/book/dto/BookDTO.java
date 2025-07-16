package com.fluffyknightz.ebook_library.modules.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public record BookDTO(String id,
                      @NotBlank String title,
                      @NotNull LocalDate publishedDate,
                      @NotBlank String synopsis,
                      @NotNull MultipartFile coverImage,
                      @NotNull(message = "Files are required") @NotEmpty(message = "At least one file must be uploaded") List<MultipartFile> files,
                      @NotNull(message = "Files are required") @NotEmpty(message = "At least one file must be uploaded") List<String> genres,
                      @NotNull(message = "Files are required") @NotEmpty(message = "At least one file must be uploaded") List<String> authors) {}