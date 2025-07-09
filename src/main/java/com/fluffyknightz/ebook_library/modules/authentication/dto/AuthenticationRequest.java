package com.fluffyknightz.ebook_library.modules.authentication.dto;

import lombok.Builder;

@Builder
public record AuthenticationRequest(String username, String password, boolean rememberMe) {


}
