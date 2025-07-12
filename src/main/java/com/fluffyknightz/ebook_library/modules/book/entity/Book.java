package com.fluffyknightz.ebook_library.modules.book.entity;

import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import com.fluffyknightz.ebook_library.modules.file.entity.File;
import com.fluffyknightz.ebook_library.modules.genre.entity.Genre;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Document(collection = "book")
@CompoundIndex(def = "{'title': 1, 'published_date': 1}", name = "book_uqx", unique = true)
public class Book implements Serializable {

    @Id
    private String id;

    private String title;

    @Field(name = "published_date")
    private LocalDate publishedDate;

    private String synopsis;

    @Field(name = "cover_image_name")
    private String coverImageName;

    @Field(name = "cover_image_type")
    private String coverImageType;

    @Field(name = "s3_key")
    private String s3Key;

    @Field(name = "object_url")
    private String objectURL;

    @DBRef
    private List<File> files;

    @DBRef
    private List<Genre> genres;

    @DBRef
    private List<Author> authors;

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

    public Book(String title, LocalDate publishedDate, String synopsis, String coverImageName, String coverImageType,
                String s3Key, String objectURL, List<File> files, List<Genre> genres, List<Author> authors,
                LocalDate createdDate, User createdUser, LocalDate updatedDate, User updatedUser, boolean isDeleted) {
        this.title = title;
        this.publishedDate = publishedDate;
        this.synopsis = synopsis;
        this.coverImageName = coverImageName;
        this.coverImageType = coverImageType;
        this.s3Key = s3Key;
        this.objectURL = objectURL;
        this.files = files;
        this.genres = genres;
        this.authors = authors;
        this.createdDate = createdDate;
        this.createdUser = createdUser;
        this.updatedDate = updatedDate;
        this.updatedUser = updatedUser;
        this.isDeleted = isDeleted;
    }
}
