package org.example.xpresbank.VM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVM {
    private Long id;
    private String username;
    private String email;
    private boolean active;
    private String role;
    private String message;
}
