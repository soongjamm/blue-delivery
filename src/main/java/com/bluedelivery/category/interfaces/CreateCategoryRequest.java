package com.bluedelivery.category.interfaces;

import javax.validation.constraints.Pattern;

import com.bluedelivery.category.application.CreateCategoryParam;
import com.bluedelivery.common.RegexConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {
    @Pattern(regexp = RegexConstants.CATEGORY_NAME)
    private String name;
    
    public CreateCategoryParam toParam() {
        return new CreateCategoryParam(name);
    }
}
