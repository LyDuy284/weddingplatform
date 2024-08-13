package com.fu.weddingplatform.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
@CrossOrigin("*")
public class SentEmailController {

  // @GetMapping("sent")
  // public ResponseEntity<?> sentEmail(@RequestParam String bookingDetailId)
  // throws MessagingException {
  // ResponseDTO<String> response = new ResponseDTO<>();
  // BookingDetail bookingDetail =
  // bookingDetailRepository.findById(bookingDetailId).orElseThrow(
  // () -> new ErrorException("null"));
  // sentEmailService.sentRejectBooking(bookingDetail);
  // response.setData("sent");
  // response.setStatus(ResponseStatusDTO.SUCCESS);
  // response.setMessage(BookingSuccessMessage.GET_ALL_BY_SUPPLIER);
  // return new ResponseEntity<>(response, HttpStatus.OK);
  // }
}
