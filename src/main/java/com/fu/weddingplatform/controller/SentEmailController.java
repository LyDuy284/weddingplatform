package com.fu.weddingplatform.controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.weddingplatform.constant.booking.BookingSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.service.SentEmailService;

@RestController
@RequestMapping("email")
@CrossOrigin("*")
public class SentEmailController {

  @Autowired
  private SentEmailService sentEmailService;

  @Autowired
  private BookingRepository bookingRepository;

  @GetMapping("sent")
  public ResponseEntity<?> sentEmail(@RequestParam String bookingId) throws MessagingException {
    ResponseDTO<String> response = new ResponseDTO<>();
    Booking booking = bookingRepository.findById(bookingId).orElseThrow(
        () -> new ErrorException("null"));
    sentEmailService.sentBookingForCouple(booking);
    response.setData("sent");
    response.setStatus(ResponseStatusDTO.SUCCESS);
    response.setMessage(BookingSuccessMessage.GET_ALL_BY_SUPPLIER);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
