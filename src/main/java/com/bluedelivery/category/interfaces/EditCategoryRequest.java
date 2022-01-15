package com.bluedelivery.category.interfaces;


import javax.validation.constraints.Pattern;

import com.bluedelivery.category.application.EditCategoryParam;
import com.bluedelivery.common.RegexConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditCategoryRequest {
    @Pattern(regexp = RegexConstants.CATEGORY_NAME)
    private String name;
    
    public EditCategoryParam toParam(Long id) {
        return new EditCategoryParam(id, this.name);
    }
}
