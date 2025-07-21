package com.fluffyknightz.ebook_library.modules.author.entity;

import com.fluffyknightz.ebook_library.modules.user.entity.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Document(collection = "author")
@CompoundIndex(def = "{'name': 1, 'nationality': 1, 'birthed_date': 1}", name = "author_uqx", unique = true)
public class Author implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private String nationality;

    @Field(name = "image_name")
    private String imageName;

    @Field(name = "image_type")
    private String imageType;

    @Field(name = "s3_key")
    @Indexed(unique = true)
    private String s3Key;

    @Field(name = "object_url")
    @Indexed(unique = true)
    private String objectURL;

    @Field(name = "birthed_date")
    private LocalDate birthedDate;

    @Field(name = "created_date")
    private LocalDate createdDate;

    @DBRef
    @Field(name = "created_user")
    private User createdUser;

    @Field(name = "updated_date")
    private LocalDate updatedDate;

    @DBRef
    @Field(name = "updated_user")
    private User updatedUser;

    @Field(name = "is_deleted")
    private boolean isDeleted;

    public Author(String name, String description, String nationality, LocalDate birthedDate, LocalDate createdDate,
                  User createdUser, LocalDate updatedDate, User updatedUser, boolean isDeleted) {
        this.name = name;
        this.description = description;
        this.nationality = nationality;
        this.birthedDate = birthedDate;
        this.createdDate = createdDate;
        this.createdUser = createdUser;
        this.updatedDate = updatedDate;
        this.updatedUser = updatedUser;
        this.isDeleted = isDeleted;
    }
}
