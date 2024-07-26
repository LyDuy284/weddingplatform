package com.fu.weddingplatform.service;

import com.fu.weddingplatform.request.quoteRequest.CreateQuoteRequestDTO;
import com.fu.weddingplatform.response.quoteResquest.QuoteResquestResponse;

public interface QuoteRequestService {
  public QuoteResquestResponse createQuoteResquest(CreateQuoteRequestDTO createDTO);
}
