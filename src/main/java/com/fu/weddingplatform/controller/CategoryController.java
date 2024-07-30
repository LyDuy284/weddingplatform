package com.fu.weddingplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.weddingplatform.constant.category.CategorySuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.request.category.CreateCategoryDTO;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.category.CategoryResponse;
import com.fu.weddingplatform.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("category")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("create")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_STAFF)
    public ResponseEntity<?> createServiceByAdminOrSupplier(@RequestBody CreateCategoryDTO createDTO) {
        ResponseDTO<CategoryResponse> responseDTO = new ResponseDTO<>();
        CategoryResponse data = categoryService.createCategory(createDTO);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(CategorySuccessMessage.CREATE);
        responseDTO.setData(data);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("getAllCategory")
    public ResponseEntity<?> getAllCategory(@RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAscending) {
        List<CategoryResponse> categoryResponses = categoryService.getAllCategories(pageNo, pageSize, sortBy,
                isAscending);
        ListResponseDTO<CategoryResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(CategorySuccessMessage.GET_ALL);
        responseDTO.setData(categoryResponses);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<?> getById(@RequestParam String id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        ResponseDTO<CategoryResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(CategorySuccessMessage.GET_BY_ID);
        responseDTO.setData(category);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("getCategoryName/")
    public ResponseEntity<?> getCategoryName() {
        List<String> category = categoryService.getCategoryName();
        ListResponseDTO<String> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(CategorySuccessMessage.GET_ALL_NAME);
        responseDTO.setData(category);
        return ResponseEntity.ok().body(responseDTO);
    }
}
