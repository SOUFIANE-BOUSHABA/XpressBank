package org.example.xpresbank.VM;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequestVM {
    private Long id;
    private double amount;
    private double interestRate;
    private Date startDate;
    private Date endDate;
    private String status;
    private Long userId;
    private String eligibilityStatus;
    private String message;
}
