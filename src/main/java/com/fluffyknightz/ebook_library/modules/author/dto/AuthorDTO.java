package com.fluffyknightz.ebook_library.modules.author.dto;

import java.time.LocalDate;

public record AuthorDTO(String id, String name, String nationality, LocalDate birthDate, String description) {}
