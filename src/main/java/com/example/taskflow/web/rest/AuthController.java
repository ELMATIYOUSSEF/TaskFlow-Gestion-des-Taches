package com.example.taskflow.web.rest;

import com.example.taskflow.Dto.UserDTO;
import com.example.taskflow.Entity.User;
import com.example.taskflow.exception.ResourceNotFoundException;
import com.example.taskflow.handler.response.ResponseMessage;
import com.example.taskflow.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@SessionAttributes("user")
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password, Model model) throws ResourceNotFoundException {
        String Token = userService.Login(email, password, model);
        return ResponseMessage.ok("Logged with successfully" , Token);
    }


    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) throws IllegalAccessException {
        String string = userService.logout(token);
        return ResponseEntity.ok().body(Map.of("message", string));
    }


    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDTO) throws IllegalAccessException {
        UserDTO register = userService.register(userDTO);
        return ResponseEntity.ok().body(Map.of("done", register));
    }

}
