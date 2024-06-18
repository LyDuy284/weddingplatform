package com.fu.weddingplatform.service;

import com.fu.weddingplatform.request.category.CreateCategoryDTO;
import com.fu.weddingplatform.response.category.CategoryResponse;

public interface CategoryService {

    public CategoryResponse createCategory(CreateCategoryDTO createDTO);

}
