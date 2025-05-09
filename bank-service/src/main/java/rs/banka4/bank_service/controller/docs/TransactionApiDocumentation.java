package rs.banka4.bank_service.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import rs.banka4.bank_service.domain.transaction.db.TransactionStatus;
import rs.banka4.bank_service.domain.transaction.dtos.CreateFeeTransactionDto;
import rs.banka4.bank_service.domain.transaction.dtos.CreatePaymentDto;
import rs.banka4.bank_service.domain.transaction.dtos.CreateTransferDto;
import rs.banka4.bank_service.domain.transaction.dtos.TransactionDto;
import rs.banka4.bank_service.exceptions.account.AccountNotFound;
import rs.banka4.bank_service.exceptions.account.NotAccountOwner;
import rs.banka4.bank_service.exceptions.transaction.ClientCannotPayToOwnAccount;
import rs.banka4.bank_service.exceptions.transaction.InsufficientFunds;
import rs.banka4.bank_service.exceptions.transaction.TransactionNotFound;
import rs.banka4.bank_service.exceptions.user.client.ClientNotFound;

@Tag(
    name = "TransactionController",
    description = "Endpoints for payments"
)
public interface TransactionApiDocumentation {

    @Operation(
        summary = "Create a new Transaction",
        description = "Creates a new payment with the provided details.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Successfully created new transaction"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request - Invalid data or Insufficient funds",
                content = @Content(schema = @Schema(implementation = InsufficientFunds.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Client not found",
                content = @Content(schema = @Schema(implementation = ClientNotFound.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Account not found",
                content = @Content(schema = @Schema(implementation = AccountNotFound.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Conflict - Cannot pay to yourself",
                content = @Content(
                    schema = @Schema(implementation = ClientCannotPayToOwnAccount.class)
                )
            )
        }
    )
    ResponseEntity<TransactionDto> createTransaction(
        Authentication authentication,
        @Valid CreatePaymentDto createPaymentDto
    );

    @Operation(
        summary = "Create a new Transfer",
        description = "Creates a new transfer. The client can only transfer using their own account.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Successfully created new payment"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request - Invalid data or Insufficient funds",
                content = @Content(schema = @Schema(implementation = InsufficientFunds.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Not account owner",
                content = @Content(schema = @Schema(implementation = NotAccountOwner.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Client not found",
                content = @Content(schema = @Schema(implementation = ClientNotFound.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Account not found",
                content = @Content(schema = @Schema(implementation = AccountNotFound.class))
            ),
        }
    )
    ResponseEntity<TransactionDto> createTransfer(
        Authentication authentication,
        @Valid CreateTransferDto createTransferDto
    );

    @Operation(
        summary = "Pay a Fee",
        description = "Creates a fee transaction for the authenticated client. This endpoint is used for paying service fees.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Fee payment successfully created"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request - Invalid data or insufficient funds",
                content = @Content(schema = @Schema(implementation = InsufficientFunds.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Client or account not found",
                content = @Content(schema = @Schema(implementation = ClientNotFound.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - Not authorized to perform this action"
            )
        }
    )
    ResponseEntity<Void> payFee(@Valid CreateFeeTransactionDto createFeeTransactionDto);

    @Operation(
        summary = "Get Client Payments",
        description = "Retrieves the list of payments for the authenticated client. Requires authentication.",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved payments",
                content = @Content(schema = @Schema(implementation = TransactionDto.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized - Token errors"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - Access denied"
            )
        }
    )
    ResponseEntity<Page<TransactionDto>> getAllTransactionsForClient(
        Authentication auth,
        @Parameter(description = "Payment status") TransactionStatus status,
        @Parameter(description = "Payment amount") BigDecimal amount,
        @Parameter(description = "Payments on date") LocalDate date,
        @Parameter(description = "Account number") String accountNumber,
        @Parameter(description = "Page number") int page,
        @Parameter(description = "Number of employees per page") int size
    );

    @Operation(
        summary = "Get Transaction by ID",
        description = "Retrieves the transaction with the provided ID.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved transaction",
                content = @Content(schema = @Schema(implementation = TransactionDto.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Transaction not found",
                content = @Content(schema = @Schema(implementation = TransactionNotFound.class))
            )
        }
    )
    ResponseEntity<TransactionDto> getTransactionById(
        Authentication auth,
        @Parameter(description = "Transaction ID") UUID id
    );

    @Operation(
        summary = "Get Client Transfers",
        description = "Retrieves the list of transfers for the authenticated client. Requires authentication.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved transfers",
                content = @Content(schema = @Schema(implementation = TransactionDto.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized - Token errors"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - Access denied"
            )
        }
    )
    ResponseEntity<Page<TransactionDto>> getAllTransfers(
        Authentication auth,
        @Parameter(description = "Page number") int page,
        @Parameter(description = "Number of transfers per page") int size
    );
}
