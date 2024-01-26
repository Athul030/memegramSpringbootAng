package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.CategoryDTO;
import com.athul.memegramspring.exceptions.ApiResponse;
import com.athul.memegramspring.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    //create
    @PostMapping("/")
    public ResponseEntity<CategoryDTO> createCategory( @Valid  @RequestBody CategoryDTO categoryDTO){
        CategoryDTO createdCategory=categoryService.createCategory(categoryDTO);
        return new ResponseEntity<CategoryDTO>(createdCategory, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{catId}")
    public ResponseEntity<CategoryDTO> updateCategory( @Valid @RequestBody CategoryDTO categoryDTO,
                                                      @PathVariable Integer catId){
        CategoryDTO updatedCategory=categoryService.updateCategory(categoryDTO,catId);
        return new ResponseEntity<CategoryDTO>(updatedCategory, HttpStatus.CREATED);
    }

    //delete
    @DeleteMapping("/{catId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer catId){
        categoryService.deleteCategory(catId);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Category is deleted successfully",true), HttpStatus.OK);
    }
    //get
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Integer catId){
        CategoryDTO categoryDTO = categoryService.getCategory(catId);
        return new ResponseEntity<CategoryDTO>(categoryDTO,HttpStatus.OK);
    }


    //get All
    @GetMapping("/")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        List<CategoryDTO> listCategory = categoryService.getAllCategories();
        return new ResponseEntity<List<CategoryDTO>>(listCategory,HttpStatus.OK);
    }
}
