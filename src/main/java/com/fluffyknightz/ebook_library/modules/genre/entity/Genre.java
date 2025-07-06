package com.fluffyknightz.ebook_library.modules.genre.entity;

import com.fluffyknightz.ebook_library.modules.user.entity.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Document(collection = "genre")
public class Genre {

    @Id
    private String id;

    private String name;

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

    public Genre(String name, LocalDate createdDate, User createdUser, LocalDate updatedDate, User updatedUser,
                 boolean isDeleted) {
        this.name = name;
        this.createdDate = createdDate;
        this.createdUser = createdUser;
        this.updatedDate = updatedDate;
        this.updatedUser = updatedUser;
        this.isDeleted = isDeleted;
    }
}
