package com.yusuf.supplierService.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    //Generic
    private int status;
    private String message;

    //for pagination
    private Integer totalPages;
    private Integer totalElements;

    private SupplierDTO supplier;
    private List<SupplierDTO> suppliers;


    private final LocalDateTime timestamp = LocalDateTime.now();
}
