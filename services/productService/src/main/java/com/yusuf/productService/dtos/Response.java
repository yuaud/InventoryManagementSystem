package com.yusuf.productService.dtos;

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

    //data output optionals
    private CategoryDTO category;
    private List<CategoryDTO> categories;

    private ProductDTO product;
    private List<ProductDTO> products;

    private final LocalDateTime timestamp = LocalDateTime.now();
}
