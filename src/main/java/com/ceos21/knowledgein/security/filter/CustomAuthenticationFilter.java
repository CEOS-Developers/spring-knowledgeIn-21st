package com.ceos21.knowledgein.security.filter;

import com.ceos21.knowledgein.security.dto.request.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        final UsernamePasswordAuthenticationToken authenticationToken;

        try {
            RequestLogin requestUser = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            authenticationToken = new UsernamePasswordAuthenticationToken(requestUser.email(), requestUser.password());
        } catch (IOException e) {
            throw new AuthenticationServiceException("Authentication failed", e);
        }

        setDetails(request, authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
