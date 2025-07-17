package com.fluffyknightz.ebook_library.modules.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthenticationRequest(@NotBlank(message = "{validation.username.required}") String username,
                                    @NotBlank(message = "{validation.password.required}") String password,
                                    boolean rememberMe) {


}
