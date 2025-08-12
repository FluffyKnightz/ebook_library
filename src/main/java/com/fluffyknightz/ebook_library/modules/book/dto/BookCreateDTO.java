package com.fluffyknightz.ebook_library.modules.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public record BookCreateDTO(@NotBlank String title,
                            @NotNull LocalDate publishedDate,
                            @NotBlank String synopsis,
                            MultipartFile coverImage,
                            @NotNull @NotEmpty List<MultipartFile> files,
                            @NotNull @NotEmpty List<String> genres,
                            @NotNull @NotEmpty List<String> authors) {}