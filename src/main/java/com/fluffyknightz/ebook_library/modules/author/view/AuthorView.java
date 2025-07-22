package com.fluffyknightz.ebook_library.modules.author.view;

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
@Document(collection = "author_view")
public class AuthorView {

    private String id;
    private String name;
    private String description;
    private String nationality;

    @Field("image_url")
    private String imageURL;

    @Field("birthed_date")
    private LocalDate birthedDate;

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
