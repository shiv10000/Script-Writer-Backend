package org.shivam.script_writer.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class PractiseFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Before request: " + request.getRequestURI());

        try {
            filterChain.doFilter(request, response);
        }
        finally {
            System.out.println(
                    "After request: " + request.getRequestURI()
                            + " | Status: " + response.getStatus()
            );
        }




    }
}
