package com.fu.weddingplatform.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.bookingDetail.BookingDetailErrorMessage;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingDetailRepository;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.service.BookingDetailService;

@Service
public class BookingDetailServiceImp implements BookingDetailService {

  @Autowired
  private BookingDetailRepository bookingDetailRepository;

  @Autowired
  private BookingRepository bookingRepository;

  @Override
  public BookingDetail updateBookingServiceStatus(String bookingDetailId, String status) {

    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    bookingDetail.setStatus(status);
    bookingDetailRepository.save(bookingDetail);
    return bookingDetail;
  }

  @Override
  public BookingDetail confirmBookingService(String bookingDetailId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'confirmBookingService'");
  }

  @Override
  public BookingDetail rejectBookingService(String bookingDetailId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'rejectBookingService'");
  }

  @Override
  public BookingDetail cancleBookingService(String bookingDetailId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'cancleBookingService'");
  }

  @Override
  public BookingDetail doneBookingService(String bookingDetailId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'doneBookingService'");
  }

  @Override
  public BookingDetail completeBookingService(String bookingDetailId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'completeBookingService'");
  }

  @Override
  public BookingDetail processingBookingService(String bookingDetailId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'processingBookingService'");
  }

}
