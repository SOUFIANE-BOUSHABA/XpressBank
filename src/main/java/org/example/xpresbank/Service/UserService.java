package org.example.xpresbank.Service;

import jakarta.transaction.Transactional;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserVM createUser(RegisterUserDTO registerUserDTO) {
        Role role = roleRepository.findByName(RoleType.valueOf(registerUserDTO.getRole()))
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + registerUserDTO.getRole()));

        User newUser = userMapper.fromRegisterUserDTO(registerUserDTO);
        newUser.setRole(role);

        User savedUser = userRepository.save(newUser);
        return userMapper.toUserVM(savedUser, "User created successfully");
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        return userMapper.toUserDTO(user);
    }

    @Transactional
    public UserVM updateUser(Long userId, RegisterUserDTO registerUserDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        existingUser.setUsername(registerUserDTO.getUsername());
        existingUser.setEmail(registerUserDTO.getEmail());
        existingUser.setActive(registerUserDTO.isActive());

        Role role = roleRepository.findByName(RoleType.valueOf(registerUserDTO.getRole()))
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + registerUserDTO.getRole()));
        existingUser.setRole(role);

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toUserVM(updatedUser, "User updated successfully");
    }



    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        userRepository.delete(user);
    }



    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }
}
