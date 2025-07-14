package com.fluffyknightz.ebook_library.modules.authentication.api;

import com.fluffyknightz.ebook_library.config.security.AuthenticatedData;
import com.fluffyknightz.ebook_library.config.security.JwtService;
import com.fluffyknightz.ebook_library.config.security.MyUserDetails;
import com.fluffyknightz.ebook_library.config.security.MyUserDetailsService;
import com.fluffyknightz.ebook_library.modules.authentication.dto.AuthenticationRequest;
import com.fluffyknightz.ebook_library.modules.authentication.dto.AuthenticationResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationAPI {

    private final JwtService jwtService;
    private final AuthenticationManager manager;
    private final MyUserDetailsService myUserDetailsService;
    private final AuthenticatedData authenticatedData;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest,
                                                        HttpServletResponse response) {
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.username(),
                                                        authenticationRequest.password()));
        SecurityContextHolder.getContext()
                             .setAuthentication(authentication);
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        var jwtToken = jwtService.generateToken(myUserDetails);
        AuthenticationResponse auth = AuthenticationResponse.builder()
                                                            .token(jwtToken)
                                                            .build();

        ResponseCookie cookie = ResponseCookie.from("jwtToken", jwtToken)
                                              .httpOnly(
                                                      true) // true can use it from javascript or false if only want to use from backend
                                              .secure(true) // Set to true in production with HTTPS
                                              .path("/")
                                              .maxAge(authenticationRequest.rememberMe() ? 2592000 : -1) // 10 hours
                                              .sameSite(
                                                      "Strict") // make it 'none' if you different domain, strict for same also localhost is same
                                              .build();

        // Add the cookie to the response
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(auth);
    }
}
