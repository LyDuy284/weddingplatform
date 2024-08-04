package com.fu.weddingplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fu.weddingplatform.constant.rating.RatingSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.serviceSupplier.ServiceSupplierSuccessMessage;
import com.fu.weddingplatform.request.serviceSupplier.CreateServiceSupplier;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import com.fu.weddingplatform.service.ServiceSupplierService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("service-supplier")
@CrossOrigin("*")
public class ServiceSupplierController {
    @Autowired
    private ServiceSupplierService serviceSupplierService;

    @PostMapping("create")
    public ResponseEntity<?> createRating(@Validated @RequestBody CreateServiceSupplier request) {
        ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService.createServiceSupplier(request);
        ResponseDTO<ServiceSupplierResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSupplierSuccessMessage.CREATE);
        responseDTO.setData(serviceSupplierResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("getById")
    public ResponseEntity<?> getMethodName(@RequestParam String id) {
        ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService.getServiceSupplierByID(id);
        ResponseDTO<ServiceSupplierResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSupplierSuccessMessage.GET_BY_ID);
        responseDTO.setData(serviceSupplierResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
