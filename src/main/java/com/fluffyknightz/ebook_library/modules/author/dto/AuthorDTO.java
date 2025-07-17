package com.fluffyknightz.ebook_library.modules.author.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AuthorDTO(String id,
                        @NotBlank(message = "{validation.name.required}") String name,
                        @NotBlank(message = "{validation.nationality.required}") String nationality,
                        @NotNull(message = "{validation.birthDate.required}") LocalDate birthDate,
                        @NotBlank(message = "{validation.description.required}") String description) {}
