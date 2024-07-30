package com.fu.weddingplatform.service;

import java.util.List;

import com.fu.weddingplatform.request.category.CreateCategoryDTO;
import com.fu.weddingplatform.response.category.CategoryResponse;

public interface CategoryService {

    public CategoryResponse createCategory(CreateCategoryDTO createDTO);

    public List<CategoryResponse> getAllCategories(int pageNo, int pageSize, String sortBy, boolean isAscending);

    public CategoryResponse getCategoryById(String categoryId);

    public List<String> getCategoryName();

}
