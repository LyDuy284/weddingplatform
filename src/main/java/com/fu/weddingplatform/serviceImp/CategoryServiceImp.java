package com.fu.weddingplatform.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.category.CategoryErrorMessage;
import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.entity.Category;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.CategoryRepository;
import com.fu.weddingplatform.request.category.CreateCategoryDTO;
import com.fu.weddingplatform.response.category.CategoryResponse;
import com.fu.weddingplatform.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryResponse createCategory(CreateCategoryDTO createDTO) {

        Category category = Category.builder()
                .categoryName(createDTO.getCategoryName())
                .status(Status.ACTIVATED)
                .build();
        Category categorySaved = categoryRepository.save(category);
        return modelMapper.map(categorySaved, CategoryResponse.class);
    }

    @Override
    public CategoryResponse getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ErrorException(CategoryErrorMessage.NOT_FOUND));

        CategoryResponse response = modelMapper.map(category, CategoryResponse.class);

        return response;
    }

    @Override
    public List<CategoryResponse> getAllCategories(int pageNo, int pageSize, String sortBy, boolean isAscending) {
        List<CategoryResponse> response = new ArrayList<>();
        Page<Category> categories;
        if (isAscending) {
            categories = categoryRepository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
        } else {
            categories = categoryRepository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
        }
        System.out.println(categories.getContent());
        if (categories.hasContent()) {
            for (Category category : categories) {
                response.add(modelMapper.map(category, CategoryResponse.class));
            }
        } else {
            throw new ErrorException(CoupleErrorMessage.EMPTY_COUPLE_LIST);
        }
        return response;
    }
}
