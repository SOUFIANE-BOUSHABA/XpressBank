package org.example.xpresbank.VM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionVM {
    private Long id;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private double amount;
    private double transactionFee;
    private String type;
    private String status;
    private String message;
}
