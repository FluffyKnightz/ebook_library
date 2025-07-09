package com.fluffyknightz.ebook_library.modules.file.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Document(collection = "file")
public class File implements Serializable {

    @Id
    private String id;

    @Field(name = "file_name")
    private String fileName;

    @Field(name = "file_type")
    private String fileType;

    @Field(name = "s3_key")
    private String s3Key;

    @Field(name = "object_url")
    private String objectURL;
}
