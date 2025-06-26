package com.yusuf.categoryService.services.impl;


import com.yusuf.categoryService.dtos.CategoryDTO;
import com.yusuf.categoryService.dtos.Response;
import com.yusuf.categoryService.exceptions.NotFoundException;
import com.yusuf.categoryService.models.Category;
import com.yusuf.categoryService.repositories.CategoryRepository;
import com.yusuf.categoryService.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response createCategory(CategoryDTO categoryDTO) {
        Category categoryToSave = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(categoryToSave);

        return Response.builder()
                .status(200)
                .message("Category created")
                .build();
    }

    @Override
    public Response getAllCategories() {
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        categories.forEach(category -> category.setProducts(null));
        List<CategoryDTO> categoryDTOList = modelMapper.map(categories, new TypeToken<List<CategoryDTO>>() {}.getType()); //burada hata var

        return Response.builder()
                .status(200)
                .message("All categories successfully retrieved")
                .categories(categoryDTOList)
                .build();
    }

    @Override
    public Response getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));
        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

        return Response.builder()
                .status(200)
                .message("Category successfully retrieved")
                .category(categoryDTO)
                .build();
    }

    @Override
    public Response updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));
        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);

        return Response.builder()
                .status(200)
                .message("Category successfully updated")
                .build();
    }

    @Override
    public Response deleteCategory(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        categoryRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Category successfully deleted")
                .build();
    }
}
