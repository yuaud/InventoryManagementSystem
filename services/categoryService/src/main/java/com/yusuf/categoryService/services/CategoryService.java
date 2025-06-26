package com.yusuf.categoryService.services;

import com.yusuf.categoryService.dtos.CategoryDTO;
import com.yusuf.categoryService.dtos.Response;

public interface CategoryService {

    Response createCategory(CategoryDTO categoryDTO);

    Response getAllCategories();

    Response getCategoryById(Long id);

    Response updateCategory(Long id, CategoryDTO categoryDTO);

    Response deleteCategory(Long id);
}
