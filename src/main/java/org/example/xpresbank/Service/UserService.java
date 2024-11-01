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
    public UserDTO register(RegisterUserDTO registerUserDTO) {
        Role role = roleRepository.findByName(RoleType.valueOf(registerUserDTO.getRole()))
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + registerUserDTO.getRole()));

        User newUser = userMapper.fromRegisterUserDTO(registerUserDTO);
        newUser.setRole(role);

        return userMapper.toUserDTO(userRepository.save(newUser));
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

    public UserDTO getLoggedInUser(String token) {
        User user = sessions.get(token);
        if (user == null) {
            throw new UserNotFoundException("User not logged in or token is invalid");
        }
        return userMapper.toUserDTO(user);
    }

    public void logout(String token) {
        if (!sessions.containsKey(token)) {
            throw new UserNotFoundException("Invalid session token provided for logout");
        }
        sessions.remove(token);
    }
}
