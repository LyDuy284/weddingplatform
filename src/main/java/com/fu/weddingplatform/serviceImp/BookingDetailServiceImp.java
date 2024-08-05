package com.fu.weddingplatform.serviceImp;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.booking.BookingStatus;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailErrorMessage;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailStatus;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.BookingDetailHistory;
import com.fu.weddingplatform.entity.BookingHistory;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingDetailHistoryRepository;
import com.fu.weddingplatform.repository.BookingDetailRepository;
import com.fu.weddingplatform.repository.BookingHistoryRepository;
import com.fu.weddingplatform.response.booking.BookingDetailResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import com.fu.weddingplatform.service.BookingDetailService;
import com.fu.weddingplatform.service.ServiceSupplierService;
import com.fu.weddingplatform.utils.Utils;

@Service
public class BookingDetailServiceImp implements BookingDetailService {

  @Autowired
  private BookingDetailRepository bookingDetailRepository;

  @Autowired
  private BookingHistoryRepository bookingHistoryRepository;

  @Autowired
  private BookingDetailHistoryRepository bookingDetailHistoryRepository;

  @Autowired
  private ServiceSupplierService serviceSupplierService;


  @Autowired
  private ModelMapper modelMapper;

  @Override
  public BookingDetailResponse confirmBookingDetail(String bookingDetailId) {

    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    if (!(bookingDetail.getStatus().equals(BookingDetailStatus.PENDING))) {
      throw new ErrorException(BookingDetailErrorMessage.CONFIRM);
    }

    bookingDetail.setStatus(BookingDetailStatus.CONFIRM);

    bookingDetailRepository.save(bookingDetail);

    BookingDetailHistory bookingDetailHistory = BookingDetailHistory.builder()
        .bookingDetail(bookingDetail)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.CONFIRM)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);

    List<BookingDetail> listBookingDetails = bookingDetailRepository.findByBookingAndStatus(bookingDetail.getBooking(),
        BookingDetailStatus.PENDING);

    if (listBookingDetails.size() == 0) {
      BookingHistory bookingHistory = BookingHistory.builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .booking(bookingDetail.getBooking())
          .status(BookingStatus.CONFIRM)
          .build();

      bookingHistoryRepository.save(bookingHistory);
    }

    ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
        .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

    BookingDetailResponse response = modelMapper.map(bookingDetail, BookingDetailResponse.class);
    response.setServiceSupplierResponse(serviceSupplierResponse);
    return response;
  }

  @Override
  public BookingDetailResponse rejectBookingDetail(String bookingDetailId) {
    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    if ((bookingDetail.getStatus().equals(BookingDetailStatus.PENDING))) {
      throw new ErrorException(BookingDetailErrorMessage.REJECT);
    }

    bookingDetail.setStatus(BookingDetailStatus.REJECT);

    bookingDetailRepository.save(bookingDetail);

    BookingDetailHistory bookingDetailHistory = BookingDetailHistory.builder()
        .bookingDetail(bookingDetail)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.REJECT)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);

    List<BookingDetail> listBookingDetailPending = bookingDetailRepository.findByBookingAndStatusNot(
        bookingDetail.getBooking(),
        BookingDetailStatus.PENDING);

    if (listBookingDetailPending.size() == 0) {

      List<BookingDetail> listBookingDetailComplete = bookingDetailRepository.findByBookingAndStatusNot(
          bookingDetail.getBooking(),
          BookingDetailStatus.COMPLETED);

      List<BookingDetail> listBookingDetailConfirm = bookingDetailRepository.findByBookingAndStatusNot(
          bookingDetail.getBooking(),
          BookingDetailStatus.COMPLETED);

      if (listBookingDetailComplete.size() == 0 && listBookingDetailConfirm.size() == 0) {
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.REJECT)
            .build();
        bookingHistoryRepository.save(bookingHistory);
      } else if (listBookingDetailComplete.size() == 0) {
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.CONFIRM)
            .build();
        bookingHistoryRepository.save(bookingHistory);
      } else {
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.COMPLETED)
            .build();
        bookingHistoryRepository.save(bookingHistory);
      }

    }

    ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
        .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

    BookingDetailResponse response = modelMapper.map(bookingDetail, BookingDetailResponse.class);
    response.setServiceSupplierResponse(serviceSupplierResponse);
    return response;
  }

  @Override
  public BookingDetailResponse cancleBookingDetail(String bookingDetailId) {
    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    if ((bookingDetail.getStatus().equals(BookingDetailStatus.COMPLETED))) {
      throw new ErrorException(BookingDetailErrorMessage.CANCLE);
    }

    bookingDetail.setStatus(BookingDetailStatus.CANCEL);

    bookingDetailRepository.save(bookingDetail);

    BookingDetailHistory bookingDetailHistory = BookingDetailHistory.builder()
        .bookingDetail(bookingDetail)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.CANCEL)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);

    LocalDate currentDate = Utils.getCurrentDate();

    LocalDate completedDate = Utils.convertStringToLocalDate(bookingDetail.getCompletedDate());

    int daysBetween = (int) ChronoUnit.DAYS.between(completedDate, currentDate);

    if (daysBetween > 10) {
      // refund 80%
    }

    List<BookingDetail> listBookingDetailPending = bookingDetailRepository.findByBookingAndStatusNot(
        bookingDetail.getBooking(),
        BookingDetailStatus.PENDING);

    if (listBookingDetailPending.size() == 0) {

      List<BookingDetail> listBookingDetailComplete = bookingDetailRepository.findByBookingAndStatusNot(
          bookingDetail.getBooking(),
          BookingDetailStatus.COMPLETED);

      List<BookingDetail> listBookingDetailConfirm = bookingDetailRepository.findByBookingAndStatusNot(
          bookingDetail.getBooking(),
          BookingDetailStatus.COMPLETED);

      List<BookingDetail> listBookingDetailReject = bookingDetailRepository.findByBookingAndStatusNot(
          bookingDetail.getBooking(),
          BookingDetailStatus.REJECT);

      if (listBookingDetailComplete.size() == 0 && listBookingDetailConfirm.size() == 0
          && listBookingDetailReject.size() == 0) {
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.CANCLE)
            .build();
        bookingHistoryRepository.save(bookingHistory);
      } else if (listBookingDetailComplete.size() == 0 && listBookingDetailConfirm.size() == 0) {
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.REJECT)
            .build();
        bookingHistoryRepository.save(bookingHistory);
      } else if (listBookingDetailComplete.size() == 0) {
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.CONFIRM)
            .build();
        bookingHistoryRepository.save(bookingHistory);
      } else {
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.COMPLETED)
            .build();
        bookingHistoryRepository.save(bookingHistory);
      }

    }
    ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
        .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

    BookingDetailResponse response = modelMapper.map(bookingDetail, BookingDetailResponse.class);
    response.setServiceSupplierResponse(serviceSupplierResponse);
    return response;
  }

  @Override
  public BookingDetailResponse completeBookingDetail(String bookingDetailId) {
    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    if (!(bookingDetail.getStatus().equals(BookingDetailStatus.CONFIRM))) {
      throw new ErrorException(BookingDetailErrorMessage.COMPLETE);
    }

    bookingDetail.setStatus(BookingDetailStatus.COMPLETED);

    bookingDetailRepository.save(bookingDetail);

    BookingDetailHistory bookingDetailHistory = BookingDetailHistory.builder()
        .bookingDetail(bookingDetail)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.COMPLETED)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);
    List<BookingDetail> listBookingDetailPending = bookingDetailRepository.findByBookingAndStatusNot(
        bookingDetail.getBooking(),
        BookingDetailStatus.PENDING);

    if (listBookingDetailPending.size() == 0) {
      BookingHistory bookingHistory = BookingHistory.builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .booking(bookingDetail.getBooking())
          .status(BookingStatus.COMPLETED)
          .build();

      bookingHistoryRepository.save(bookingHistory);
    }

    ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
        .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

    BookingDetailResponse response = modelMapper.map(bookingDetail, BookingDetailResponse.class);
    response.setServiceSupplierResponse(serviceSupplierResponse);
    return response;
  }

}
