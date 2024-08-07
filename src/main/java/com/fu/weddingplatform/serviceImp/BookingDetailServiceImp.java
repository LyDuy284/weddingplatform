package com.fu.weddingplatform.serviceImp;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.booking.BookingStatus;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailErrorMessage;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailStatus;
import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.BookingDetailHistory;
import com.fu.weddingplatform.entity.BookingHistory;
import com.fu.weddingplatform.exception.EmptyException;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingDetailHistoryRepository;
import com.fu.weddingplatform.repository.BookingDetailRepository;
import com.fu.weddingplatform.repository.BookingHistoryRepository;
import com.fu.weddingplatform.response.booking.BookingDetailResponse;
import com.fu.weddingplatform.response.bookingHIstory.BookingDetailHistoryResponse;
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

    bookingDetail.setStatus(BookingDetailStatus.APPROVED);

    bookingDetailRepository.save(bookingDetail);

    BookingDetailHistory bookingDetailHistory = BookingDetailHistory.builder()
        .bookingDetail(bookingDetail)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.APPROVED)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);

    List<BookingDetail> listBookingDetails = bookingDetailRepository.findByBookingAndStatus(bookingDetail.getBooking(),
        BookingDetailStatus.PENDING);

    if (listBookingDetails.size() == 0) {
      bookingDetail.getBooking().setStatus(BookingStatus.CONFIRM);

      BookingHistory bookingHistory = BookingHistory.builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .booking(bookingDetail.getBooking())
          .status(BookingStatus.CONFIRM)
          .build();

      bookingHistoryRepository.saveAndFlush(bookingHistory);

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

    if (!(bookingDetail.getStatus().equals(BookingDetailStatus.PENDING))) {
      throw new ErrorException(BookingDetailErrorMessage.REJECT);
    }

    bookingDetail.setStatus(BookingDetailStatus.REJECTED);

    Booking booking = bookingDetail.getBooking();
    booking.setTotalPrice(booking.getTotalPrice() - bookingDetail.getPrice());
    bookingDetailRepository.saveAndFlush(bookingDetail);

    BookingDetailHistory bookingDetailHistory = BookingDetailHistory.builder()
        .bookingDetail(bookingDetail)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.REJECTED)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);

    List<BookingDetail> listBookingDetailPending = bookingDetailRepository.findByBookingAndStatus(
        bookingDetail.getBooking(),
        BookingDetailStatus.PENDING);

    if (listBookingDetailPending.size() == 0) {

      List<BookingDetail> listBookingDetailComplete = bookingDetailRepository.findByBookingAndStatus(
          bookingDetail.getBooking(),
          BookingDetailStatus.COMPLETED);

      List<BookingDetail> listBookingDetailConfirm = bookingDetailRepository.findByBookingAndStatus(
          bookingDetail.getBooking(),
          BookingDetailStatus.APPROVED);

      List<BookingDetail> listBookingDetailProcessing = bookingDetailRepository.findByBookingAndStatus(
          bookingDetail.getBooking(),
          BookingDetailStatus.PROCESSING);

      if (listBookingDetailComplete.size() == 0 && listBookingDetailConfirm.size() == 0
          && listBookingDetailProcessing.size() == 0) {
        bookingDetail.getBooking().setStatus(BookingStatus.REJECT);
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.REJECT)
            .build();

        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (listBookingDetailConfirm.size() != 0) {

        bookingDetail.getBooking().setStatus(BookingStatus.CONFIRM);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.CONFIRM)
            .build();

        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (listBookingDetailProcessing.size() != 0) {

        bookingDetail.getBooking().setStatus(BookingStatus.PROCESSING);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.PROCESSING)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else {
        bookingDetail.getBooking().setStatus(BookingStatus.COMPLETED);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.COMPLETED)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
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

    bookingDetail.setStatus(BookingDetailStatus.CANCELLED);

    Booking booking = bookingDetail.getBooking();
    booking.setTotalPrice(booking.getTotalPrice() - bookingDetail.getPrice());
    bookingDetailRepository.saveAndFlush(bookingDetail);

    BookingDetailHistory bookingDetailHistory = BookingDetailHistory.builder()
        .bookingDetail(bookingDetail)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.CANCELLED)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);

    LocalDate currentDate = Utils.getCurrentDate();

    LocalDate completedDate = Utils.convertStringToLocalDate(bookingDetail.getCompletedDate());

    int daysBetween = (int) ChronoUnit.DAYS.between(completedDate, currentDate);

    if (daysBetween > 10) {
      // refund 80%
    }

    List<BookingDetail> listBookingDetailPending = bookingDetailRepository.findByBookingAndStatus(
        booking,
        BookingDetailStatus.PENDING);

    if (listBookingDetailPending.size() == 0) {

      List<BookingDetail> listBookingDetailComplete = bookingDetailRepository.findByBookingAndStatus(
          booking,
          BookingDetailStatus.COMPLETED);

      List<BookingDetail> listBookingDetailConfirm = bookingDetailRepository.findByBookingAndStatus(
          booking,
          BookingDetailStatus.APPROVED);

      List<BookingDetail> listBookingDetailReject = bookingDetailRepository.findByBookingAndStatus(
          booking,
          BookingDetailStatus.REJECTED);

      List<BookingDetail> listBookingDetailProcessing = bookingDetailRepository.findByBookingAndStatus(
          booking,
          BookingDetailStatus.PROCESSING);

      if (listBookingDetailComplete.size() == 0 && listBookingDetailConfirm.size() == 0
          && listBookingDetailReject.size() == 0 && listBookingDetailPending.size() == 0) {

        booking.setStatus(BookingStatus.CANCLE);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(booking)
            .status(BookingStatus.CANCLE)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (listBookingDetailComplete.size() == 0 && listBookingDetailConfirm.size() == 0
          && listBookingDetailPending.size() == 0) {

        booking.setStatus(BookingStatus.REJECT);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(booking)
            .status(BookingStatus.REJECT)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (listBookingDetailConfirm.size() != 0) {

        booking.setStatus(BookingStatus.CONFIRM);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(booking)
            .status(BookingStatus.CONFIRM)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (listBookingDetailProcessing.size() != 0) {
        booking.setStatus(BookingStatus.PROCESSING);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(booking)
            .status(BookingStatus.PROCESSING)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else {
        booking.setStatus(BookingStatus.COMPLETED);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(booking)
            .status(BookingStatus.COMPLETED)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
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

    if (!(bookingDetail.getStatus().equals(BookingDetailStatus.APPROVED))) {
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
    List<BookingDetail> listBookingDetailProcessing = bookingDetailRepository.findByBookingAndStatusNot(
        bookingDetail.getBooking(),
        BookingDetailStatus.PROCESSING);

    if (listBookingDetailPending.size() == 0 && listBookingDetailProcessing.size() == 0) {

      bookingDetail.getBooking().setStatus(BookingStatus.COMPLETED);

      BookingHistory bookingHistory = BookingHistory.builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .booking(bookingDetail.getBooking())
          .status(BookingStatus.COMPLETED)
          .build();

      bookingHistoryRepository.saveAndFlush(bookingHistory);
    }

    ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
        .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

    BookingDetailResponse response = modelMapper.map(bookingDetail, BookingDetailResponse.class);
    response.setServiceSupplierResponse(serviceSupplierResponse);
    return response;
  }

  @Override
  public List<BookingDetailHistoryResponse> getBookingDetailHistoryById(String bookingDetailId) {

    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    List<BookingDetailHistory> listBookingDetails = bookingDetailHistoryRepository
        .findByBookingDetailOrderByCreatedAt(bookingDetail);

    if (listBookingDetails.size() == 0) {
      throw new EmptyException(BookingDetailErrorMessage.EMPTY);
    }

    List<BookingDetailHistoryResponse> response = new ArrayList<>();

    for (BookingDetailHistory bookingDetailHistory : listBookingDetails) {
      BookingDetailHistoryResponse bookingDetailHistoryResponse = modelMapper.map(bookingDetailHistory,
          BookingDetailHistoryResponse.class);
      response.add(bookingDetailHistoryResponse);
    }
    return response;
  }

  @Override
  public BookingDetailResponse processingBookingDetail(String bookingDetailId) {
    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    if (!(bookingDetail.getStatus().equals(BookingDetailStatus.APPROVED))) {
      throw new ErrorException(BookingDetailErrorMessage.PROCESSING);
    }

    bookingDetail.setStatus(BookingDetailStatus.PROCESSING);

    bookingDetailRepository.save(bookingDetail);

    BookingDetailHistory bookingDetailHistory = BookingDetailHistory.builder()
        .bookingDetail(bookingDetail)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.APPROVED)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);

    List<BookingDetail> listPendingBookingDetails = bookingDetailRepository.findByBookingAndStatus(
        bookingDetail.getBooking(),
        BookingDetailStatus.PENDING);

    List<BookingDetail> listProcessingBookingDetails = bookingDetailRepository.findByBookingAndStatus(
        bookingDetail.getBooking(),
        BookingDetailStatus.PROCESSING);

    if (listPendingBookingDetails.size() == 0 && listProcessingBookingDetails.size() == 0) {
      bookingDetail.getBooking().setStatus(BookingStatus.CONFIRM);

      BookingHistory bookingHistory = BookingHistory.builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .booking(bookingDetail.getBooking())
          .status(BookingStatus.CONFIRM)
          .build();

      bookingHistoryRepository.saveAndFlush(bookingHistory);

    } else if (listPendingBookingDetails.size() == 0) {
      bookingDetail.getBooking().setStatus(BookingStatus.CONFIRM);

      BookingHistory bookingHistory = BookingHistory.builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .booking(bookingDetail.getBooking())
          .status(BookingStatus.PROCESSING)
          .build();

      bookingHistoryRepository.saveAndFlush(bookingHistory);
    }

    ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
        .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

    BookingDetailResponse response = modelMapper.map(bookingDetail, BookingDetailResponse.class);
    response.setServiceSupplierResponse(serviceSupplierResponse);
    return response;
  }

}
