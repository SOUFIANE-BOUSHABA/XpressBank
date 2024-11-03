package org.example.xpresbank.Mapper;

import org.example.xpresbank.DTO.CreateTransactionDTO;
import org.example.xpresbank.DTO.TransactionDTO;
import org.example.xpresbank.Entity.Account;
import org.example.xpresbank.Entity.Enums.TransactionType;
import org.example.xpresbank.Entity.Transaction;
import org.example.xpresbank.VM.TransactionVM;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(CreateTransactionDTO createTransactionDTO, Account sourceAccount, Account destinationAccount) {
        return Transaction.builder()
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .type(TransactionType.valueOf(createTransactionDTO.getType().toUpperCase()))
                .amount(createTransactionDTO.getAmount())
                .transactionFee(calculateTransactionFee(createTransactionDTO.getType(), createTransactionDTO.getAmount()))
                .status("PENDING")
                .timestamp(new java.util.Date())
                .build();
    }

    public TransactionVM toTransactionVM(Transaction transaction, String message) {
        return TransactionVM.builder()
                .id(transaction.getId())
                .sourceAccountNumber(transaction.getSourceAccount().getAccountNumber())
                .destinationAccountNumber(transaction.getDestinationAccount() != null ? transaction.getDestinationAccount().getAccountNumber() : null)
                .amount(transaction.getAmount())
                .transactionFee(transaction.getTransactionFee())
                .type(transaction.getType().name())
                .status(transaction.getStatus())
                .message(message)
                .build();
    }



    private double calculateTransactionFee(String type, double amount) {
        switch (type.toUpperCase()) {
            case "CLASSIC":
                return amount * 0.02;
            case "INSTANT":
                return amount * 0.05;
            default:
                return 0.0;
        }
    }
}