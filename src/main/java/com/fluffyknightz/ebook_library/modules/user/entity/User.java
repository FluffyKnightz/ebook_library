package com.fluffyknightz.ebook_library.modules.user.entity;

import com.fluffyknightz.ebook_library.modules.role.Role;
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
@Document(collection = "user")
public class User implements Serializable {
    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;

    private Role role;

    @Field(name = "is_deleted")
    private boolean isDeleted;

    public User(String username, String email, String password, Role role, boolean isDeleted) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isDeleted = isDeleted;
    }
}
