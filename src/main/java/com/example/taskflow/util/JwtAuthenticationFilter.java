package com.example.taskflow.util;

import com.example.taskflow.Dto.UserDTO;
import com.example.taskflow.Entity.User;
import com.example.taskflow.exception.ResourceNotFoundException;
import com.example.taskflow.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor; /*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.Authentication; */
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter  {
//extends OncePerRequestFilter
 /*
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private UserMapper userMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null && jwtTokenUtil.isTokenValid(token)) {
            String email = jwtTokenUtil.getEmailFromToken(token);
            User user = null;
            try {
                 user = userMapper.dtoToEntity(userService.findByEmail(email));
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }

            if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    } */

}
