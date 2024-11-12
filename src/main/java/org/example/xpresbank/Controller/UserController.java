package org.example.xpresbank.Controller;

import org.example.xpresbank.DTO.RegisterUserDTO;
import org.example.xpresbank.DTO.UserDTO;
import org.example.xpresbank.Service.AuthService;
import org.example.xpresbank.Service.UserService;
import org.example.xpresbank.Utils.AuthUtils;
import org.example.xpresbank.Entity.User;
import org.example.xpresbank.VM.UserVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    private void checkAdminPermission(String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();

        User user = authService.getUserFromSession(token);

        if (!AuthUtils.hasRole(user, "ADMIN")) {
            throw new SecurityException("Unauthorized. Only ADMIN users can perform this action.");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<UserVM> createUser(@RequestHeader("Authorization") String authorizationHeader,
                                             @RequestBody RegisterUserDTO registerUserDTO) {
        checkAdminPermission(authorizationHeader);
        UserVM userVM = userService.createUser(registerUserDTO);
        return ResponseEntity.ok(userVM);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader("Authorization") String authorizationHeader) {
        checkAdminPermission(authorizationHeader);
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@RequestHeader("Authorization") String authorizationHeader,
                                               @PathVariable Long userId) {
        checkAdminPermission(authorizationHeader);
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserVM> updateUser(@RequestHeader("Authorization") String authorizationHeader,
                                             @PathVariable Long userId,
                                             @RequestBody RegisterUserDTO registerUserDTO) {
        checkAdminPermission(authorizationHeader);
        UserVM updatedUser = userService.updateUser(userId, registerUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authorizationHeader,
                                             @PathVariable Long userId) {
        checkAdminPermission(authorizationHeader);
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
