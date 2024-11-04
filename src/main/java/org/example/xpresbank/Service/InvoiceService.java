package org.example.xpresbank.Service;

import jakarta.transaction.Transactional;
import org.example.xpresbank.DTO.CreateInvoiceDTO;
import org.example.xpresbank.DTO.InvoiceDTO;
import org.example.xpresbank.Entity.Enums.InvoiceStatus;
import org.example.xpresbank.Entity.Invoice;
import org.example.xpresbank.Entity.User;
import org.example.xpresbank.Exception.InvoiceNotFoundException;
import org.example.xpresbank.Mapper.InvoiceMapper;
import org.example.xpresbank.Repository.InvoiceRepository;
import org.example.xpresbank.Repository.UserRepository;
import org.example.xpresbank.VM.InvoiceVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final InvoiceMapper invoiceMapper;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, UserRepository userRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Transactional
    public InvoiceVM createInvoice(Long userId, CreateInvoiceDTO createInvoiceDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvoiceNotFoundException("User not found with ID: " + userId));

        Invoice invoice = invoiceMapper.fromCreateInvoiceDTO(createInvoiceDTO, user);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toInvoiceVM(savedInvoice, "Invoice created successfully");
    }

    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(invoiceMapper::toInvoiceDTO)
                .collect(Collectors.toList());
    }

    public InvoiceDTO getInvoiceById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with ID: " + invoiceId));
        return invoiceMapper.toInvoiceDTO(invoice);
    }

    @Transactional
    public InvoiceVM updateInvoiceStatus(Long invoiceId, String status) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with ID: " + invoiceId));

        invoice.setStatus(InvoiceStatus.valueOf(status.toUpperCase()));
        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toInvoiceVM(updatedInvoice, "Invoice status updated successfully");
    }
}
