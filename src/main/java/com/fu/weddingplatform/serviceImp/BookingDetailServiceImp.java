package com.fu.weddingplatform.serviceImp;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
  public BookingDetail updateBookingServiceStatus(BookingDetail bookingDetail, String status) {

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

    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    if (!(bookingDetail.getStatus().equalsIgnoreCase(BookingDetailStatus.WAITING))) {
      throw new ErrorException(BookingDetailErrorMessage.CONFIRM);
    }

    return updateBookingServiceStatus(bookingDetail, BookingDetailStatus.CONFIRM);

  }

  @Override
  public BookingDetail rejectBookingService(String bookingDetailId) {
    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    if (!(bookingDetail.getStatus().equalsIgnoreCase(BookingDetailStatus.WAITING))) {
      throw new ErrorException(BookingDetailErrorMessage.CONFIRM);
    }

    return updateBookingServiceStatus(bookingDetail, BookingDetailStatus.REJECT);

  }

  @Override
  public BookingDetail cancleBookingService(String bookingDetailId) {

    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    if (!(bookingDetail.getStatus().equalsIgnoreCase(BookingDetailStatus.WAITING))) {
      throw new ErrorException(BookingDetailErrorMessage.CONFIRM);
    }

    LocalDate completeDate = Utils.convertStringToLocalDate(bookingDetail.getCompletedDate());
    LocalDate currentDate = Utils.getCurrentDate();

    long daysBetween = ChronoUnit.DAYS.between(currentDate, completeDate);

    if (daysBetween <= 7) {
      throw new ErrorException(BookingDetailErrorMessage.CANCLE);
    }

    return updateBookingServiceStatus(bookingDetail, BookingDetailStatus.CANCEL);

  }

  @Override
  public BookingDetail doneBookingService(String bookingDetailId) {
    // return updateBookingServiceStatus(bookingDetailId, BookingDetailStatus.DONE);
    return null;

  }

  @Override
  public BookingDetail completeBookingService(String bookingDetailId) {
    return null;
    // return updateBookingServiceStatus(bookingDetailId,
    // BookingDetailStatus.COMPLETED);

  }

  @Override
  public BookingDetail processingBookingService(String bookingDetailId) {
    // return updateBookingServiceStatus(bookingDetailId,
    // BookingDetailStatus.ON_PROCESSING);
    return null;

  }

}
