package com.athul.memegramspring.service;

import com.athul.memegramspring.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    //create
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    //update
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Integer categoryId);

    //delete
    void deleteCategory(Integer categoryId);

    //get
    CategoryDTO getCategory(Integer categoryId);

    //getAll
    List<CategoryDTO> getAllCategories();
}
