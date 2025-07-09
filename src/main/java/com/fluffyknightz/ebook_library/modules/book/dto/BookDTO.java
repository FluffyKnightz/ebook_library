package com.fluffyknightz.ebook_library.modules.book.dto;

import java.time.LocalDate;
import java.util.List;

public record BookDTO(
        String id,
        String title,
        LocalDate publishedDate,
        String synopsis,
        List<String> files,
        List<String> genres,
        List<String> authors
) {
}