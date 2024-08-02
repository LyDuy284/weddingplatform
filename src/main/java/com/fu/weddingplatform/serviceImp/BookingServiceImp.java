package com.fu.weddingplatform.serviceImp;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.booking.BookingErrorMessage;
import com.fu.weddingplatform.constant.booking.BookingStatus;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailStatus;
import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.constant.service.ServiceErrorMessage;
import com.fu.weddingplatform.constant.serviceSupplier.SupplierErrorMessage;
import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.BookingHistory;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Quotation;
import com.fu.weddingplatform.entity.Services;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingDetailRepository;
import com.fu.weddingplatform.repository.BookingHistoryRepository;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.repository.QuotationRepository;
import com.fu.weddingplatform.repository.ServiceRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.request.booking.CreateBookingDTO;
import com.fu.weddingplatform.request.booking.QuotationBookingDTO;
import com.fu.weddingplatform.request.booking.ServiceBookingDTO;
import com.fu.weddingplatform.response.booking.BookingResponse;
import com.fu.weddingplatform.response.booking.BookingStatusResponse;
import com.fu.weddingplatform.response.booking.ServiceBookingResponse;
import com.fu.weddingplatform.response.couple.CoupleResponse;
import com.fu.weddingplatform.response.promotion.PromotionByServiceResponse;
import com.fu.weddingplatform.response.service.ServiceResponse;
import com.fu.weddingplatform.service.BookingService;
import com.fu.weddingplatform.service.CoupleService;
import com.fu.weddingplatform.service.PromotionService;
import com.fu.weddingplatform.service.ServiceService;
import com.fu.weddingplatform.utils.Utils;

@Service
public class BookingServiceImp implements BookingService {

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private BookingDetailRepository bookingDetailRepository;

  @Autowired
  private ServiceRepository serviceRepository;

  @Autowired
  private CoupleRepository coupleRepository;

  @Autowired
  private ServiceSupplierRepository serviceSupplierRepository;

  @Autowired
  private BookingHistoryRepository bookingHistoryRepository;

  @Autowired
  private QuotationRepository quotationRepository;

  @Autowired
  private ServiceService serviceService;

  @Autowired
  private CoupleService coupleService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private PromotionService promotionService;

  @Override
  public BookingResponse createBooking(CreateBookingDTO createDTO) {

    Couple couple = coupleRepository.findById(createDTO.getCoupleId()).orElseThrow(
        () -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));

    serviceSupplierRepository.findById(createDTO.getSupplierId()).orElseThrow(
        () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

    ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    LocalDate currentDate = LocalDate.now(vietnamZoneId);

    if (createDTO.getListService().size() <= 0) {
      throw new ErrorException(BookingErrorMessage.EMPTY_LIST_SERVICE);
    }

    Booking booking = new Booking().builder()
        .couple(couple)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingStatus.PENDING)
        .build();

    Booking bookingSaved = bookingRepository.save(booking);
    List<ServiceBookingResponse> listServiceBookingResponses = new ArrayList<>();

    List<BookingDetail> listBookingDetails = new ArrayList<>();
    int totalPrice = 0;
    for (ServiceBookingDTO serviceBooking : createDTO.getListService()) {
      Optional<Services> service = serviceRepository.findById(serviceBooking.getServiceId().trim());

      if (service.isEmpty()) {
        bookingRepository.delete(bookingSaved);
        throw new ErrorException(ServiceErrorMessage.NOT_FOUND);
      }

      if (!(service.get().getServiceSupplier().getId().equalsIgnoreCase(createDTO.getSupplierId()))) {
        throw new ErrorException(BookingErrorMessage.ALL_SERVICES_HAVE_THE_SAME_SUPPLIER);
      }

      LocalDate completeDate = serviceBooking.getDateCompleted().toLocalDate();

      if (currentDate.isEqual(completeDate) || currentDate.isAfter(completeDate)) {
        throw new ErrorException(BookingErrorMessage.COMPLETE_DATE_GREATER_THAN_CURRENT_DATE);
      }

      PromotionByServiceResponse promotion = promotionService.getPromotionByService(service.get().getId());

      BookingDetail bookingDetail = new BookingDetail().builder()
          .service(service.get())
          .booking(bookingSaved)
          .completedDate(completeDate.toString())
          .originalPrice(service.get().getPrice())
          .price(service.get().getPrice())
          .status(BookingDetailStatus.WAITING)
          .build();

      if (promotion != null) {
        bookingDetail.setPrice(service.get().getPrice() - service.get().getPrice() * promotion.getPercent());
      }

      listBookingDetails.add(bookingDetail);
    }

    for (QuotationBookingDTO quotationBookingDTO : createDTO.getListQuotations()) {
      Optional<Quotation> quotation = quotationRepository.findById(quotationBookingDTO.getQuoationId().trim());

      if (quotation.isEmpty()) {
        bookingRepository.delete(bookingSaved);
        throw new ErrorException(ServiceErrorMessage.NOT_FOUND);
      }

      if (!(quotation.get().getServiceSupplier().getId().equalsIgnoreCase(createDTO.getSupplierId()))) {
        throw new ErrorException(BookingErrorMessage.ALL_SERVICES_HAVE_THE_SAME_SUPPLIER);
      }

      if (!(quotation.get().getStatus().equalsIgnoreCase(Status.QUOTED))) {
        bookingRepository.delete(bookingSaved);
        throw new ErrorException(BookingErrorMessage.SERVICE_MUST_BE_QUOTED);
      }
      LocalDate completeDate = quotation.get().getEventDate().toLocalDate();

      if (currentDate.isEqual(completeDate) || currentDate.isAfter(completeDate)) {
        throw new ErrorException(BookingErrorMessage.COMPLETE_DATE_GREATER_THAN_CURRENT_DATE);
      }

      PromotionByServiceResponse promotion = promotionService
          .getPromotionByService(quotation.get().getService().getId());

      BookingDetail bookingDetail = new BookingDetail().builder()
          .service(quotation.get().getService())
          .booking(bookingSaved)
          .originalPrice(quotation.get().getPrice())
          .price(quotation.get().getPrice())
          .note(quotationBookingDTO.getNote())
          .quotation(quotation.get())
          .completedDate(completeDate.toString())
          .status(BookingDetailStatus.WAITING)
          .build();

      if (promotion != null) {
        bookingDetail.setPrice(quotation.get().getPrice() - quotation.get().getPrice() * promotion.getPercent());
      }

      listBookingDetails.add(bookingDetail);
    }

    for (BookingDetail bookingDetail : listBookingDetails) {

      BookingDetail bookingDetailSaved = bookingDetailRepository.save(bookingDetail);
      ServiceResponse serviceResponse = serviceService
          .getServiceById(bookingDetailSaved.getService().getId());

      ServiceBookingResponse serviceBookingResponse = new ServiceBookingResponse().builder()
          .service(serviceResponse)
          .bookingPrice(bookingDetail.getPrice())
          .build();

      totalPrice += bookingDetailSaved.getPrice();
      listServiceBookingResponses.add(serviceBookingResponse);

      BookingHistory bookingHistory = new BookingHistory().builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .bookingDetail(bookingDetailSaved)
          .status(bookingDetail.getStatus())
          .build();
      bookingHistoryRepository.save(bookingHistory);
    }

    BookingResponse response = modelMapper.map(bookingSaved, BookingResponse.class);
    CoupleResponse coupleResponse = coupleService.getCoupleById(couple.getId());
    response.setServiceBookings(listServiceBookingResponses);
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
  public BookingResponse updateBookingStatus(String bookingId, String status) {

    Booking booking = saveStatus(bookingId, status);

    BookingResponse response = convertBookingToBookingResponse(booking);
    return response;
  }

  @Override
  public Booking saveStatus(String bookingId, String status) {
    Booking booking = bookingRepository.findById(bookingId).orElseThrow(
        () -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));

    booking.setStatus(status);
    Booking response = bookingRepository.save(booking);

    // ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");

    // DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
    // HH:mm:ss");
    // LocalDateTime localDateTime = LocalDateTime.now(vietnamZoneId);

    // BookingHistory bookingHistory = new BookingHistory().builder()
    // .createdAt(localDateTime.format(dateTimeFormatter))
    // .booking(booking)
    // .status(status)
    // .build();
    // bookingHistoryRepository.save(bookingHistory);

    return response;
  }

  @Override
  public BookingResponse convertBookingToBookingResponse(Booking booking) {
    BookingResponse response = modelMapper.map(booking, BookingResponse.class);
    CoupleResponse coupleResponse = coupleService.getCoupleById(booking.getCouple().getId());

    int totalPrice = 0;

    List<ServiceBookingResponse> listServiceBookingResponses = new ArrayList<>();

    for (BookingDetail bookingDetail : booking.getBookingDetails()) {

      ServiceResponse serviceResponse = serviceService
          .getServiceById(bookingDetail.getService().getId());

      ServiceBookingResponse serviceBookingResponse = new ServiceBookingResponse().builder()
          .service(serviceResponse)
          .completedDate(bookingDetail.getCompletedDate())
          .originalPrice(bookingDetail.getOriginalPrice())
          .bookingPrice(bookingDetail.getPrice())
          .status(bookingDetail.getStatus())
          .build();

      totalPrice += bookingDetail.getPrice();
      listServiceBookingResponses.add(serviceBookingResponse);

    }
    response.setCouple(coupleResponse);
    response.setServiceBookings(listServiceBookingResponses);
    response.setTotalPrice(totalPrice);
    return response;
  }

  @Override
  public List<BookingResponse> getAllBookingByCouple(String coupleId, int pageNo, int pageSize, String sortBy,
      boolean isAscending) {

    Couple couple = coupleRepository.findById(coupleId).orElseThrow(
        () -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));

    Page<Booking> pageResult;

    if (isAscending) {
      pageResult = bookingRepository.findByCouple(couple,
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
    } else {
      pageResult = bookingRepository.findByCouple(couple,
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
    }

    List<BookingResponse> response = new ArrayList<>();

    if (pageResult.hasContent()) {
      for (Booking booking : pageResult.getContent()) {
        BookingResponse bookingResponse = convertBookingToBookingResponse(booking);
        response.add(bookingResponse);
      }
    } else {
      throw new ErrorException(BookingErrorMessage.EMPTY_LIST);
    }
    return response;
  }

  @Override
  public List<BookingResponse> getAllBookingBySupplier(String supplierId) {

    serviceSupplierRepository.findById(supplierId).orElseThrow(
        () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

    List<String> listBookingIds = bookingRepository.findBookingIdBySupplierId(supplierId);

    if (listBookingIds.size() == 0) {
      throw new ErrorException(BookingErrorMessage.EMPTY_LIST);
    }

    List<BookingResponse> response = new ArrayList<>();

    for (String bookingId : listBookingIds) {
      BookingResponse bookingResponse = getBookingById(bookingId);
      response.add(bookingResponse);
    }

    return response;
  }

  @Override
  public List<BookingStatusResponse> getBookingStatusById(String bookingId) {
    Booking booking = bookingRepository.findById(bookingId).orElseThrow(
        () -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));

    List<BookingStatusResponse> response = new ArrayList<BookingStatusResponse>();

    // for (BookingHistory bookingHistory : booking.getBookingHistories()) {
    // BookingStatusResponse bookingStatus = new BookingStatusResponse().builder()
    // .status(bookingHistory.getStatus())
    // .createAt(bookingHistory.getCreatedAt())
    // .build();

    // response.add(bookingStatus);
    // }

    return response;
  }

}
