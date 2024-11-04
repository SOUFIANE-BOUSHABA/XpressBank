package org.example.xpresbank.DTO;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceDTO {
    private String description;
    private double amount;
    private Date dueDate;
    private Date issueDate;
}