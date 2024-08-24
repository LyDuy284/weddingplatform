package com.fu.weddingplatform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fu.weddingplatform.constant.payment.PaymentSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.request.payment.CreatePaymentDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("payment")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @PostMapping(value = "request")
    public ResponseEntity<?> requestPayment(HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody CreatePaymentDTO paymentRequest) throws JsonProcessingException {
        String paymentURL = paymentService.requestPaymentVNP(request, response, paymentRequest);
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(PaymentSuccessMessage.GET_PAYMENT_URL);
        responseDTO.setData(paymentURL);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }

    @GetMapping(value = "call-back")
    public ResponseEntity<?> payment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        paymentService.responsePaymentVNP(request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
