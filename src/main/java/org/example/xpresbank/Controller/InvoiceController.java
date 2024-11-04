package org.example.xpresbank.Controller;

import org.example.xpresbank.DTO.CreateInvoiceDTO;
import org.example.xpresbank.DTO.InvoiceDTO;
import org.example.xpresbank.Entity.User;
import org.example.xpresbank.Service.AuthService;
import org.example.xpresbank.Service.InvoiceService;
import org.example.xpresbank.Utils.PermissionUtils;
import org.example.xpresbank.VM.InvoiceVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final PermissionUtils permissionUtils;
    private final AuthService authService;



    @Autowired
    public InvoiceController(InvoiceService invoiceService, PermissionUtils permissionUtils, AuthService authService) {
        this.invoiceService = invoiceService;
        this.permissionUtils = permissionUtils;
        this.authService = authService;
    }



    @PostMapping("/create")
    public ResponseEntity<InvoiceVM> createInvoice(@RequestHeader("Authorization") String authorizationHeader,
                                                   @RequestBody CreateInvoiceDTO createInvoiceDTO) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = authService.getUserFromSession(token);

        InvoiceVM invoiceVM = invoiceService.createInvoice(user.getId(), createInvoiceDTO);
        return ResponseEntity.ok(invoiceVM);
    }



    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = authService.getUserFromSession(token);

        if (!permissionUtils.isAdminOrEmployee(user)) {
            throw new SecurityException("Unauthorized. Only ADMIN or EMPLOYEE users can view all invoices.");
        }

        List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }




    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@RequestHeader("Authorization") String authorizationHeader,
                                                     @PathVariable Long invoiceId) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = authService.getUserFromSession(token);

        InvoiceDTO invoice = invoiceService.getInvoiceById(invoiceId);

        if (!permissionUtils.isAdminOrEmployee(user) && !invoice.getUserId().equals(user.getId())) {
            throw new SecurityException("Unauthorized. You do not have permission to view this invoice.");
        }
        return ResponseEntity.ok(invoice);
    }



    @PutMapping("/update-status/{invoiceId}")
    public ResponseEntity<InvoiceVM> updateInvoiceStatus(@RequestHeader("Authorization") String authorizationHeader,
                                                         @PathVariable Long invoiceId,
                                                         @RequestParam String status) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = authService.getUserFromSession(token);

        if (!permissionUtils.isAdminOrEmployee(user)) {
            throw new SecurityException("Unauthorized. Only ADMIN or EMPLOYEE users can update the status of an invoice.");
        }
        InvoiceVM invoiceVM = invoiceService.updateInvoiceStatus(invoiceId, status);
        return ResponseEntity.ok(invoiceVM);
    }
}
