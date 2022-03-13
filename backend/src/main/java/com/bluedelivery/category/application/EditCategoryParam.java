package com.bluedelivery.category.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class EditCategoryParam {
    private final Long id;
    private final String name;
}
