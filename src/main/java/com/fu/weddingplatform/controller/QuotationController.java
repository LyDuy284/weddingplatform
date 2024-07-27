package com.fu.weddingplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.fu.weddingplatform.constant.quotation.QuotationSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.constant.service.ServiceSuccessMessage;
import com.fu.weddingplatform.request.quotation.CreateQuoteRequestDTO;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.quotation.QuotationByCoupleResponse;
import com.fu.weddingplatform.response.quotation.QuotationBySupplierResponse;
import com.fu.weddingplatform.response.quotation.QuotationResponse;
import com.fu.weddingplatform.response.service.ServiceResponse;
import com.fu.weddingplatform.service.QuotationService;

@RestController
@RequestMapping("quote-request")
@CrossOrigin("*")
public class QuotationController {

  @Autowired
  private QuotationService quotationService;

  @PostMapping("create")
  public ResponseEntity<?> createQuoteRequest(@Validated @RequestBody CreateQuoteRequestDTO createDTO) {
    ResponseDTO<QuotationResponse> responseDTO = new ResponseDTO<>();
    QuotationResponse data = quotationService.createQuoteResquest(createDTO);
    responseDTO.setData(data);
    responseDTO.setMessage(QuotationSuccessMessage.CREATE);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PutMapping("quoteService/{id}")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SERVICE_SUPPLIER)
  public ResponseEntity<?> putMethodName(@RequestParam String id, @RequestParam int price) {
    ResponseDTO<QuotationResponse> responseDTO = new ResponseDTO<>();
    QuotationResponse data = quotationService.quoteService(id, price);
    responseDTO.setData(data);
    responseDTO.setMessage(QuotationSuccessMessage.QUOTE_SERVICE);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("getQuoteRequestByCouple")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_COUPLE)
  public ResponseEntity<?> getQuoteRequestByCouple(@RequestParam String coupleId,
      @RequestParam(defaultValue = "0") int pageNo,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "true") boolean isAscending) {
    List<QuotationByCoupleResponse> quotationByCoupleResponses = quotationService.getQuoteRequestByCouple(coupleId,
        pageNo,
        pageSize, sortBy, isAscending);
    ListResponseDTO<QuotationByCoupleResponse> responseDTO = new ListResponseDTO<>();
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    responseDTO.setMessage(QuotationSuccessMessage.GET_ALL_BY_COUPLE);
    responseDTO.setData(quotationByCoupleResponses);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("getQuoteRequestBySupplier")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SERVICE_SUPPLIER)
  public ResponseEntity<?> getQuoteRequestBySupplier(@RequestParam String supplierId,
      @RequestParam(defaultValue = "0") int pageNo,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "true") boolean isAscending) {
    List<QuotationBySupplierResponse> quotationBySupplierResponses = quotationService.getQuoteRequestBySupplier(
        supplierId,
        pageNo, pageSize, sortBy, isAscending);
    ListResponseDTO<QuotationBySupplierResponse> responseDTO = new ListResponseDTO<>();
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    responseDTO.setMessage(ServiceSuccessMessage.GET_ALL);
    responseDTO.setData(quotationBySupplierResponses);
    return ResponseEntity.ok().body(responseDTO);
  }

}
