package com.fluffyknightz.ebook_library.modules.genre.config;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UnwindOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class GenreViewCreate {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void createGenreView() {

        // Drop view if exists (optional)
        try {
            mongoTemplate.getDb()
                         .getCollection("genre_view")
                         .drop();
        } catch (Exception ignored) {

        }

        mongoTemplate.getDb()
                     .createView("genre_view",    // view name
                                 "genre",         // source collection
                                 Arrays.asList(Aggregates.addFields(new Field<>("createdUserId", "$created_user.$id"),
                                                                    new Field<>("updatedUserId", "$updated_user.$id")),

                                               Aggregates.lookup("user", "createdUserId", "_id", "created_user_info"),
                                               Aggregates.unwind("$created_user_info",
                                                                 new UnwindOptions().preserveNullAndEmptyArrays(true)),

                                               Aggregates.lookup("user", "updatedUserId", "_id", "updated_user_info"),
                                               Aggregates.unwind("$updated_user_info",
                                                                 new UnwindOptions().preserveNullAndEmptyArrays(true)),


                                               Aggregates.project(Projections.fields(
                                                       Projections.include("id", "name", "created_date", "updated_date",
                                                                           "is_deleted"),

                                                       Projections.computed("created_user_id",
                                                                            "$created_user_info._id"),

                                                       Projections.computed("created_user_username",
                                                                            "$created_user_info.username"),

                                                       Projections.computed("updated_user_id",
                                                                            "$updated_user_info._id"),

                                                       Projections.computed("updated_user_username",
                                                                            "$updated_user_info.username")

                                               ))));
    }
}
