package org.example.xpresbank.Mapper;

import org.example.xpresbank.DTO.UserDTO;
import org.example.xpresbank.DTO.RegisterUserDTO;
import org.example.xpresbank.Entity.User;
import org.example.xpresbank.VM.UserVM;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.isActive())
                .role(user.getRole().getName().name())
                .build();
    }

    public User fromRegisterUserDTO(RegisterUserDTO registerUserDTO) {
        return User.builder()
                .username(registerUserDTO.getUsername())
                .password(registerUserDTO.getPassword())
                .email(registerUserDTO.getEmail())
                .active(registerUserDTO.isActive())
                .build();
    }

    public UserVM toUserVM(User user, String message) {
        return UserVM.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.isActive())
                .role(user.getRole().getName().name())
                .message(message)
                .build();
    }
}
