package org.example.xpresbank.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {
    private String username;
    private String password;
    private String email;
    private boolean active;
    private String role;


    private int age;
    private double monthlyIncome;
    private int creditScore;
    private double debtToIncomeRatio;
    private int bankingDuration;
}
