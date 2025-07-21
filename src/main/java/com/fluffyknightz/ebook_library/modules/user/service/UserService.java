package com.fluffyknightz.ebook_library.modules.user.service;

import com.fluffyknightz.ebook_library.modules.user.dto.UserDTO;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void save(UserDTO userDTO);

    Page<User> findForTable(String search, Pageable pageable);

    User findById(String id);

    void delete(String id);

    void update(UserDTO userDTO);
}
