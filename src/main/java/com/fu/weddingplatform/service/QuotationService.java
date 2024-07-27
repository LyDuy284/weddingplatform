package com.fu.weddingplatform.service;

import java.util.List;

import com.fu.weddingplatform.request.quotation.CreateQuoteRequestDTO;
import com.fu.weddingplatform.response.quotation.QuotationByCoupleResponse;
import com.fu.weddingplatform.response.quotation.QuotationBySupplierResponse;
import com.fu.weddingplatform.response.quotation.QuotationResponse;

public interface QuotationService {
  public QuotationResponse createQuoteResquest(CreateQuoteRequestDTO createDTO);

  public QuotationResponse quoteService(String quoteRequestId, int price);

  public List<QuotationByCoupleResponse> getQuoteRequestByCouple(String coupleId,
      int pageNo,
      int pageSize,
      String sortBy,
      boolean isAscending);

  public List<QuotationBySupplierResponse> getQuoteRequestBySupplier(String supplierId,
      int pageNo,
      int pageSize,
      String sortBy,
      boolean isAscending);
}
