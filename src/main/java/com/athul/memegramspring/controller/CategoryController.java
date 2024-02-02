package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.CategoryDTO;
import com.athul.memegramspring.exceptions.ApiResponseCustom;
import com.athul.memegramspring.service.CategoryService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService categoryService;
    //create
    @PostMapping("/")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<CategoryDTO> createCategory( @Valid  @RequestBody CategoryDTO categoryDTO){
        CategoryDTO createdCategory=categoryService.createCategory(categoryDTO);
        return new ResponseEntity<CategoryDTO>(createdCategory, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{catId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404",description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<CategoryDTO> updateCategory( @Valid @RequestBody CategoryDTO categoryDTO,
                                                      @PathVariable Integer catId){
        CategoryDTO updatedCategory=categoryService.updateCategory(categoryDTO,catId);
        return new ResponseEntity<CategoryDTO>(updatedCategory, HttpStatus.CREATED);
    }

    //delete
    @DeleteMapping("/{catId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404",description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ApiResponseCustom> deleteCategory(@PathVariable Integer catId){
        categoryService.deleteCategory(catId);
        return new ResponseEntity<>(new ApiResponseCustom("Category is deleted successfully",true), HttpStatus.OK);
    }
    //get
    @GetMapping("/{catId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404",description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Integer catId){
        CategoryDTO categoryDTO = categoryService.getCategory(catId);
        return new ResponseEntity<CategoryDTO>(categoryDTO,HttpStatus.OK);
    }


    //get All
    @GetMapping("/")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        List<CategoryDTO> listCategory = categoryService.getAllCategories();
        return new ResponseEntity<List<CategoryDTO>>(listCategory,HttpStatus.OK);
    }
}
