package com.yusuf.transactionService.services.impl;


import com.yusuf.transactionService.dtos.Response;
import com.yusuf.transactionService.dtos.TransactionDTO;
import com.yusuf.transactionService.dtos.TransactionRequest;
import com.yusuf.transactionService.enums.TransactionStatus;
import com.yusuf.transactionService.enums.TransactionType;
import com.yusuf.transactionService.exceptions.NameValueRequiredException;
import com.yusuf.transactionService.exceptions.NotFoundException;
import com.yusuf.transactionService.feign.UserInterface;
import com.yusuf.transactionService.models.Product;
import com.yusuf.transactionService.models.Supplier;
import com.yusuf.transactionService.models.Transaction;
import com.yusuf.transactionService.models.User;
import com.yusuf.transactionService.repositories.ProductRepository;
import com.yusuf.transactionService.repositories.SupplierRepository;
import com.yusuf.transactionService.repositories.TransactionRepository;
import com.yusuf.transactionService.services.TransactionService;
import com.yusuf.transactionService.specification.TransactionFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    private final UserInterface userInterface;


    @Override
    public Response purchase(TransactionRequest transactionRequest) {

        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if(supplierId == null) throw new NameValueRequiredException("Supplier ID is required");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        //User user = userService.getCurrentLoggedInUser();

        User user = userInterface.getCurrentUser().getBody();


        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        //create transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .supplier(supplier)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Purchase successful")
                .build();
    }

    @Override
    public Response sell(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Integer quantity = transactionRequest.getQuantity();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        //User user = userService.getCurrentLoggedInUser();
        User user = userInterface.getCurrentUser().getBody();

        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        //Create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.SALE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Product Sale successful")
                .build();
    }

    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {

        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if(supplierId == null) throw new NameValueRequiredException("Supplier ID is required");

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        //User user = userService.getCurrentLoggedInUser();
        User user = userInterface.getCurrentUser().getBody();

        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        //create transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .status(TransactionStatus.PROCESSING)
                .product(product)
                .supplier(supplier)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .description(transactionRequest.getDescription())
                .note(transactionRequest.getNote())
                .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Product Returned in progress")
                .build();
    }

    @Override
    public Response getAllTransactions(int page, int size, String filter) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        //use the transaction specification
        Specification<Transaction> specification = TransactionFilter.byFilter(filter);
        Page<Transaction> transactionPage = transactionRepository.findAll(specification, pageable);

        List<TransactionDTO> transactionDTOS = modelMapper.map(transactionPage.getContent(), new TypeToken<List<TransactionDTO>>()
        {}.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("Success")
                .transactions(transactionDTOS)
                .totalElements(transactionPage.getTotalElements())
                .totalPages(transactionPage.getTotalPages())
                .build();
    }

    @Override
    public Response getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);

        transactionDTO.getUser().setTransactions(null);
        //transactionDTO.setUser(null);

        return Response.builder()
                .status(200)
                .message("Transaction successfully retrieved")
                .transaction(transactionDTO)
                .build();
    }

    @Override
    public Response getAllTransactionByMonthAndYear(int month, int year) {
        List<Transaction> transactions = transactionRepository.findAll(TransactionFilter.byMonthAndYear(month, year));

        List<TransactionDTO> transactionDTOS = modelMapper.map(transactions,
                new TypeToken<List<TransactionDTO>>() {}.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setProduct(null);
            transactionDTO.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("Transaction successfully retrieved")
                .transactions(transactionDTOS)
                .build();
    }

    @Override
    public Response updateTransaction(Long transactionId, TransactionStatus status) {
        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
        existingTransaction.setStatus(status);
        existingTransaction.setUpdateAt(LocalDateTime.now());

        transactionRepository.save(existingTransaction);

        return Response.builder()
                .status(200)
                .message("Transaction Status Successfully Updated")
                .build();
    }
}
