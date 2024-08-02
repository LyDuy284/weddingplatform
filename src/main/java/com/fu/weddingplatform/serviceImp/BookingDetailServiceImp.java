package com.fu.weddingplatform.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.bookingDetail.BookingDetailErrorMessage;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailStatus;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.BookingHistory;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingDetailRepository;
import com.fu.weddingplatform.repository.BookingHistoryRepository;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.service.BookingDetailService;
import com.fu.weddingplatform.utils.Utils;

@Service
public class BookingDetailServiceImp implements BookingDetailService {

  @Autowired
  private BookingDetailRepository bookingDetailRepository;

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private BookingHistoryRepository bookingHistoryRepository;

  @Override
  public BookingDetail updateBookingServiceStatus(String bookingDetailId, String status) {

    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    bookingDetail.setStatus(status);
    bookingDetailRepository.save(bookingDetail);

    BookingHistory bookingHistory = BookingHistory.builder()
        .bookingDetail(bookingDetail)
        .status(status)
        .createdAt(Utils.formatVNDatetimeNow())
        .build();

    bookingHistoryRepository.save(bookingHistory);

    return bookingDetail;
  }

  @Override
  public BookingDetail confirmBookingService(String bookingDetailId) {

    return updateBookingServiceStatus(bookingDetailId, BookingDetailStatus.CONFIRM);

  }

  @Override
  public BookingDetail rejectBookingService(String bookingDetailId) {
    return updateBookingServiceStatus(bookingDetailId, BookingDetailStatus.REJECT);

  }

  @Override
  public BookingDetail cancleBookingService(String bookingDetailId) {
    return updateBookingServiceStatus(bookingDetailId, BookingDetailStatus.CANCEL);

  }

  @Override
  public BookingDetail doneBookingService(String bookingDetailId) {
    return updateBookingServiceStatus(bookingDetailId, BookingDetailStatus.DONE);

  }

  @Override
  public BookingDetail completeBookingService(String bookingDetailId) {
    return updateBookingServiceStatus(bookingDetailId, BookingDetailStatus.COMPLETED);

  }

  @Override
  public BookingDetail processingBookingService(String bookingDetailId) {
    return updateBookingServiceStatus(bookingDetailId, BookingDetailStatus.ON_PROCESSING);

  }

}
