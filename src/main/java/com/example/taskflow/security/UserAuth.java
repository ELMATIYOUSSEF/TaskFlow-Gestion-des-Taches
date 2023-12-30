package com.example.taskflow.security;

import com.example.taskflow.Entity.Role;
import com.example.taskflow.Entity.User;
import com.example.taskflow.repository.RoleRepository;
import com.example.taskflow.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class UserAuth {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserAuth(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Bean
    public CommandLineRunner init() {
        return args -> generateUser();
    }

    public User generateUser() {
        Role admin = Role.builder().id(1L).name("ADMIN").build();
        Role userRole = Role.builder().id(2L).name("USER").build();
        Optional<Role> roleOptional = roleRepository.findById(1L);
        Optional<Role> roleUOptional = roleRepository.findById(2L);
        Role role = roleOptional.orElseGet(() -> roleRepository.save(admin));
        Role roleUser = roleUOptional.orElseGet(() -> roleRepository.save(userRole));
        List<Role> listRole = new ArrayList<>();
        listRole.add(role);
        listRole.add(roleUser);

        Optional<User> user = userRepository.findById(1L);
        return user.orElseGet(() -> {
            User build = User.builder()
                    .email("youssef@gmail.com")
                    .authorities(listRole)
                    .password(BCrypt.hashpw("youssef", BCrypt.gensalt()))
                    .username("Mati")
                    .build();
            return userRepository.save(build);
        });
    }
}
