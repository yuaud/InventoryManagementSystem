package com.yusuf.productService.services.impl;

import com.yusuf.productService.dtos.ProductDTO;
import com.yusuf.productService.dtos.Response;
import com.yusuf.productService.exceptions.NotFoundException;
import com.yusuf.productService.models.Category;
import com.yusuf.productService.models.Product;
import com.yusuf.productService.repositories.CategoryRepository;
import com.yusuf.productService.repositories.ProductRepository;
import com.yusuf.productService.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/services/productService/src/main/resources/images/";

    // After you set your fronend change this directory for frontend
    private static final String FRONTEND_IMAGE_DIRECTORY = "/home/yuaud/Desktop/InventoryManagementSystem/frontend/public/products/";

    @Override
    public Response saveProduct(ProductDTO productDTO, MultipartFile imageFile) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        //map our dto to product entity
        Product productToSave = Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .description(productDTO.getDescription())
                .category(category)
                .build();
        if (imageFile != null && !imageFile.isEmpty()) {
            log.info("Image file exists");
            String imagePath = saveImage(imageFile);
            productToSave.setImageUrl(imagePath);
        }
        //save the product entity
        productRepository.save(productToSave);
        return Response.builder()
                .status(200)
                .message("Product saved successfully")
                .build();
    }

    @Override
    public Response updateProduct(ProductDTO productDTO, MultipartFile imageFile) {
        //check if product exists
        Product existingProduct = productRepository.findById(productDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));
        //check if image exists
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            existingProduct.setImageUrl(imagePath);
        }
        //check if category exists and greater and 0 (means valid)
        if (productDTO.getCategoryId() != null && productDTO.getCategoryId() > 0) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            existingProduct.setCategory(category);
        }
        //check if product fields are has changed
        if(productDTO.getName() != null && !productDTO.getName().isBlank()) {
            existingProduct.setName(productDTO.getName());
        }
        if(productDTO.getSku() != null && !productDTO.getSku().isBlank()) {
            existingProduct.setSku(productDTO.getSku());
        }
        if(productDTO.getDescription() != null && !productDTO.getDescription().isBlank()) {
            existingProduct.setDescription(productDTO.getDescription());
        }
        if(productDTO.getPrice() != null && productDTO.getPrice().compareTo(BigDecimal.ZERO) >= 0) {
            existingProduct.setPrice(productDTO.getPrice());
        }
        if(productDTO.getStockQuantity() != null && productDTO.getStockQuantity() >= 0) {
            existingProduct.setStockQuantity(productDTO.getStockQuantity());
        }
        //update the product
        productRepository.save(existingProduct);

        return Response.builder()
                .status(200)
                .message("Product updated successfully")
                .build();
    }

    @Override
    public Response getAllProducts() {

        List<Product> productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<ProductDTO> productDTOList = modelMapper.map(productList, new TypeToken<List<ProductDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("Products found")
                .products(productDTOList)
                .build();
    }

    @Override
    public Response getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        return Response.builder()
                .status(200)
                .message("Product found")
                .product(modelMapper.map(product, ProductDTO.class))
                .build();
    }

    @Override
    public Response deleteProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        productRepository.deleteById(id);
        return Response.builder()
                .status(200)
                .message("Product deleted successfully")
                .build();
    }

    @Override
    public Response searchProduct(String input) {
        List<Product> productList = productRepository.findByNameContainingOrDescriptionContaining(input, input);
        if(productList.isEmpty()){
            throw new NotFoundException("Product not found");
        }
        List<ProductDTO> productDTOList = modelMapper.map(productList, new TypeToken<List<ProductDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("Products found")
                .products(productDTOList)
                .build();
    }

    private String saveImage(MultipartFile imageFile) {
        //validate image and check if it is greater than 1GIB
        //!imageFile.getOriginalFilename().startsWith("image/") ||
        if (imageFile.getSize() > 1024 * 1024 * 1024) {
            throw new IllegalArgumentException("Only image files under 1GIB is allowed");
        }
        //create the directory if it doesn't exists
        File file = new File(FRONTEND_IMAGE_DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
            log.info("Image directory created: {}", file.getAbsolutePath());
        }
        //generate unique file name for the image
        String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

        //geet the absolute path of the image
        String imagePath = FRONTEND_IMAGE_DIRECTORY + uniqueFileName;
        try{
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile); //writing the image to this folder with an unique name
        }catch (IOException e){
            throw new IllegalArgumentException("Error in saving image file: "+e.getMessage());
        }
        return "products/"+ uniqueFileName;
    }
}
