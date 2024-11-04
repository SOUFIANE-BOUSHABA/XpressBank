package org.example.xpresbank.DTO;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequestDTO {
    private Long id;
    private double amount;
    private double interestRate;
    private Date startDate;
    private Date endDate;
    private String status;
    private Long userId;
    private String eligibilityStatus;
}
