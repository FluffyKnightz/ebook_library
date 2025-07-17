package com.fluffyknightz.ebook_library.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluffyknightz.ebook_library.exception.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Date;

@Configuration
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // 1. Set the HTTP status and JSON content type
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 2. Create your error payload
        ErrorDetails body = new ErrorDetails(new Date(), HttpStatus.UNAUTHORIZED.value(), authException.getMessage(),
                                             request.getRequestURI());

        // 3. Serialize it directly to the response
        mapper.writeValue(response.getWriter(), body);
    }
}
