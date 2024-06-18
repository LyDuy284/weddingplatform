package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.entity.Category;
import com.fu.weddingplatform.repository.CategoryRepository;
import com.fu.weddingplatform.request.category.CreateCategoryDTO;
import com.fu.weddingplatform.service.CategoryService;
import com.fu.weddingplatform.response.category.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}
