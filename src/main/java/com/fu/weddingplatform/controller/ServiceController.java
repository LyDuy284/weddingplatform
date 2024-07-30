package com.fu.weddingplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.constant.service.ServiceSuccessMessage;
import com.fu.weddingplatform.request.service.CreateServiceDTO;
import com.fu.weddingplatform.request.service.UpdateServiceDTO;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.service.ServiceByCategoryAndSupplierResponse;
import com.fu.weddingplatform.response.service.ServiceByCategoryResponse;
import com.fu.weddingplatform.response.service.ServiceBySupplierResponse;
import com.fu.weddingplatform.response.service.ServiceResponse;
import com.fu.weddingplatform.service.ServiceService;

@RestController
@RequestMapping("service")
@CrossOrigin("*")
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

    @PutMapping("update")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SERVICE_SUPPLIER)
    public ResponseEntity<?> updateService(@Validated @RequestBody UpdateServiceDTO updateDTO) {
        ResponseDTO<ServiceResponse> responseDTO = new ResponseDTO<>();
        ServiceResponse data = service.updateService(updateDTO);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSuccessMessage.UPDATE);
        responseDTO.setData(data);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("getAllServices")
    public ResponseEntity<?> getAllServices(@RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAscending) {
        List<ServiceResponse> serviceResponses = service.getAllServices(pageNo, pageSize, sortBy, isAscending);
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
    public ResponseEntity<?> getAllActiveServices(@RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAscending) {
        List<ServiceResponse> serviceResponses = service.getAllActivateServices(pageNo, pageSize, sortBy, isAscending);
        ListResponseDTO<ServiceResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSuccessMessage.GET_ALL);
        responseDTO.setData(serviceResponses);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("getAllServicesBySupplier/{id}")
    public ResponseEntity<?> getAllServicesBySupplier(@RequestParam String supplierId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAscending) {
        List<ServiceBySupplierResponse> serviceResponses = service.getAllServicesBySupplier(supplierId, pageNo,
                pageSize, sortBy, isAscending);
        ListResponseDTO<ServiceBySupplierResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSuccessMessage.GET_ALL_BY_SUPPLIER);
        responseDTO.setData(serviceResponses);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("getAllServicesByCategoryAndSupplier/")
    public ResponseEntity<?> getAllServicesByCategoryAndSupplier(@RequestParam String categoryId,
            @RequestParam String supplierId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAscending) {
        List<ServiceByCategoryAndSupplierResponse> serviceResponses = service.getAllServicesByCategoryAndSupplier(
                categoryId,
                supplierId,
                pageNo,
                pageSize, sortBy, isAscending);
        ListResponseDTO<ServiceByCategoryAndSupplierResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSuccessMessage.GET_ALL_BY_CATEGORY_AND_SUPPLIER);
        responseDTO.setData(serviceResponses);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("getAllServicesByCategory/")
    public ResponseEntity<?> getAllServicesByCategory(@RequestParam String categoryId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAscending) {
        List<ServiceByCategoryResponse> serviceResponses = service.getAllServicesByCategory(
                categoryId,
                pageNo,
                pageSize, sortBy, isAscending);
        ListResponseDTO<ServiceByCategoryResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSuccessMessage.GET_ALL_BY_CATEGORY);
        responseDTO.setData(serviceResponses);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("filterService/")
    public ResponseEntity<?> filterService(@RequestParam String categoryId,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "0") int minPrice,
            @RequestParam(defaultValue = "0") int maxPrice) {
        List<ServiceResponse> serviceResponses = service.filterService(categoryId, type, minPrice, maxPrice);
        ListResponseDTO<ServiceResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSuccessMessage.GET_ALL_BY_CATEGORY);
        responseDTO.setData(serviceResponses);
        return ResponseEntity.ok().body(responseDTO);
    }

}
