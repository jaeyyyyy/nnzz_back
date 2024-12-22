package com.nnzz.nnzz.exception;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestURIFilter extends OncePerRequestFilter {
    public static final String ORIGINAL_URI_ATTRIBUTE = "original_uri";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        request.setAttribute(ORIGINAL_URI_ATTRIBUTE, request.getRequestURI());
        filterChain.doFilter(request, response);
    }
}
