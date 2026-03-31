package com.example.demo2.security.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.demo2.dto.response.ErrorResponse;
import com.example.demo2.dto.response.FieldErrorDetail;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException ex
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        List<FieldErrorDetail> errors = new ArrayList<>();
        FieldErrorDetail e = new FieldErrorDetail("JwtAccess", "Access denied.");
        errors.add(e);
        
        ErrorResponse error = new ErrorResponse(
                403,
                "FORBIDDEN",
                errors
        );

        response.getWriter()
                .write(new ObjectMapper().writeValueAsString(error));
    }
}
