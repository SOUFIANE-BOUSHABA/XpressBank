package org.example.xpresbank.Service;

import jakarta.transaction.Transactional;
import org.example.xpresbank.DTO.AccountDTO;
import org.example.xpresbank.Entity.Account;
import org.example.xpresbank.Entity.Enums.AccountStatus;
import org.example.xpresbank.Entity.User;
import org.example.xpresbank.Exception.AccountNotFoundException;
import org.example.xpresbank.Mapper.AccountMapper;
import org.example.xpresbank.Repository.AccountRepository;
import org.example.xpresbank.Repository.UserRepository;
import org.example.xpresbank.VM.AccountVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;
    }

    @Transactional
    public AccountVM createAccount(String accountNumber, double balance, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .balance(balance)
                .user(user)
                .status(AccountStatus.ACTIVE)
                .build();

        Account savedAccount = accountRepository.save(account);
        return accountMapper.toAccountVM(savedAccount, "Account created successfully");
    }

    public List<AccountDTO> getAccountsByUserId(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accounts.stream().map(accountMapper::toAccountDTO).collect(Collectors.toList());
    }

    public AccountVM getAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        return accountMapper.toAccountVM(account, "Account retrieved successfully");
    }

    @Transactional
    public AccountVM updateAccountStatus(Long accountId, AccountStatus status) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        account.setStatus(status);
        Account updatedAccount = accountRepository.save(account);
        return accountMapper.toAccountVM(updatedAccount, "Account status updated successfully");
    }

    public void deleteAccount(Long accountId , Long userId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
        if(!account.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized. Only the account owner can delete it.");
        }
        accountRepository.delete(account);
    }



    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(accountMapper::toAccountDTO).collect(Collectors.toList());
    }
}
