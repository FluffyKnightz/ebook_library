package com.fluffyknightz.ebook_library.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ErrorDetails {
    private Date timestamp;
    private int httpStatus;
    private String error;
    private String path;

}
