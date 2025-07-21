package com.fluffyknightz.ebook_library.modules.genre.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Document(collection = "genre_view")
public class GenreView {

    private String id;
    private String name;

    @Field("is_deleted")
    private boolean isDeleted;

    @Field("created_user_id")
    private String createdUserId;

    @Field("created_user_username")
    private String createdUserUsername;

    @Field("created_date")
    private LocalDate createdDate;

    @Field("updated_user_id")
    private String updatedUserId;

    @Field("updated_user_username")
    private String updatedUserUsername;

    @Field("updated_date")
    private LocalDate updatedDate;

}
