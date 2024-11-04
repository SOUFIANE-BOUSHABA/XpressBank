package org.example.xpresbank.DTO;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private Long id;
    private String description;
    private double amount;
    private String status;
    private Date dueDate;
    private Date issueDate;
    private Long userId;
}
