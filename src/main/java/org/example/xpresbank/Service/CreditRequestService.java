package org.example.xpresbank.Service;

import jakarta.transaction.Transactional;
import org.example.xpresbank.DTO.CreateCreditRequestDTO;
import org.example.xpresbank.DTO.CreditRequestDTO;
import org.example.xpresbank.Entity.CreditRequest;
import org.example.xpresbank.Entity.Enums.CreditRequestStatus;
import org.example.xpresbank.Entity.User;
import org.example.xpresbank.Exception.CreditRequestNotFoundException;
import org.example.xpresbank.Exception.UserNotEligibleException;
import org.example.xpresbank.Mapper.CreditRequestMapper;
import org.example.xpresbank.Repository.CreditRequestRepository;
import org.example.xpresbank.Repository.UserRepository;
import org.example.xpresbank.Utils.AuthUtils;
import org.example.xpresbank.Utils.LoanEligibilityChecker;
import org.example.xpresbank.VM.CreditRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditRequestService {

    private final CreditRequestRepository creditRequestRepository;
    private final UserRepository userRepository;
    private final CreditRequestMapper creditRequestMapper;
    private final LoanEligibilityChecker loanEligibilityChecker;

    @Autowired
    public CreditRequestService(CreditRequestRepository creditRequestRepository, UserRepository userRepository, CreditRequestMapper creditRequestMapper, LoanEligibilityChecker loanEligibilityChecker) {
        this.creditRequestRepository = creditRequestRepository;
        this.userRepository = userRepository;
        this.creditRequestMapper = creditRequestMapper;
        this.loanEligibilityChecker = loanEligibilityChecker;
    }

    @Transactional
    public CreditRequestVM createCreditRequest(Long userId, CreateCreditRequestDTO createCreditRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CreditRequestNotFoundException("User not found with ID: " + userId));

        if (!loanEligibilityChecker.isEligible(user, createCreditRequestDTO.getAmount())) {
            throw new UserNotEligibleException("User is not eligible for the requested credit amount");
        }

        CreditRequest creditRequest = creditRequestMapper.fromCreateCreditRequestDTO(createCreditRequestDTO, user);
        CreditRequest savedCreditRequest = creditRequestRepository.save(creditRequest);
        return creditRequestMapper.toCreditRequestVM(savedCreditRequest, "Credit request submitted successfully");
    }

    public List<CreditRequestDTO> getAllCreditRequests() {
        return creditRequestRepository.findAll().stream()
                .map(creditRequestMapper::toCreditRequestDTO)
                .collect(Collectors.toList());
    }

    public CreditRequestDTO getCreditRequestById(Long creditRequestId) {
        CreditRequest creditRequest = creditRequestRepository.findById(creditRequestId)
                .orElseThrow(() -> new CreditRequestNotFoundException("Credit request not found with ID: " + creditRequestId));
        return creditRequestMapper.toCreditRequestDTO(creditRequest);
    }

    @Transactional
    public CreditRequestVM approveCreditRequest(Long userId, Long creditRequestId, boolean approve) {
        User adminOrEmployee = userRepository.findById(userId)
                .orElseThrow(() -> new CreditRequestNotFoundException("User not found with ID: " + userId));

        if (!AuthUtils.hasRole(adminOrEmployee, "ADMIN") && !AuthUtils.hasRole(adminOrEmployee, "EMPLOYEE")) {
            throw new SecurityException("Unauthorized. Only ADMIN or EMPLOYEE users can approve credit requests.");
        }

        CreditRequest creditRequest = creditRequestRepository.findById(creditRequestId)
                .orElseThrow(() -> new CreditRequestNotFoundException("Credit request not found with ID: " + creditRequestId));

        if (approve) {
            creditRequest.setStatus(CreditRequestStatus.APPROVED);
            return creditRequestMapper.toCreditRequestVM(creditRequestRepository.save(creditRequest), "Credit request approved successfully");
        } else {
            creditRequest.setStatus(CreditRequestStatus.REJECTED);
            return creditRequestMapper.toCreditRequestVM(creditRequestRepository.save(creditRequest), "Credit request rejected successfully");
        }
    }
}
