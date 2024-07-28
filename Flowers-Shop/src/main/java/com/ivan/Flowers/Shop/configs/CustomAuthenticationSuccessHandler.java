package com.ivan.Flowers.Shop.configs;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        authorities.forEach(authority -> {
            try {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    response.sendRedirect("/home");
                } else if (authority.getAuthority().equals("ROLE_MODERATOR")) {
                    response.sendRedirect("/orders/pending");
                } else if (authority.getAuthority().equals("ROLE_USER")) {
                    response.sendRedirect("/home");
                }
//                else {
//                    response.sendRedirect("/home");
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
