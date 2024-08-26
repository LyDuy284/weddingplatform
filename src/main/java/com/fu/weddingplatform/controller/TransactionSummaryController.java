package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.comboService.ComboSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.constant.transactionSummary.TransactionSummarySuccessMessage;
import com.fu.weddingplatform.entity.TransactionSummary;
import com.fu.weddingplatform.request.transactionSummary.TransactionSummaryResponse;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.statistic.DashboardStatistic;
import com.fu.weddingplatform.response.transaction.TransactionResponse;
import com.fu.weddingplatform.service.TransactionService;
import com.fu.weddingplatform.service.TransactionSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transaction-summary")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TransactionSummaryController {

    @Autowired
    TransactionSummaryService transactionSummaryService;

    @Autowired
    TransactionService transactionService;

    @GetMapping("staffStatistic")
    @PreAuthorize(RolePreAuthorize.ROLE_STAFF)
    public ResponseEntity<?> getStaffDashboardStatistic(@RequestParam(required = false, defaultValue = "0") Integer month,
                                                        @RequestParam(required = false, defaultValue = "0") Integer quarter,
                                                        @RequestParam(required = false, defaultValue = "0") Integer year) {
        DashboardStatistic dashboardStatistic = transactionSummaryService.getStaffDashboardStatistic(month, quarter, year);
        ResponseDTO<DashboardStatistic> responseDTO = new ResponseDTO<>();
        responseDTO.setData(dashboardStatistic);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(TransactionSummarySuccessMessage.GET_STATISTIC);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("supplierStatistic")
    @PreAuthorize(RolePreAuthorize.ROLE_SUPPLIER)
    public ResponseEntity<?> getSuppDashboardStatistic(@RequestParam(required = false, defaultValue = "0") Integer month,
                                                       @RequestParam(required = false, defaultValue = "0") Integer quarter,
                                                       @RequestParam(required = false, defaultValue = "0") Integer year,
                                                       @RequestParam String supplierId) {
        DashboardStatistic dashboardStatistic = transactionService.getSupplierDashboardStatistic(month, quarter, year, supplierId);
        ResponseDTO<DashboardStatistic> responseDTO = new ResponseDTO<>();
        responseDTO.setData(dashboardStatistic);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(TransactionSummarySuccessMessage.GET_STATISTIC);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("detail")
    public ResponseEntity<?> getSuppDashboardStatistic(@RequestParam String bookingId) {
        TransactionSummaryResponse transactionSummaryResponse = transactionSummaryService.gettransactionSummary(bookingId);
        ResponseDTO<TransactionSummaryResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setData(transactionSummaryResponse);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(TransactionSummarySuccessMessage.GET);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
