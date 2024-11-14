package org.example.xpresbank.Controller;

import org.example.xpresbank.DTO.CreateCreditRequestDTO;
import org.example.xpresbank.DTO.CreditRequestDTO;
import org.example.xpresbank.Entity.User;
import org.example.xpresbank.Service.AuthService;
import org.example.xpresbank.Service.CreditRequestService;
import org.example.xpresbank.Utils.PermissionUtils;
import org.example.xpresbank.VM.CreditRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credit-requests")

public class CreditRequestController {

    private final CreditRequestService creditRequestService;
    private final PermissionUtils permissionUtils;
    private final AuthService authService;

    @Autowired
    public CreditRequestController(CreditRequestService creditRequestService, PermissionUtils permissionUtils, AuthService authService) {
        this.creditRequestService = creditRequestService;
        this.permissionUtils = permissionUtils;
        this.authService = authService;
    }

    @PostMapping("/request")
    public ResponseEntity<CreditRequestVM> requestCredit(@RequestHeader("Authorization") String authorizationHeader,
                                                         @RequestBody CreateCreditRequestDTO createCreditRequestDTO) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User requestingUser = authService.getUserFromSession(token);


        CreditRequestVM creditRequest = creditRequestService.createCreditRequest(requestingUser.getId(), createCreditRequestDTO);
        return ResponseEntity.ok(creditRequest);
    }

    @GetMapping
    public ResponseEntity<List<CreditRequestDTO>> getAllCreditRequests(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = authService.getUserFromSession(token);

        if (!permissionUtils.isAdminOrEmployee(user)) {
            List<CreditRequestDTO> userCreditRequests = creditRequestService.getCreditRequestsByUserId(user.getId());
            return ResponseEntity.ok(userCreditRequests);
        }

        List<CreditRequestDTO> creditRequests = creditRequestService.getAllCreditRequests();
        return ResponseEntity.ok(creditRequests);
    }



    @GetMapping("/{creditRequestId}")
    public ResponseEntity<CreditRequestDTO> getCreditRequestById(@RequestHeader("Authorization") String authorizationHeader,
                                                                 @PathVariable Long creditRequestId) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = authService.getUserFromSession(token);

        CreditRequestDTO creditRequest = creditRequestService.getCreditRequestById(creditRequestId);
        if (!permissionUtils.isAdminOrEmployee(user) && !creditRequest.getUserId().equals(user.getId())) {
            throw new SecurityException("Unauthorized. Only ADMIN, EMPLOYEE or the user who made the request can view this credit request.");
        }

        return ResponseEntity.ok(creditRequest);
    }

    @PutMapping("/approve/{creditRequestId}")
    public ResponseEntity<CreditRequestVM> approveCreditRequest(@RequestHeader("Authorization") String authorizationHeader,
                                                                @PathVariable Long creditRequestId,
                                                                @RequestParam boolean approve) {
        String token = authorizationHeader.substring("Bearer ".length()).trim();
        User user = authService.getUserFromSession(token);


        if (!permissionUtils.isAdminOrEmployee(user)) {
            throw new SecurityException("Unauthorized. Only ADMIN or EMPLOYEE users can approve/reject credit requests.");
        }

        CreditRequestVM creditRequestVM = creditRequestService.approveCreditRequest(user.getId(), creditRequestId, approve);
        return ResponseEntity.ok(creditRequestVM);
    }
}
