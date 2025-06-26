package com.yusuf.transactionService.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yusuf.transactionService.enums.TransactionStatus;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {
    @Positive(message = "Product id is required")
    private Long productId;

    @Positive(message = "Quantity is required")
    private Integer quantity;

    @Positive(message = "Supplier id is required")
    private Long supplierId;

    private String description;
    private String note;
}
