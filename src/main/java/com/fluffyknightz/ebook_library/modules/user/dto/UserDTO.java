package com.fluffyknightz.ebook_library.modules.user.dto;

import com.fluffyknightz.ebook_library.modules.role.Role;


public record UserDTO(String id, String username, String password, String email, Role role) {}
