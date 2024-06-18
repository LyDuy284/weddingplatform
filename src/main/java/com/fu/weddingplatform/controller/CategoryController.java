package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.category.CategorySuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.request.category.CreateCategoryDTO;
import com.fu.weddingplatform.service.CategoryService;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.category.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("category")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("create")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SERVICE_SUPPLIER)
    public ResponseEntity<?> getAllCouple(@RequestBody CreateCategoryDTO createDTO){
        ResponseDTO<CategoryResponse> responseDTO = new ResponseDTO<>();
        CategoryResponse data = categoryService.createCategory(createDTO);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(CategorySuccessMessage.CREATE);
        responseDTO.setData(data);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
