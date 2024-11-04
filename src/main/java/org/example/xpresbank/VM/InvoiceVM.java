package org.example.xpresbank.VM;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceVM {
    private Long id;
    private String description;
    private double amount;
    private String status;
    private String message;
}
