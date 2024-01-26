package com.athul.memegramspring.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {

    private Integer categoryId;
    @NotBlank
    private String categoryTitle;
    @NotBlank
    private String categoryDescription;
}
