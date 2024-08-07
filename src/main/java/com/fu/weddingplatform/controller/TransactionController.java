package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.comboService.ComboSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.combo.ComboResponse;
import com.fu.weddingplatform.response.transaction.TransactionResponse;
import com.fu.weddingplatform.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("transaction")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("getCoupleTransactionsHistoryByFilter")
    public ResponseEntity<?> getCoupleTransactionsHistoryByFilter(@RequestParam String coupleId,
                                                                  @RequestParam(required = false) String isDeposit,
                                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                                  @RequestParam(defaultValue = "true") boolean isAscending,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeTo,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrom) {
        List<TransactionResponse> transactionResponses = transactionService.getCoupleTransactionsHistoryByFilter(coupleId, pageNo, pageSize, sortBy, isAscending, timeFrom, timeTo, isDeposit);
        ListResponseDTO<TransactionResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setData(transactionResponses);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ComboSuccessMessage.GET);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("getBookingTransactionsHistoryByFilter")
    public ResponseEntity<?> getBookingTransactionsHistoryByFilter(@RequestParam String bookingId,
                                                                  @RequestParam(required = false) String isDeposit,
                                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                                  @RequestParam(defaultValue = "true") boolean isAscending,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeTo,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrom) {
        List<TransactionResponse> transactionResponses = transactionService.getBookingTransactionsHistoryByFilter(bookingId, pageNo, pageSize, sortBy, isAscending, timeFrom, timeTo, isDeposit);
        ListResponseDTO<TransactionResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setData(transactionResponses);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ComboSuccessMessage.GET);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable String id){
        TransactionResponse transactionResponse = transactionService.getTransactionById(id);
        ResponseDTO<TransactionResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setData(transactionResponse);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ComboSuccessMessage.GET);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
