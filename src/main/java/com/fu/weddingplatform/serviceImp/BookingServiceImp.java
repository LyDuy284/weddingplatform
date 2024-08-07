package com.fu.weddingplatform.serviceImp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.booking.BookingErrorMessage;
import com.fu.weddingplatform.constant.booking.BookingStatus;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailStatus;
import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.constant.promotion.PromotionType;
import com.fu.weddingplatform.constant.service.ServiceErrorMessage;
import com.fu.weddingplatform.constant.supplier.SupplierErrorMessage;
import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.BookingDetailHistory;
import com.fu.weddingplatform.entity.BookingHistory;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Promotion;
import com.fu.weddingplatform.entity.PromotionServiceSupplier;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.exception.EmptyException;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingDetailHistoryRepository;
import com.fu.weddingplatform.repository.BookingDetailRepository;
import com.fu.weddingplatform.repository.BookingHistoryRepository;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.repository.PromotionServiceSupplierRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.repository.SupplierRepository;
import com.fu.weddingplatform.request.booking.CreateBookingDTO;
import com.fu.weddingplatform.request.booking.ServiceSupplierBookingDTO;
import com.fu.weddingplatform.response.booking.BookingDetailBySupplierResponse;
import com.fu.weddingplatform.response.booking.BookingDetailResponse;
import com.fu.weddingplatform.response.booking.BookingResponse;
import com.fu.weddingplatform.response.bookingHIstory.BookingHistoryResponse;
import com.fu.weddingplatform.response.couple.CoupleResponse;
import com.fu.weddingplatform.response.promotion.PromotionResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierBySupplierBooking;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import com.fu.weddingplatform.service.BookingDetailService;
import com.fu.weddingplatform.service.BookingService;
import com.fu.weddingplatform.service.CoupleService;
import com.fu.weddingplatform.service.PromotionService;
import com.fu.weddingplatform.service.SentEmailService;
import com.fu.weddingplatform.service.ServiceSupplierService;
import com.fu.weddingplatform.utils.Utils;

@Service
public class BookingServiceImp implements BookingService {

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private BookingDetailRepository bookingDetailRepository;

  @Autowired
  private CoupleRepository coupleRepository;

  @Autowired
  private ServiceSupplierRepository serviceSupplierRepository;

  @Autowired
  private BookingHistoryRepository bookingHistoryRepository;

  @Autowired
  private BookingDetailHistoryRepository bookingDetailHistoryRepository;

  @Autowired
  private CoupleService coupleService;

  @Autowired
  private ServiceSupplierService serviceSupplierService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private PromotionService promotionService;

  @Autowired
  private PromotionServiceSupplierRepository promotionServiceSupplierRepository;

  @Autowired
  private SupplierRepository supplierRepository;

  @Autowired
  private BookingDetailService bookingDetailService;

  @Autowired
  private SentEmailService sentEmailService;

  @Override
  public BookingResponse createBooking(CreateBookingDTO createDTO) {

    Couple couple = coupleRepository.findById(createDTO.getCoupleId()).orElseThrow(
        () -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));

    if (createDTO.getListServiceSupplier().size() <= 0) {
      throw new EmptyException(BookingErrorMessage.EMPTY_LIST_SERVICE);
    }

    LocalDate currentDate = Utils.getCurrentDate();

    Booking booking = Booking.builder()
        .couple(couple)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingStatus.PENDING)
        .build();

    Booking bookingSaved = bookingRepository.save(booking);

    List<BookingDetailResponse> listBookingDetailResponse = new ArrayList<>();
    List<BookingDetail> listBookingDetailSaved = new ArrayList<>();

    List<BookingDetail> listBookingDetails = new ArrayList<>();
    int totalPrice = 0;
    for (ServiceSupplierBookingDTO serviceSupplierBookingDTO : createDTO.getListServiceSupplier()) {
      Optional<ServiceSupplier> serviceSupplier = serviceSupplierRepository
          .findById(serviceSupplierBookingDTO.getServiceSupplierId().trim());

      if (serviceSupplier.isEmpty()) {
        bookingRepository.delete(bookingSaved);
        throw new ErrorException(ServiceErrorMessage.NOT_FOUND);
      }

      LocalDate completeDate = serviceSupplierBookingDTO.getDateCompleted().toLocalDate();

      if (currentDate.isEqual(completeDate) || currentDate.isAfter(completeDate)) {
        throw new ErrorException(BookingErrorMessage.COMPLETE_DATE_GREATER_THAN_CURRENT_DATE);
      }

      PromotionServiceSupplier promotionServiceSupplier = promotionServiceSupplierRepository
          .findFirstByServiceSupplierAndStatus(serviceSupplier.get(),
              Status.ACTIVATED);

      Promotion promotion = null;
      int price = 0;
      if (promotionServiceSupplier != null) {
        promotion = promotionServiceSupplier.getPromotion();

        switch (promotion.getType()) {
          case PromotionType.PERCENT:
            price = (int) (serviceSupplier.get().getPrice() * (100 - promotion.getValue()) *
                0.01);
            break;
          case PromotionType.MONEY:
            price = serviceSupplier.get().getPrice() - promotion.getValue();
            break;
          default:
            break;
        }
      } else {
        price = serviceSupplier.get().getPrice();
      }

      BookingDetail bookingDetail = BookingDetail.builder()
          .booking(bookingSaved)
          .serviceSupplier(serviceSupplier.get())
          .completedDate(completeDate.toString())
          .note(serviceSupplierBookingDTO.getNote())
          .price(price)
          .createAt(Utils.formatVNDatetimeNow())
          .promotionServiceSupplier(promotionServiceSupplier)
          .status(BookingDetailStatus.PENDING)
          .build();

      totalPrice += bookingDetail.getPrice();

      listBookingDetails.add(bookingDetail);
    }

    bookingSaved.setTotalPrice(totalPrice);
    bookingRepository.saveAndFlush(bookingSaved);

    BookingHistory bookingHistory = BookingHistory.builder()
        .booking(bookingSaved)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(Status.PENDING)
        .build();

    bookingHistoryRepository.save(bookingHistory);

    for (BookingDetail bookingDetail : listBookingDetails) {
      BookingDetail bookingDetailSaved = bookingDetailRepository.saveAndFlush(bookingDetail);
      listBookingDetailSaved.add(bookingDetailSaved);
      BookingDetailHistory bookingDetailHistory = BookingDetailHistory
          .builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .status(bookingDetail.getStatus())
          .bookingDetail(bookingDetailSaved)
          .build();

      bookingDetailHistoryRepository.save(bookingDetailHistory);

      ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
          .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

      BookingDetailResponse bookingDetailResponse = modelMapper.map(bookingDetail, BookingDetailResponse.class);

      if (bookingDetail.getPromotionServiceSupplier() != null) {
        PromotionResponse promotionResponse = promotionService
            .convertPromotionToResponse(bookingDetail.getPromotionServiceSupplier().getPromotion());
        bookingDetailResponse.setPromotionResponse(promotionResponse);
      }

      bookingDetailResponse.setServiceSupplierResponse(serviceSupplierResponse);
      listBookingDetailResponse.add(bookingDetailResponse);

    }
    bookingSaved.setBookingDetails(listBookingDetails);
    bookingRepository.save(bookingSaved);
    BookingResponse response = modelMapper.map(bookingSaved,
        BookingResponse.class);
    CoupleResponse coupleResponse = coupleService.getCoupleById(couple.getId());
    response.setListBookingDetail(listBookingDetailResponse);
    response.setCouple(coupleResponse);
    response.setTotalPrice(totalPrice);

    return response;
  }

  @Override
  public BookingResponse getBookingById(String bookingId) {
    Booking booking = bookingRepository.findById(bookingId).orElseThrow(
        () -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));
    BookingResponse response = convertBookingToBookingResponse(booking);
    return response;
  }

  @Override
  public BookingResponse convertBookingToBookingResponse(Booking booking) {
    if (booking == null) {
      return null;
    }
    BookingResponse response = modelMapper.map(booking, BookingResponse.class);
    CoupleResponse coupleResponse = coupleService.getCoupleById(booking.getCouple().getId());

    List<BookingDetailResponse> listBookingDetailResponses = new ArrayList<>();

    for (BookingDetail bookingDetail : booking.getBookingDetails()) {

      ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
          .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

      PromotionServiceSupplier promotionServiceSupplier = bookingDetail.getPromotionServiceSupplier();
      Promotion promotion = null;
      if (promotionServiceSupplier != null) {
        promotion = promotionServiceSupplier.getPromotion();
      }

      PromotionResponse promotionResponse = promotionService.convertPromotionToResponse(promotion);

      BookingDetailResponse bookingDetailResponse = modelMapper.map(bookingDetail, BookingDetailResponse.class);
      bookingDetailResponse.setPromotionResponse(promotionResponse);
      bookingDetailResponse.setServiceSupplierResponse(serviceSupplierResponse);
      listBookingDetailResponses.add(bookingDetailResponse);

    }
    response.setCouple(coupleResponse);
    response.setListBookingDetail(listBookingDetailResponses);
    return response;
  }

  @Override
  public List<BookingDetailBySupplierResponse> getAllBookingBySupplier(String supplierId) {

    supplierRepository.findById(supplierId).orElseThrow(
        () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

    List<BookingDetail> listBookingDetails = bookingDetailRepository.findBySupplier(supplierId);

    List<BookingDetailBySupplierResponse> response = new ArrayList<>();

    if (listBookingDetails.size() == 0) {
      throw new EmptyException(SupplierErrorMessage.EMPTY);
    }

    for (BookingDetail bookingDetail : listBookingDetails) {
      BookingDetailBySupplierResponse bookingDetailBySupplierResponse = modelMapper.map(bookingDetail,
          BookingDetailBySupplierResponse.class);

      bookingDetailBySupplierResponse.setBookingDetailId(bookingDetail.getId());
      CoupleResponse coupleResponse = coupleService.getCoupleById(bookingDetail.getBooking().getCouple().getId());

      PromotionServiceSupplier promotionServiceSupplier = bookingDetail.getPromotionServiceSupplier();
      Promotion promotion = null;
      if (promotionServiceSupplier != null) {
        promotion = promotionServiceSupplier.getPromotion();
      }

      PromotionResponse promotionResponse = promotionService.convertPromotionToResponse(promotion);
      ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
          .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

      ServiceSupplierBySupplierBooking serviceSupplierBySupplierBooking = modelMapper.map(serviceSupplierResponse,
          ServiceSupplierBySupplierBooking.class);

      bookingDetailBySupplierResponse.setCouple(coupleResponse);
      bookingDetailBySupplierResponse.setPromotionResponse(promotionResponse);
      bookingDetailBySupplierResponse.setServiceSupplierResponse(serviceSupplierBySupplierBooking);

      response.add(bookingDetailBySupplierResponse);
    }
    return response;
  }

  @Override
  public BookingResponse cancelBooking(String bookingId) {

    Booking booking = bookingRepository.findById(bookingId).orElseThrow(
        () -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));

    List<BookingDetail> listBookingDetails = booking.getBookingDetails().stream().collect(Collectors.toList());
    List<BookingDetailResponse> listBookingDetailResponses = new ArrayList<BookingDetailResponse>();
    for (BookingDetail detail : listBookingDetails) {
      BookingDetailResponse bookingDetailResponse = bookingDetailService.cancleBookingDetail(detail.getId());
      listBookingDetailResponses.add(bookingDetailResponse);
    }
    CoupleResponse coupleResponse = coupleService.getCoupleById(booking.getCouple().getId());

    BookingResponse response = modelMapper.map(booking, BookingResponse.class);

    response.setCouple(coupleResponse);
    response.setListBookingDetail(listBookingDetailResponses);
    response.setStatus(BookingStatus.CANCLE);
    return response;
  }

  @Override
  public List<BookingResponse> getAllBookingByCouple(String coupleId) {

    Couple couple = coupleRepository.findById(coupleId).orElseThrow(
        () -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));

    List<Booking> listBookings = bookingRepository.findByCouple(couple);

    if (listBookings.size() == 0) {
      throw new EmptyException(BookingErrorMessage.EMPTY_LIST);
    }
    List<BookingResponse> response = new ArrayList<BookingResponse>();
    for (Booking booking : listBookings) {
      BookingResponse bookingResponse = convertBookingToBookingResponse(booking);
      response.add(bookingResponse);
    }

    return response;

  }

  @Override
  public List<BookingHistoryResponse> getBookingHistoryById(String bookingId) {
    Booking booking = bookingRepository.findById(bookingId).orElseThrow(
        () -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));

    List<BookingHistory> listBookings = bookingHistoryRepository.findByBookingOrderByCreatedAt(booking);

    if (listBookings.size() == 0) {
      throw new EmptyException(BookingErrorMessage.EMPTY_LIST);
    }

    List<BookingHistoryResponse> response = new ArrayList<>();

    for (BookingHistory bookingHistory : listBookings) {
      BookingHistoryResponse bookingHistoryResponse = modelMapper.map(bookingHistory,
          BookingHistoryResponse.class);
      response.add(bookingHistoryResponse);
    }
    return response;
  }

}
