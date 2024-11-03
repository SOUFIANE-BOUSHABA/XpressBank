package org.example.xpresbank.DTO;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private String type;
    private double amount;
    private double transactionFee;
    private Date timestamp;
    private String status;
}
