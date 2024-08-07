package com.fu.weddingplatform.controller;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.fu.weddingplatform.constant.booking.BookingSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.request.booking.CreateBookingDTO;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.booking.BookingDetailBySupplierResponse;
import com.fu.weddingplatform.response.booking.BookingResponse;
import com.fu.weddingplatform.response.bookingHIstory.BookingHistoryResponse;
import com.fu.weddingplatform.service.BookingService;

@RestController
@RequestMapping("booking")
@CrossOrigin("*")
public class BookingController {
  @Autowired
  BookingService bookingService;

  @PostMapping("create")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_COUPLE)
  public ResponseEntity<?> createBooking(@Validated @RequestBody CreateBookingDTO createBookingDTO) {
    BookingResponse data = bookingService.createBooking(createBookingDTO);
    ResponseDTO<BookingResponse> responseDTO = new ResponseDTO<>();
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    responseDTO.setMessage(BookingSuccessMessage.CREATE);
    responseDTO.setData(data);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }

  @GetMapping("getBySupplier")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_SUPPLIER)
  public ResponseEntity<?> getBySupplier(@RequestParam String supplierId) {
    List<BookingDetailBySupplierResponse> data = bookingService.getAllBookingBySupplier(supplierId);
    ListResponseDTO<BookingDetailBySupplierResponse> response = new ListResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.GET_ALL_BY_SUPPLIER);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("getByCouple")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_COUPLE)
  public ResponseEntity<?> getByCouple(@RequestParam String coupleId) {
    List<BookingResponse> data = bookingService.getAllBookingByCouple(coupleId);
    ListResponseDTO<BookingResponse> response = new ListResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.GET_ALL_BY_COUPLE);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("getById")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_COUPLE_SUPPLIER)
  public ResponseEntity<?> getBookingById(@RequestParam String id) {
    BookingResponse data = bookingService.getBookingById(id);
    ResponseDTO<BookingResponse> response = new ResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.GET_BY_ID);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("getBookingHistoryById")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_COUPLE_SUPPLIER)
  public ResponseEntity<?> getBookingHistoryById(@RequestParam String id) {
    List<BookingHistoryResponse> data = bookingService.getBookingHistoryById(id);
    ListResponseDTO<BookingHistoryResponse> response = new ListResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.GET_BY_ID);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping("cancle")
  @PreAuthorize(RolePreAuthorize.ROLE_ADMIN_COUPLE)
  public ResponseEntity<?> cancleBookingById(@RequestParam String id) {
    BookingResponse data = bookingService.cancelBooking(id);
    ResponseDTO<BookingResponse> response = new ResponseDTO<>();
    response.setData(data);
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.CANCLE);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
