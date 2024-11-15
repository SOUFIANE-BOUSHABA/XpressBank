package org.example.xpresbank.Controller;

import org.example.xpresbank.Entity.User;
import org.example.xpresbank.Service.AccountService;
import org.example.xpresbank.Service.AuthService;
import org.example.xpresbank.Utils.PermissionUtils;
import org.example.xpresbank.VM.AccountVM;
import org.example.xpresbank.DTO.AccountDTO;
import org.example.xpresbank.Entity.Enums.AccountStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final PermissionUtils permissionUtils;
    private final AuthService authService;

    @Autowired
    public AccountController(AccountService accountService, PermissionUtils permissionUtils , AuthService authService) {
        this.accountService = accountService;
        this.permissionUtils = permissionUtils;
        this.authService = authService;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountVM> createAccount(@RequestHeader("Authorization") String authorizationHeader,
                                                   @RequestBody AccountDTO accountDTO) {
        permissionUtils.checkUserPermission(authorizationHeader);
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = authService.getUserFromSession(token);
        AccountVM account = accountService.createAccount(accountDTO.getAccountNumber(), accountDTO.getBalance(), user.getId());
        return ResponseEntity.ok(account);
    }


    @GetMapping("/user/accounts")
    public ResponseEntity<List<AccountDTO>> getAccountsByUserId(@RequestHeader("Authorization") String authorizationHeader) {
        permissionUtils.checkUserPermission(authorizationHeader);

        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = authService.getUserFromSession(token);

        List<AccountDTO> accounts = accountService.getAccountsByUserId(user.getId());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountVM> getAccount(@RequestHeader("Authorization") String authorizationHeader,
                                                @PathVariable Long accountId) {
        AccountVM account = accountService.getAccount(accountId);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{accountId}/status")
    public ResponseEntity<AccountVM> updateAccountStatus(@RequestHeader("Authorization") String authorizationHeader,
                                                         @PathVariable Long accountId,
                                                         @RequestBody String status) {
        permissionUtils.checkAdminPermission(authorizationHeader);
        AccountStatus accountStatus = AccountStatus.valueOf(status.toUpperCase());
        AccountVM updatedAccount = accountService.updateAccountStatus(accountId, accountStatus);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@RequestHeader("Authorization") String authorizationHeader,
                                                @PathVariable Long accountId) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = authService.getUserFromSession(token);
        permissionUtils.checkUserPermission(authorizationHeader);
        accountService.deleteAccount(accountId , user.getId());
        return ResponseEntity.ok("Account deleted successfully");
    }



    @GetMapping("/all")
    public ResponseEntity<List<AccountDTO>> getAllAccounts(@RequestHeader("Authorization") String authorizationHeader) {
        permissionUtils.checkAdminPermission(authorizationHeader);
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
}
