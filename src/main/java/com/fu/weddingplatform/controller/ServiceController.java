package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.constant.service.ServiceSuccessMessage;
import com.fu.weddingplatform.request.service.CreateServiceDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.service.ServiceResponse;
import com.fu.weddingplatform.service.ServiceService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> getAllCouple(@Validated @RequestBody CreateServiceDTO createDTO){
        ResponseDTO<ServiceResponse> responseDTO = new ResponseDTO<>();
        ServiceResponse data = service.createService(createDTO);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ServiceSuccessMessage.CREATE);
        responseDTO.setData(data);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
