package com.fluffyknightz.ebook_library.modules.author.entity;

import com.fluffyknightz.ebook_library.modules.user.entity.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
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
@CompoundIndex(def = "{'name': 1, 'nationality': 1, 'birth_date': 1}", name = "author_uqx", unique = true)
public class Author implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private String nationality;

    @Field(name = "birth_date")
    private LocalDate birthDate;

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

    public Author(String name, String description, LocalDate createdDate, User createdUser, LocalDate updatedDate,
                  User updatedUser, boolean isDeleted) {
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.createdUser = createdUser;
        this.updatedDate = updatedDate;
        this.updatedUser = updatedUser;
        this.isDeleted = isDeleted;
    }
}
