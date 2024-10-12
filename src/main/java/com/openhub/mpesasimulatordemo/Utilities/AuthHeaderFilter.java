package com.openhub.mpesasimulatordemo.Utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class AuthHeaderFilter extends OncePerRequestFilter {
    private final String[] protectedRoutes = {
            "/mpesa-sim/transaction/mpesa-express",
            "/mpesa-sim/transaction/mpesa-express"
    };
    private final FileHandler fileHandler;

    @Autowired
    public AuthHeaderFilter(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("requestURI: " + request.getRequestURI());
        boolean shouldFilter = false;

        for (String route : protectedRoutes) {
            if (request.getRequestURI().startsWith(route)) {
                shouldFilter = true;
                break;
            }
        }
        if (shouldFilter) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                final String token = authHeader.substring(7);
                try {
                    Set<String> access_tokens = fileHandler.readJSONTokens();
                    if (access_tokens.contains(token)) {
                        if (SecurityContextHolder.getContext().getAuthentication() == null && access_tokens.contains(token)) {
                            UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(token, null, null);
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        }
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");

                        // Build a JSON response
                        Map<String, Object> responseData = new HashMap<>();
                        responseData.put("status", "1");
                        responseData.put("message", "Not Authorised to Access Resource");
                        responseData.put("error", "UnAuthorized");

                        ObjectMapper objectMapper = new ObjectMapper();
                        String jsonResponse = objectMapper.writeValueAsString(responseData);

                        response.getWriter().write(jsonResponse);
                    }

                }
                catch (Exception e) {
                    System.out.println("Error parsing Bearer token: " + e.getMessage());

                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/json");

                    // Build a JSON response
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("status", "error");
                    responseData.put("message", "Could Not parse the token");
                    responseData.put("error", e.getMessage());

                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonResponse = objectMapper.writeValueAsString(responseData);

                    response.getWriter().write(jsonResponse);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
