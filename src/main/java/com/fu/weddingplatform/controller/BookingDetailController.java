package com.fu.weddingplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.weddingplatform.constant.booking.BookingSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.response.ResponseDTO;
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
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SERVICE_SUPPLIER)
  public ResponseEntity<?> confirmBookingDetailById(@RequestParam String id) {
    BookingDetail data = bookingDetailService.confirmBookingService(id);
    ResponseDTO<BookingDetail> response = new ResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.CONFIRM);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("reject")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SERVICE_SUPPLIER)
  public ResponseEntity<?> rejectBookingDetailById(@RequestParam String id) {
    BookingDetail data = bookingDetailService.rejectBookingService(id);
    ResponseDTO<BookingDetail> response = new ResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.REJECT);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("done")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SERVICE_SUPPLIER)
  public ResponseEntity<?> doneBookingDetailById(@RequestParam String id) {
    BookingDetail data = bookingDetailService.doneBookingService(id);
    ResponseDTO<BookingDetail> response = new ResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.DONE);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("process")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SERVICE_SUPPLIER)
  public ResponseEntity<?> processBookingDetailById(@RequestParam String id) {
    BookingDetail data = bookingDetailService.processingBookingService(id);
    ResponseDTO<BookingDetail> response = new ResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.CONFIRM);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("cancle")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_COUPLE)
  public ResponseEntity<?> cancleBookingDetailById(@RequestParam String id) {
    BookingDetail data = bookingDetailService.cancleBookingService(id);
    ResponseDTO<BookingDetail> response = new ResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.CANCLE);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
