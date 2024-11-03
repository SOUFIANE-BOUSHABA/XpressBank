package org.example.xpresbank.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionDTO {
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private String type;
    private double amount;
}
