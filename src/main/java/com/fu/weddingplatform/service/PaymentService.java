package com.fu.weddingplatform.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fu.weddingplatform.request.payment.CreatePaymentDTO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PaymentService {
    String requestPaymentVNP(HttpServletRequest req, HttpServletResponse resp, CreatePaymentDTO paymentRequest) throws JsonProcessingException;
    void responsePaymentVNP(HttpServletRequest req, HttpServletResponse response) throws IOException;
}
