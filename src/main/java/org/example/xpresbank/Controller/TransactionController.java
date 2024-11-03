package org.example.xpresbank.Controller;

import org.example.xpresbank.DTO.CreateTransactionDTO;
import org.example.xpresbank.Entity.User;
import org.example.xpresbank.Service.AuthService;
import org.example.xpresbank.Service.TransactionService;
import org.example.xpresbank.Utils.PermissionUtils;
import org.example.xpresbank.VM.TransactionVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final PermissionUtils permissionUtils;
    private final AuthService authService;

    @Autowired
    public TransactionController(TransactionService transactionService, PermissionUtils permissionUtils ,  AuthService authService) {
        this.transactionService = transactionService;
        this.permissionUtils = permissionUtils;
        this.authService = authService;

    }

    @PostMapping("/create")
    public ResponseEntity<TransactionVM> createTransaction(@RequestHeader("Authorization") String authorizationHeader,
                                                           @RequestBody CreateTransactionDTO createTransactionDTO) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User createdByUser = authService.getUserFromSession(token);
        permissionUtils.checkUserPermission(authorizationHeader);
        TransactionVM transaction = transactionService.createTransaction(createTransactionDTO , createdByUser);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/approve/{transactionId}")
    public ResponseEntity<TransactionVM> approveTransaction(@RequestHeader("Authorization") String authorizationHeader,
                                                            @PathVariable Long transactionId) {
        permissionUtils.CheckEmployPermission(authorizationHeader);
        TransactionVM approvedTransaction = transactionService.approveTransaction(transactionId);
        return ResponseEntity.ok(approvedTransaction);
    }

    @GetMapping("/user-transactions")
    public ResponseEntity<List<TransactionVM>> getUserTransactions(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = authService.getUserFromSession(token);
        List<TransactionVM> transactions = transactionService.getUserTransactions(user);
        return ResponseEntity.ok(transactions);
    }






}
