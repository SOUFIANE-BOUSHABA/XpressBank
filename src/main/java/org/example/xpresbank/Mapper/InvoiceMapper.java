package org.example.xpresbank.Mapper;

import org.example.xpresbank.DTO.CreateInvoiceDTO;
import org.example.xpresbank.DTO.InvoiceDTO;
import org.example.xpresbank.Entity.Enums.InvoiceStatus;
import org.example.xpresbank.Entity.Invoice;
import org.example.xpresbank.Entity.User;
import org.example.xpresbank.VM.InvoiceVM;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public Invoice fromCreateInvoiceDTO(CreateInvoiceDTO createInvoiceDTO, User user) {
        return Invoice.builder()
                .description(createInvoiceDTO.getDescription())
                .amount(createInvoiceDTO.getAmount())
                .dueDate(createInvoiceDTO.getDueDate())
                .issueDate(createInvoiceDTO.getIssueDate())
                .status(InvoiceStatus.PENDING)
                .user(user)
                .build();
    }

    public InvoiceDTO toInvoiceDTO(Invoice invoice) {
        return InvoiceDTO.builder()
                .id(invoice.getId())
                .description(invoice.getDescription())
                .amount(invoice.getAmount())
                .status(invoice.getStatus().name())
                .dueDate(invoice.getDueDate())
                .issueDate(invoice.getIssueDate())
                .userId(invoice.getUser().getId())
                .build();
    }

    public InvoiceVM toInvoiceVM(Invoice invoice, String message) {
        return InvoiceVM.builder()
                .id(invoice.getId())
                .description(invoice.getDescription())
                .amount(invoice.getAmount())
                .status(invoice.getStatus().name())
                .message(message)
                .build();
    }
}
