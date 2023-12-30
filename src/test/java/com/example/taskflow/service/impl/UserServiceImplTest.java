package com.example.taskflow.service.impl;

import com.example.taskflow.Dto.UserDTO;
import com.example.taskflow.Dto.response.UserVM;
import com.example.taskflow.Entity.Role;
import com.example.taskflow.Entity.User;
import com.example.taskflow.exception.ResourceNotFoundException;
import com.example.taskflow.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    ModelMapper modelMapper ;
    @InjectMocks
    private UserServiceImpl userService;

  //  @InjectMocks
   // private CompetitionServiceImpl competitionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void createUser() {
        assertTrue(false);
    }

    // Create a new user with valid input and return the created user.
    @Test
    public void test_createUser_validInput() throws ResourceNotFoundException {
        // Arrange
        UserDTO userDTO = new UserDTO();
        UserVM vm = new UserVM();
        vm.setEmail("test@example.com");userDTO.setEmail("test@example.com");
        vm.setUsername("test");
        userDTO.setPassword("password");
        Role admin = Role.builder().id(1L).name("admin").build();
        List<Role> list = new ArrayList<>();
        list.add(admin);
        userDTO.setAuthorities(list);

        when(userService.createUser(userDTO)).thenReturn(vm);

        // Act
        UserVM actualResult = userService.createUser(userDTO);

        // Assert
        assertNotNull(actualResult);
        assertEquals(vm.getEmail(), actualResult.getEmail());
        assertEquals(vm.getUsername(), actualResult.getUsername());
    }




    // Throw a ResourceNotFoundException if the email already exists in the database.
    @Test
    public void test_createUser_existingEmail() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("existing@example.com");
        userDTO.setPassword("password");
        Role admin = Role.builder().id(1L).name("admin").build();
        List<Role> list = new ArrayList<>();
        list.add(admin);
        userDTO.setAuthorities(list);
        User existingUser = new User();
        existingUser.setEmail("existing@example.com");
        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));
        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () ->userService.createUser(userDTO));
    }

}
