package rs.banka4.bank_service.service.abstraction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import rs.banka4.bank_service.domain.transaction.db.TransactionStatus;
import rs.banka4.bank_service.domain.transaction.dtos.CreateFeeTransactionDto;
import rs.banka4.bank_service.domain.transaction.dtos.CreatePaymentDto;
import rs.banka4.bank_service.domain.transaction.dtos.CreateTransferDto;
import rs.banka4.bank_service.domain.transaction.dtos.TransactionDto;

public interface TransactionService {
    TransactionDto createTransaction(
        Authentication authentication,
        CreatePaymentDto createPaymentDto
    );

    TransactionDto createTransfer(
        Authentication authentication,
        CreateTransferDto createTransferDto
    );

    void createFeeTransaction(CreateFeeTransactionDto createFeeTransactionDto);

    Page<TransactionDto> getAllTransactionsForClient(
        String token,
        TransactionStatus paymentStatus,
        BigDecimal amount,
        LocalDate paymentDate,
        String accountNumber,
        PageRequest pageRequest
    );

    TransactionDto getTransactionById(String token, UUID transactionId);

    Page<TransactionDto> getAllTransfersForClient(String token, PageRequest pageRequest);
}
