package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.walletHistory.WalletHistorySuccessMessage;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.walletHistory.WalletHistoryResponse;
import com.fu.weddingplatform.service.WalletHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("wallet-history")
@CrossOrigin("*")
@RequiredArgsConstructor
public class WalletHistoryController {

    @Autowired
    WalletHistoryService walletHistoryService;

    @GetMapping("byFilter")
    public ResponseEntity<?> getWalletHistoryByFilter(@RequestParam String walletId,
                                                                  @RequestParam(required = false) String type,
                                                                  @RequestParam(defaultValue = "0") int pageNo,
                                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                                  @RequestParam(defaultValue = "true") boolean isAscending,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeTo,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrom){
        List<WalletHistoryResponse> walletHistoryResponses = walletHistoryService.getWalletHistory(walletId,  pageNo, pageSize, sortBy, isAscending, timeFrom,  timeTo, type);
        ListResponseDTO<WalletHistoryResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setData(walletHistoryResponses);
        responseDTO.setMessage(WalletHistorySuccessMessage.GET_LIST);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
