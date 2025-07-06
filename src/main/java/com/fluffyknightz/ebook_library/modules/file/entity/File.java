package com.fluffyknightz.ebook_library.modules.file.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Document(collection = "file")
public class File {

    @Id
    private String id;

    private String file;

    @Field(name = "file_type")
    private String fileType;

    @Field(name = "s3_key")
    private String s3Key;
}
