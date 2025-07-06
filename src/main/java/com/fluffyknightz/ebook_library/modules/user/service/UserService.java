package com.fluffyknightz.ebook_library.modules.user.service;

import com.fluffyknightz.ebook_library.modules.user.dto.UserDTO;
import com.fluffyknightz.ebook_library.modules.user.entity.User;

import java.util.List;

public interface UserService {
    User save(UserDTO userDTO);

    List<User> findAll();

    User findById(String id);

    void delete(String id);

    User update(UserDTO userDTO);
}
