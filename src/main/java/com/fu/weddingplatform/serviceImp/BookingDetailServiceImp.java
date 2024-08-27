package com.fu.weddingplatform.serviceImp;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fu.weddingplatform.constant.booking.BookingStatus;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailErrorMessage;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailStatus;
import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.BookingDetailHistory;
import com.fu.weddingplatform.entity.BookingHistory;
import com.fu.weddingplatform.entity.InvoiceDetail;
import com.fu.weddingplatform.entity.Transaction;
import com.fu.weddingplatform.entity.TransactionSummary;
import com.fu.weddingplatform.exception.EmptyException;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingDetailHistoryRepository;
import com.fu.weddingplatform.repository.BookingDetailRepository;
import com.fu.weddingplatform.repository.BookingHistoryRepository;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.repository.InvoiceDetailRepository;
import com.fu.weddingplatform.repository.TransactionRepository;
import com.fu.weddingplatform.repository.TransactionSummaryRepository;
import com.fu.weddingplatform.request.booking.CancelBookingDTO;
import com.fu.weddingplatform.request.email.CancelBookingDetailMailForCouple;
import com.fu.weddingplatform.request.email.CancelBookingMailForSupplierDTO;
import com.fu.weddingplatform.request.email.MailApproveForCoupleDTO;
import com.fu.weddingplatform.request.email.ProcessingMailForCoupleDTO;
import com.fu.weddingplatform.request.email.RejectMailDTO;
import com.fu.weddingplatform.response.booking.BookingDetailResponse;
import com.fu.weddingplatform.response.bookingHIstory.BookingDetailHistoryResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import com.fu.weddingplatform.service.BookingDetailService;
import com.fu.weddingplatform.service.PaymentService;
import com.fu.weddingplatform.service.SentEmailService;
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

  @Autowired
  private SentEmailService sentEmailService;

  @Autowired
  private TransactionSummaryRepository transactionSummaryRepository;

  @Autowired
  private PaymentService paymentService;
  @Autowired
  private InvoiceDetailRepository invoiceDetailRepository;
  @Autowired
  private TransactionRepository transactionRepository;
  @Autowired
  private BookingRepository bookingRepository;

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

    if (checkApprovedBooking(bookingDetail.getBooking())) {
      bookingDetail.getBooking().setStatus(BookingStatus.APPROVED);

      BookingHistory bookingHistory = BookingHistory.builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .booking(bookingDetail.getBooking())
          .status(BookingStatus.APPROVED)
          .build();

      bookingHistoryRepository.saveAndFlush(bookingHistory);

    }

    MailApproveForCoupleDTO mail = MailApproveForCoupleDTO.builder()
        .bookingDetail(bookingDetail)
        .couple(bookingDetail.getBooking().getCouple())
        .build();

    sentEmailService.sentApprovedEmailForCouple(mail);

    ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
        .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

    BookingDetailResponse response = modelMapper.map(bookingDetail, BookingDetailResponse.class);
    response.setServiceSupplier(serviceSupplierResponse);
    return response;
  }

  @Override
  public BookingDetailResponse rejectBookingDetail(CancelBookingDTO cancelBookingDTO) throws MessagingException {
    BookingDetail bookingDetail = bookingDetailRepository.findById(cancelBookingDTO.getBookingDetailId()).orElseThrow(
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
        .description(cancelBookingDTO.getReason())
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.REJECTED)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);

    List<BookingDetail> listBookingDetailPending = bookingDetailRepository.findByBookingAndStatus(
        bookingDetail.getBooking(),
        BookingDetailStatus.PENDING);

    if (listBookingDetailPending.size() == 0) {

      if (checkRejectBooking(booking)) {
        bookingDetail.getBooking().setStatus(BookingStatus.REJECTED);
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.REJECTED)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (checkApprovedBooking(booking)) {
        bookingDetail.getBooking().setStatus(BookingStatus.APPROVED);
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.APPROVED)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);

      } else if (checkDepositBooking(booking)) {
        bookingDetail.getBooking().setStatus(BookingStatus.DEPOSITED);
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.DEPOSITED)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (checkProcessingBooking(booking)) {
        bookingDetail.getBooking().setStatus(BookingStatus.PROCESSING);
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.PROCESSING)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);

      } else if (checkDoneBooking(booking)) {
        bookingDetail.getBooking().setStatus(BookingStatus.DONE);
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.DONE)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (checkFinalPaymentBooking(booking)) {
        bookingDetail.getBooking().setStatus(BookingStatus.FINAL_PAYMENT);
        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(bookingDetail.getBooking())
            .status(BookingStatus.FINAL_PAYMENT)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);

      } else if (checkCompleteBooking(booking)) {
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

    BookingDetailResponse response = modelMapper.map(bookingDetail,
        BookingDetailResponse.class);
    response.setServiceSupplier(serviceSupplierResponse);

    List<BookingDetail> listAvailableBookings = bookingDetailRepository
        .findAvailableBookingDetailByBooking(booking.getId());

    RejectMailDTO rejectMailDTO = RejectMailDTO.builder()
        .mail(bookingDetail.getBooking().getCouple().getAccount().getEmail())
        .bookingDetail(bookingDetail)
        .coupleName(bookingDetail.getBooking().getCouple().getAccount().getName())
        .supplierName(bookingDetail.getServiceSupplier().getSupplier().getSupplierName())
        .listCurrentBookingDetails(listAvailableBookings)
        .totalPrice(Utils.formatAmountToVND(booking.getTotalPrice()))
        .remaining(Utils.formatAmountToVND(booking.getTotalPrice()))
        .paidPrice(Utils.formatAmountToVND(0))
        .reason(cancelBookingDTO.getReason())
        .build();

    Optional<TransactionSummary> transactionSummary = transactionSummaryRepository.findFirstByBooking(booking);
    if (transactionSummary.isPresent()) {
      rejectMailDTO.setPaidPrice(Utils.formatAmountToVND(transactionSummary.get().getTotalAmount()));
      rejectMailDTO
          .setRemaining(Utils.formatAmountToVND(booking.getTotalPrice() - transactionSummary.get().getTotalAmount()));
    }

    sentEmailService.sentRejectBooking(rejectMailDTO);

    return response;
  }

  @Override
  @Transactional
  public BookingDetailResponse cancleBookingDetail(CancelBookingDTO cancelBookingDTO) {
    BookingDetail bookingDetail = bookingDetailRepository.findById(cancelBookingDTO.getBookingDetailId()).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    if ((bookingDetail.getStatus().equals(BookingDetailStatus.COMPLETED))) {
      throw new ErrorException(BookingDetailErrorMessage.CANCLE);
    }

    bookingDetail.setStatus(BookingDetailStatus.CANCELED);

    Booking booking = bookingDetail.getBooking();
    booking.setTotalPrice(booking.getTotalPrice() - bookingDetail.getPrice());
    bookingDetailRepository.saveAndFlush(bookingDetail);

    BookingDetailHistory bookingDetailHistory = BookingDetailHistory.builder()
        .bookingDetail(bookingDetail)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.CANCELED)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);

    LocalDate currentDate = Utils.getCurrentDate();

    LocalDate completedDate = Utils.convertStringToLocalDateTime(bookingDetail.getCompletedDate()).toLocalDate();

    int daysBetween = (int) ChronoUnit.DAYS.between(completedDate, currentDate);

    // check deposited
    Optional<InvoiceDetail> optionalInvoiceDetail = invoiceDetailRepository
        .findDepositedInvoiceDetailByBookingDetailId(cancelBookingDTO.getBookingDetailId());
    if (optionalInvoiceDetail.isPresent()) {
      Optional<Transaction> optionalTransaction = transactionRepository
          .findCompletedTransaction(optionalInvoiceDetail.get().getId());
      if (optionalTransaction.isPresent()) {
        if (Math.abs(daysBetween) > 10) {
          // refund 40%
          int refundPrice = paymentService.refundDepositedTransaction(booking.getCouple().getId(),
              cancelBookingDTO.getBookingDetailId());
          int depositedPrice = optionalTransaction.get().getAmount();
          booking.setTotalPrice(booking.getTotalPrice() + (depositedPrice - refundPrice));
        } else {
          booking.setTotalPrice(booking.getTotalPrice() + optionalTransaction.get().getAmount());
        }
        bookingRepository.saveAndFlush(booking);
      }
    }

    List<BookingDetail> listBookingDetailPending = bookingDetailRepository.findByBookingAndStatus(
        booking,
        BookingDetailStatus.PENDING);

    if (listBookingDetailPending.size() == 0) {
      if (checkCancleBooking(booking)) {

        booking.setStatus(BookingStatus.CANCELED);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(booking)
            .status(BookingStatus.CANCELED)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (checkRejectBooking(booking)) {

        booking.setStatus(BookingStatus.REJECTED);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(booking)
            .status(BookingStatus.REJECTED)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (checkApprovedBooking(booking)) {

        booking.setStatus(BookingStatus.APPROVED);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(booking)
            .status(BookingStatus.APPROVED)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (checkDepositBooking(booking)) {
        booking.setStatus(BookingStatus.DEPOSITED);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(booking)
            .status(BookingStatus.DEPOSITED)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (checkProcessingBooking(booking)) {
        booking.setStatus(BookingStatus.PROCESSING);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(booking)
            .status(BookingStatus.PROCESSING)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (checkDoneBooking(booking)) {
        booking.setStatus(BookingStatus.DONE);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(booking)
            .status(BookingStatus.DONE)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (checkFinalPaymentBooking(booking)) {
        booking.setStatus(BookingStatus.FINAL_PAYMENT);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(booking)
            .status(BookingStatus.FINAL_PAYMENT)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      } else if (checkCompleteBooking(booking)) {
        booking.setStatus(BookingStatus.COMPLETED);

        BookingHistory bookingHistory = BookingHistory.builder()
            .createdAt(Utils.formatVNDatetimeNow())
            .booking(booking)
            .status(BookingStatus.COMPLETED)
            .build();
        bookingHistoryRepository.saveAndFlush(bookingHistory);
      }
    }

    CancelBookingMailForSupplierDTO cancleBookingMail = CancelBookingMailForSupplierDTO.builder()
        .supplierName(bookingDetail.getServiceSupplier().getSupplier().getSupplierName())
        .supplierEmail(bookingDetail.getServiceSupplier().getSupplier().getContactEmail())
        .coupleName(booking.getCouple().getAccount().getName())
        .phoneNumber(booking.getCouple().getAccount().getPhoneNumber())
        .bookingDetail(bookingDetail)
        .reason(cancelBookingDTO.getReason())
        .build();

    sentEmailService.sentCancelBookingDetailForSupplier(cancleBookingMail);

    ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
        .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

    List<BookingDetail> listCurrentBookings = bookingDetailRepository.findValidBookingDetailByBooking(booking.getId());

    Optional<TransactionSummary> transactionSummary = transactionSummaryRepository.findFirstByBooking(booking);

    CancelBookingDetailMailForCouple cancelBookingDetailMailForCouple = CancelBookingDetailMailForCouple.builder()
        .coupleName(booking.getCouple().getAccount().getName())
        .coupleMail(booking.getCouple().getAccount().getEmail())
        .bookingDetail(bookingDetail)
        .booking(booking)
        .currentBooking(listCurrentBookings)
        .totalAmount(Utils.formatAmountToVND(booking.getTotalPrice()))
        .paymentAmount(Utils.formatAmountToVND(0))
        .remaining(Utils.formatAmountToVND(0))
        .reason(cancelBookingDTO.getReason())
        .build();

    if (transactionSummary.isPresent()) {
      cancelBookingDetailMailForCouple
          .setPaymentAmount(Utils.formatAmountToVND(transactionSummary.get().getTotalAmount()));
      cancelBookingDetailMailForCouple
          .setRemaining(Utils.formatAmountToVND(booking.getTotalPrice() - transactionSummary.get().getTotalAmount()));
    }

    sentEmailService.sentCancelBookingDetailForCouple(cancelBookingDetailMailForCouple);

    BookingDetailResponse response = modelMapper.map(bookingDetail, BookingDetailResponse.class);
    response.setServiceSupplier(serviceSupplierResponse);
    return response;
  }

  @Override
  public BookingDetailResponse completeBookingDetail(String bookingDetailId) {
    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    if (!(bookingDetail.getStatus().equals(BookingDetailStatus.DONE))) {
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
      Booking booking = bookingDetail.getBooking();
      if (checkCompleteBooking(booking)) {
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
    response.setServiceSupplier(serviceSupplierResponse);
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

    if (!(bookingDetail.getStatus().equals(BookingDetailStatus.DEPOSITED))) {
      throw new ErrorException(BookingDetailErrorMessage.PROCESSING);
    }

    bookingDetail.setStatus(BookingDetailStatus.PROCESSING);

    bookingDetailRepository.save(bookingDetail);

    BookingDetailHistory bookingDetailHistory = BookingDetailHistory.builder()
        .bookingDetail(bookingDetail)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.PROCESSING)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);

    if (checkProcessingBooking(bookingDetail.getBooking())) {

      bookingDetail.getBooking().setStatus(BookingStatus.PROCESSING);

      BookingHistory bookingHistory = BookingHistory.builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .booking(bookingDetail.getBooking())
          .status(BookingStatus.PROCESSING)
          .build();
      bookingHistoryRepository.saveAndFlush(bookingHistory);
    }

    int paymentAmount = invoiceDetailRepository.getPayMentPriceByBookingDetail(bookingDetailId);

    ProcessingMailForCoupleDTO mail = ProcessingMailForCoupleDTO.builder()
        .bookingDetail(bookingDetail)
        .couple(bookingDetail.getBooking().getCouple())
        .supplier(bookingDetail.getServiceSupplier().getSupplier())
        .totalPrice(Utils.formatAmountToVND(bookingDetail.getPrice()))
        .paymentAmount(Utils.formatAmountToVND(paymentAmount))
        .remainingAmount(Utils.formatAmountToVND(bookingDetail.getPrice() - paymentAmount))
        .build();

    sentEmailService.sentProcessingEmailForCouple(mail);

    ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
        .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

    BookingDetailResponse response = modelMapper.map(bookingDetail,
        BookingDetailResponse.class);
    response.setServiceSupplier(serviceSupplierResponse);
    return response;

  }

  @Override
  public BookingDetailResponse depositBookingDetail(String bookingDetailId) {
    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    if (!(bookingDetail.getStatus().equals(BookingDetailStatus.APPROVED))) {
      throw new ErrorException(BookingDetailErrorMessage.DEPOSIT);
    }

    bookingDetail.setStatus(BookingDetailStatus.DEPOSITED);

    bookingDetailRepository.save(bookingDetail);

    BookingDetailHistory bookingDetailHistory = BookingDetailHistory.builder()
        .bookingDetail(bookingDetail)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.DEPOSITED)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);

    if (checkDepositBooking(bookingDetail.getBooking())) {
      bookingDetail.getBooking().setStatus(BookingStatus.DEPOSITED);

      BookingHistory bookingHistory = BookingHistory.builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .booking(bookingDetail.getBooking())
          .status(BookingStatus.DEPOSITED)
          .build();

      bookingHistoryRepository.saveAndFlush(bookingHistory);

    }

    ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
        .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

    BookingDetailResponse response = modelMapper.map(bookingDetail, BookingDetailResponse.class);
    response.setServiceSupplier(serviceSupplierResponse);
    return response;
  }

  @Override
  public BookingDetailResponse doneBookingDetail(String bookingDetailId) {
    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    if (!(bookingDetail.getStatus().equals(BookingDetailStatus.PROCESSING))) {
      throw new ErrorException(BookingDetailErrorMessage.DONE);
    }

    bookingDetail.setStatus(BookingDetailStatus.DONE);

    bookingDetailRepository.save(bookingDetail);

    BookingDetailHistory bookingDetailHistory = BookingDetailHistory.builder()
        .bookingDetail(bookingDetail)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.DONE)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);

    if (checkDoneBooking(bookingDetail.getBooking())) {
      bookingDetail.getBooking().setStatus(BookingStatus.DONE);

      BookingHistory bookingHistory = BookingHistory.builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .booking(bookingDetail.getBooking())
          .status(BookingStatus.DONE)
          .build();

      bookingHistoryRepository.saveAndFlush(bookingHistory);

    }

    ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
        .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

    BookingDetailResponse response = modelMapper.map(bookingDetail, BookingDetailResponse.class);
    response.setServiceSupplier(serviceSupplierResponse);
    return response;
  }

  @Override
  public BookingDetailResponse finalPaymentBookingDetail(String bookingDetailId) {
    BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElseThrow(
        () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

    if (!(bookingDetail.getStatus().equals(BookingDetailStatus.DONE))) {
      throw new ErrorException(BookingDetailErrorMessage.FINAL_PAYMENT);
    }

    bookingDetail.setStatus(BookingDetailStatus.FINAL_PAYMENT);

    bookingDetailRepository.save(bookingDetail);

    BookingDetailHistory bookingDetailHistory = BookingDetailHistory.builder()
        .bookingDetail(bookingDetail)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingDetailStatus.FINAL_PAYMENT)
        .build();

    bookingDetailHistoryRepository.save(bookingDetailHistory);

    if (checkFinalPaymentBooking(bookingDetail.getBooking())) {
      bookingDetail.getBooking().setStatus(BookingStatus.FINAL_PAYMENT);

      BookingHistory bookingHistory = BookingHistory.builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .booking(bookingDetail.getBooking())
          .status(BookingStatus.FINAL_PAYMENT)
          .build();

      bookingHistoryRepository.saveAndFlush(bookingHistory);
    }

    ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
        .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

    BookingDetailResponse response = modelMapper.map(bookingDetail, BookingDetailResponse.class);
    response.setServiceSupplier(serviceSupplierResponse);
    return response;

  }

  public boolean checkCompleteBooking(Booking booking) {

    int checkApproved = bookingDetailRepository.countByStatusInAndBooking(
        Arrays.asList(BookingDetailStatus.REJECTED, BookingDetailStatus.CANCELED, BookingDetailStatus.COMPLETED),
        booking);

    int checkComplete = bookingDetailRepository.countByStatusInAndBooking(Arrays.asList(BookingDetailStatus.COMPLETED),
        booking);

    if (checkApproved == booking.getBookingDetails().size() && checkComplete != 0) {
      return true;
    } else
      return false;

  }

  public boolean checkDepositBooking(Booking booking) {

    int checkApproved = bookingDetailRepository.countByStatusInAndBooking(
        Arrays.asList(BookingDetailStatus.PENDING, BookingDetailStatus.APPROVED), booking);

    int checkDeposit = bookingDetailRepository.countByStatusInAndBooking(
        Arrays.asList(BookingDetailStatus.DEPOSITED), booking);

    if (checkApproved == 0 && checkDeposit != 0) {
      return true;
    } else
      return false;

  }

  public boolean checkProcessingBooking(Booking booking) {

    int checkApproved = bookingDetailRepository.countByStatusInAndBooking(
        Arrays.asList(BookingDetailStatus.PENDING, BookingDetailStatus.APPROVED, BookingDetailStatus.DEPOSITED),
        booking);

    int checkProcessingBooking = bookingDetailRepository.countByStatusInAndBooking(
        Arrays.asList(BookingDetailStatus.PROCESSING), booking);

    if (checkApproved == 0 && checkProcessingBooking != 0) {
      return true;
    } else
      return false;

  }

  public boolean checkApprovedBooking(Booking booking) {

    int checkPending = bookingDetailRepository.countByStatusInAndBooking(
        Arrays.asList(BookingDetailStatus.PENDING),
        booking);

    int checkApprovedBooking = bookingDetailRepository.countByStatusInAndBooking(
        Arrays.asList(BookingDetailStatus.APPROVED), booking);

    if (checkPending == 0 && checkApprovedBooking != 0) {
      return true;
    } else
      return false;

  }

  public boolean checkCancleBooking(Booking booking) {

    int checkCancleBooking = bookingDetailRepository.countByStatusInAndBooking(
        Arrays.asList(BookingDetailStatus.CANCELED),
        booking);

    if (checkCancleBooking == booking.getBookingDetails().size()) {
      return true;
    } else
      return false;

  }

  public boolean checkFinalPaymentBooking(Booking booking) {

    int checkApprovedBooking = bookingDetailRepository.countByStatusInAndBooking(
        Arrays.asList(BookingDetailStatus.APPROVED, BookingDetailStatus.PENDING, BookingDetailStatus.DEPOSITED,
            BookingDetailStatus.PROCESSING, BookingDetailStatus.DONE),
        booking);

    int checkFinalPaymentBooking = bookingDetailRepository
        .countByStatusInAndBooking(Arrays.asList(), booking);

    if (checkApprovedBooking == 0 && checkFinalPaymentBooking != 0) {
      return true;
    } else
      return false;

  }

  public boolean checkRejectBooking(Booking booking) {

    int checkPendingBooking = bookingDetailRepository.countByStatusInAndBooking(
        Arrays.asList(BookingDetailStatus.PENDING, BookingDetailStatus.APPROVED),
        booking);

    int checkRejectBooking = bookingDetailRepository
        .countByStatusInAndBooking(Arrays.asList(BookingDetailStatus.CANCELED, BookingDetailStatus.REJECTED), booking);

    if (checkPendingBooking == 0 && checkRejectBooking == booking.getBookingDetails().size()) {
      return true;
    } else
      return false;

  }

  public boolean checkDoneBooking(Booking booking) {

    int checkApprovedBooking = bookingDetailRepository.countByStatusInAndBooking(
        Arrays.asList(BookingDetailStatus.APPROVED, BookingDetailStatus.PENDING, BookingDetailStatus.DEPOSITED,
            BookingDetailStatus.PROCESSING),
        booking);

    int checkDoneBooking = bookingDetailRepository
        .countByStatusInAndBooking(Arrays.asList(BookingDetailStatus.DONE), booking);

    if (checkApprovedBooking == 0 && checkDoneBooking != 0) {
      return true;
    } else
      return false;

  }

}
