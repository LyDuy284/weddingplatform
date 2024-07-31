package com.fu.weddingplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.weddingplatform.constant.promotion.PromotionSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.request.promotion.CreatePromotionDTO;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.promotion.PromotionByServiceResponse;
import com.fu.weddingplatform.response.promotion.PromotionBySupplierResponse;
import com.fu.weddingplatform.response.promotion.PromotionResponse;
import com.fu.weddingplatform.service.PromotionService;

@RestController
@RequestMapping("promotion")
@CrossOrigin("*")
public class PromotionController {

  @Autowired
  private PromotionService promotionService;

  @PostMapping("create")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SERVICE_SUPPLIER)
  public ResponseEntity<?> createService(@Validated @RequestBody CreatePromotionDTO createDTO) {
    ResponseDTO<PromotionResponse> responseDTO = new ResponseDTO<>();
    PromotionResponse data = promotionService.createPromotion(createDTO);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    responseDTO.setMessage(PromotionSuccessMessage.CREATE);
    responseDTO.setData(data);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("getById/{id}")
  public ResponseEntity<?> getById(@RequestParam String id) {
    PromotionResponse response = promotionService.getPromotionById(id);
    ResponseDTO<PromotionResponse> responseDTO = new ResponseDTO<>();
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    responseDTO.setMessage(PromotionSuccessMessage.GET_BY_ID);
    responseDTO.setData(response);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("getPromotionBySupplier/{id}")
  public ResponseEntity<?> getPromotionBySupplier(@RequestParam String supplierId) {
    List<PromotionBySupplierResponse> response = promotionService.getPromotionBySupplier(supplierId);
    ListResponseDTO<PromotionBySupplierResponse> responseDTO = new ListResponseDTO<>();
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    responseDTO.setMessage(PromotionSuccessMessage.GET_ALL_BY_SUPPLIER);
    responseDTO.setData(response);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("getPromotionByService/{id}")
  public ResponseEntity<?> getPromotionByService(@RequestParam String id,
      @RequestParam(defaultValue = "0") int pageNo,
      @RequestParam(defaultValue = "10") int pageSize) {
    List<PromotionByServiceResponse> response = promotionService.getPromotionByService(id, pageNo, pageSize);
    ListResponseDTO<PromotionByServiceResponse> responseDTO = new ListResponseDTO<>();
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    responseDTO.setMessage(PromotionSuccessMessage.GET_ALL_BY_SERVICE);
    responseDTO.setData(response);
    return ResponseEntity.ok().body(responseDTO);
  }
}
