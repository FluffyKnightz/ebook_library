package com.fluffyknightz.ebook_library.modules.author.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record AuthorUpdateDTO(@NotBlank String id,
                              @NotBlank(message = "{validation.name.required}") String name,
                              @NotBlank(message = "{validation.nationality.required}") String nationality,
                              @NotNull(message = "{validation.birthDate.required}") LocalDate birthedDate,
                              @NotBlank(message = "{validation.description.required}") String description,
                              MultipartFile image,
                              ImageStatus imageStatus) {}
