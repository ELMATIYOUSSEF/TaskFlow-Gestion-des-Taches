package com.example.taskflow.security;


import com.example.taskflow.Entity.Role;
import com.example.taskflow.Entity.User;
import com.example.taskflow.repository.RoleRepository;
import com.example.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class UserAuth {
    public final UserRepository userRepository;
    public final RoleRepository roleRepository;
    @Bean
    public User generateUser(){
        //
        Role admin = Role.builder().id(1L).name("admin").build();
        Optional<Role> roleOptional = roleRepository.findById(1L);
        Role role = roleOptional.orElseGet(() -> roleRepository.save(admin));
        List<Role> listRole = new ArrayList<>();
        listRole.add(role);

        Optional<User> user = userRepository.findById(1L);
      return user.orElseGet(() -> {
          User build = User.builder().email("youssef@gmail.com")
                  .authorities(listRole)
                  .password(BCrypt.hashpw("youssef", BCrypt.gensalt()))
                  .username("Mati").build();
          return userRepository.save(build);
      });
    }
}
