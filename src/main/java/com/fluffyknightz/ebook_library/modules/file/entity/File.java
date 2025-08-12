package com.fluffyknightz.ebook_library.modules.file.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
    @Indexed(unique = true)
    private String s3Key;

    @Field(name = "object_url")
    @Indexed(unique = true)
    private String objectURL;

    @Field(name = "is_deleted")
    private boolean isDeleted;

    public File(String fileName, String fileType, String s3Key, String objectURL, boolean isDeleted) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.s3Key = s3Key;
        this.objectURL = objectURL;
        this.isDeleted = isDeleted;
    }
}
