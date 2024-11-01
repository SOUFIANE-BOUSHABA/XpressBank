package org.example.xpresbank.Service;

import jakarta.transaction.Transactional;
import org.example.xpresbank.DTO.RegisterUserDTO;
import org.example.xpresbank.Entity.Enums.RoleType;
import org.example.xpresbank.Entity.Permission;
import org.example.xpresbank.Entity.Role;
import org.example.xpresbank.Entity.User;
import org.example.xpresbank.Exception.UserNotFoundException;
import org.example.xpresbank.Mapper.UserMapper;
import org.example.xpresbank.Repository.RoleRepository;
import org.example.xpresbank.Repository.UserRepository;
import org.example.xpresbank.VM.UserVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final Map<String, User> sessions = new HashMap<>();

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserVM register(RegisterUserDTO registerUserDTO) {
        Role role = roleRepository.findByName(RoleType.valueOf(registerUserDTO.getRole()))
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + registerUserDTO.getRole()));

        User newUser = userMapper.fromRegisterUserDTO(registerUserDTO);
        newUser.setRole(role);

        User savedUser = userRepository.save(newUser);
        return userMapper.toUserVM(savedUser, "Registration successful");
    }

    public String login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            String token = UUID.randomUUID().toString();
            sessions.put(token, user.get());
            return token;
        } else {
            throw new UserNotFoundException("Invalid username or password");
        }
    }

    public UserVM getLoggedInUser(String token) {
        User user = sessions.get(token);
        if (user == null) {
            throw new UserNotFoundException("User not logged in or token is invalid");
        }
        return userMapper.toUserVM(user, "User retrieved successfully");
    }

    public void logout(String token) {
        if (!sessions.containsKey(token)) {
            throw new UserNotFoundException("Invalid session token provided for logout");
        }
        sessions.remove(token);
    }

    public boolean hasPermission(User user, String permissionName) {
        return user.getRole().getPermissions().stream()
                .anyMatch(permission -> permission.getName().equals(permissionName));
    }
    @Transactional
    public void testPermisson(String token) {
        User user = sessions.get(token);
        if (user == null || !hasPermission(user, "USER_READ")) {
            throw new UserNotFoundException("You do not have permission to perform this action.");
        }

    }
}
