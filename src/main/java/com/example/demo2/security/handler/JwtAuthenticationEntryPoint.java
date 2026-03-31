package com.example.demo2.security.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.demo2.dto.response.ErrorResponse;
import com.example.demo2.dto.response.FieldErrorDetail;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        List<FieldErrorDetail> errors = new ArrayList<>();
        FieldErrorDetail e = new FieldErrorDetail("JwtAuthentication", "Invalid or expired token.");
        errors.add(e);

        ErrorResponse error = new ErrorResponse(
                401,
                "UNAUTHORIZED",
                errors
        );

        response.getWriter()
                .write(new ObjectMapper().writeValueAsString(error));
    }
}
