package com.fu.weddingplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.weddingplatform.constant.booking.BookingSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.booking.BookingDetailResponse;
import com.fu.weddingplatform.response.bookingHIstory.BookingDetailHistoryResponse;
import com.fu.weddingplatform.service.BookingDetailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("booking-service")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BookingDetailController {
  @Autowired
  BookingDetailService bookingDetailService;

  @PutMapping("confirm")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SUPPLIER)
  public ResponseEntity<?> confirmBookingDetailById(@RequestParam String id) {
    BookingDetailResponse data = bookingDetailService.confirmBookingDetail(id);
    ResponseDTO<BookingDetailResponse> response = new ResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.CONFIRM);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("reject")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SUPPLIER)
  public ResponseEntity<?> rejectBookingDetailById(@RequestParam String id) {
    BookingDetailResponse data = bookingDetailService.rejectBookingDetail(id);
    ResponseDTO<BookingDetailResponse> response = new ResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.REJECT);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("complete")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SUPPLIER)
  public ResponseEntity<?> completeBookingDetailById(@RequestParam String id) {
    BookingDetailResponse data = bookingDetailService.completeBookingDetail(id);
    ResponseDTO<BookingDetailResponse> response = new ResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.COMPLETED);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("cancle")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_COUPLE)
  public ResponseEntity<?> cancleBookingDetailById(@RequestParam String id) {
    BookingDetailResponse data = bookingDetailService.cancleBookingDetail(id);
    ResponseDTO<BookingDetailResponse> response = new ResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.CANCLE);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("getBookingDetailHistory")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_COUPLE_SUPPLIER)
  public ResponseEntity<?> getBookingDetailHistory(@RequestParam String id) {
    List<BookingDetailHistoryResponse> data = bookingDetailService.getBookingDetailHistoryById(id);
    ListResponseDTO<BookingDetailHistoryResponse> response = new ListResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.GET_BY_ID);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
