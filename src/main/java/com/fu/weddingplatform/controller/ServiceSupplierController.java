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

import com.fu.weddingplatform.constant.booking.BookingSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.constant.serviceSupplier.ServiceSupplierSuccessMessage;
import com.fu.weddingplatform.request.serviceSupplier.CreateServiceSupplier;
import com.fu.weddingplatform.request.serviceSupplier.UpdateServiceSupplier;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.booking.BookingDetailResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierBySupplierReponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierFilterResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import com.fu.weddingplatform.service.ServiceSupplierService;

@RestController
@RequestMapping("service-supplier")
@CrossOrigin("*")
public class ServiceSupplierController {
    @Autowired
    private ServiceSupplierService serviceSupplierService;

    @PostMapping("create")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SUPPLIER)
    public ResponseEntity<?> createServiceSupplier(@Validated @RequestBody CreateServiceSupplier request) {
        ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService.createServiceSupplier(request);
        ResponseDTO<ServiceSupplierResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSupplierSuccessMessage.CREATE);
        responseDTO.setData(serviceSupplierResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("update")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SUPPLIER)
    public ResponseEntity<?> updateServiceSupplier(@Validated @RequestBody UpdateServiceSupplier request) {
        ServiceSupplierFilterResponse data = serviceSupplierService.updateServiceSupplier(request);
        ResponseDTO<ServiceSupplierFilterResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSupplierSuccessMessage.UPDATE);
        responseDTO.setData(data);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("getById")
    public ResponseEntity<?> getById(@RequestParam String id) {
        ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService.getServiceSupplierByID(id);
        ResponseDTO<ServiceSupplierResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSupplierSuccessMessage.GET_BY_ID);
        responseDTO.setData(serviceSupplierResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("getBySuppler")
    public ResponseEntity<?> getBySupplier(@RequestParam String supplierId) {
        List<ServiceSupplierBySupplierReponse> serviceSupplierBySupplierReponse = serviceSupplierService
                .getBySupplier(supplierId);
        ListResponseDTO<ServiceSupplierBySupplierReponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSupplierSuccessMessage.GET_BY_ID);
        responseDTO.setData(serviceSupplierBySupplierReponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("filter")
    public ResponseEntity<?> filterServiceSupplier(@RequestParam(defaultValue = "") String categoryId,
            @RequestParam(defaultValue = "") String serviceId,
            @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "0") int minPrice,
            @RequestParam(defaultValue = "0") int maxPrice,
            @RequestParam(defaultValue = "") String supplierId) {
        List<ServiceSupplierFilterResponse> serviceSupplierBySupplierReponse = serviceSupplierService
                .filterByService(categoryId, serviceId, type, minPrice, maxPrice, supplierId);
        ListResponseDTO<ServiceSupplierFilterResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSupplierSuccessMessage.FILTER);
        responseDTO.setData(serviceSupplierBySupplierReponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("activate")
    @PreAuthorize(RolePreAuthorize.ROLE_SUPPLIER)
    public ResponseEntity<?> activeServiceSupplier(@RequestParam String id) {
        ServiceSupplierResponse data = serviceSupplierService.activeServiceSupplier(id);
        ResponseDTO<ServiceSupplierResponse> response = new ResponseDTO<>();
        response.setData(data);
        response.setStatus(ResponseStatusDTO.SUCCESS);
        response.setMessage(ServiceSupplierSuccessMessage.ACTIVATE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
