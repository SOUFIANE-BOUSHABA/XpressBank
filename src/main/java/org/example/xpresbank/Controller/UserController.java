package org.example.xpresbank.Controller;

import org.example.xpresbank.DTO.RegisterUserDTO;
import org.example.xpresbank.DTO.UserDTO;
import org.example.xpresbank.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterUserDTO registerUserDTO) {
        return userService.register(registerUserDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        return userService.login(username, password);
    }

    @GetMapping("/user")
    public UserDTO getLoggedInUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        return userService.getLoggedInUser(token);
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        userService.logout(token);
        return "User logged out successfully";
    }
}
