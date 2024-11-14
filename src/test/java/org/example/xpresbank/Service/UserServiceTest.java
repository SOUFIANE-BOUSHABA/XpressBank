package org.example.xpresbank.Service;

import org.example.xpresbank.DTO.RegisterUserDTO;
import org.example.xpresbank.DTO.UserDTO;
import org.example.xpresbank.Entity.Enums.RoleType;
import org.example.xpresbank.Entity.Role;
import org.example.xpresbank.Entity.User;
import org.example.xpresbank.Exception.UserNotFoundException;
import org.example.xpresbank.Mapper.UserMapper;
import org.example.xpresbank.Repository.RoleRepository;
import org.example.xpresbank.Repository.UserRepository;
import org.example.xpresbank.VM.UserVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setRole("USER");
        Role role = new Role();
        User user = new User();
        UserVM userVM = new UserVM();

        when(roleRepository.findByName(RoleType.valueOf(registerUserDTO.getRole()))).thenReturn(Optional.of(role));
        when(userMapper.fromRegisterUserDTO(registerUserDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserVM(any(User.class), anyString())).thenReturn(userVM);

        UserVM result = userService.createUser(registerUserDTO);

        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void testGetUserById_UserFound() {
        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(userDTO, result);
    }

}