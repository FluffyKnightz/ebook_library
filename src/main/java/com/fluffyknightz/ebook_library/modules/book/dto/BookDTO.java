package com.fluffyknightz.ebook_library.modules.book.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public record BookDTO(String id,
                      String title,
                      LocalDate publishedDate,
                      String synopsis,
                      MultipartFile coverImage,
                      List<MultipartFile> files,
                      List<String> genres,
                      List<String> authors) {}