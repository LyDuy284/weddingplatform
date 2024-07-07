package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.category.CategorySuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.constant.service.ServiceSuccessMessage;
import com.fu.weddingplatform.request.service.CreateServiceDTO;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.category.CategoryResponse;
import com.fu.weddingplatform.response.service.ServiceResponse;
import com.fu.weddingplatform.service.ServiceService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("service")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ServiceController {
    @Autowired
    ServiceService service;

    @PostMapping("create")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SERVICE_SUPPLIER)
    public ResponseEntity<?> createService(@Validated @RequestBody CreateServiceDTO createDTO) {
        ResponseDTO<ServiceResponse> responseDTO = new ResponseDTO<>();
        ServiceResponse data = service.createService(createDTO);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSuccessMessage.CREATE);
        responseDTO.setData(data);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("getAllServices")
    public ResponseEntity<?> getAllServices(@RequestParam(defaultValue = "0") int pageSize,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAscending) {
        List<ServiceResponse> serviceResponses = service.getAllServices(pageSize, size, sortBy,
                isAscending);
        ListResponseDTO<ServiceResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSuccessMessage.GET_ALL);
        responseDTO.setData(serviceResponses);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<?> getById(@RequestParam String id) {
        ServiceResponse serviceResponses = service.getServiceById(id);
        ResponseDTO<ServiceResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSuccessMessage.GET_BY_ID);
        responseDTO.setData(serviceResponses);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("getAllActiveServices")
    public ResponseEntity<?> getAllActiveServices(@RequestParam(defaultValue = "0") int pageSize,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAscending) {
        List<ServiceResponse> serviceResponses = service.getAllActivateServices(pageSize, size, sortBy,
                isAscending);
        ListResponseDTO<ServiceResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSuccessMessage.GET_ALL);
        responseDTO.setData(serviceResponses);
        return ResponseEntity.ok().body(responseDTO);
    }
}
