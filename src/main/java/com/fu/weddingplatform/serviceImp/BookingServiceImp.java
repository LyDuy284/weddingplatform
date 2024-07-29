package com.fu.weddingplatform.serviceImp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.constant.service.ServiceErrorMessage;
import com.fu.weddingplatform.constant.serviceSupplier.SupplierErrorMessage;
import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.BookingHistory;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Services;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingDetailRepository;
import com.fu.weddingplatform.repository.BookingHistoryRepository;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.repository.ServiceRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.request.booking.CreateBookingDTO;
import com.fu.weddingplatform.request.booking.ServiceBookingDTO;
import com.fu.weddingplatform.response.booking.BookingResponse;
import com.fu.weddingplatform.service.BookingService;

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
  private ModelMapper modelMapper;

  @Override
  public BookingResponse createBooking(CreateBookingDTO createDTO) {

    Couple couple = coupleRepository.findById(createDTO.getCoupleId()).orElseThrow(
        () -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));

    serviceSupplierRepository.findById(createDTO.getSupplierId()).orElseThrow(
        () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

    ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime localDateTime = LocalDateTime.now(vietnamZoneId);
    LocalDate currentDate = LocalDate.now(vietnamZoneId);

    LocalDate completeDate = createDTO.getCompleteDate().toLocalDate();

    if (currentDate.isEqual(completeDate) || currentDate.isAfter(completeDate)) {
      throw new ErrorException(BookingErrorMessage.COMPLETE_DATE_GREATER_THAN_CURRENT_DATE);
    }

    if (createDTO.getListService().size() <= 0) {
      throw new ErrorException(BookingErrorMessage.EMPTY_LIST_SERVICE);
    }

    Booking booking = new Booking().builder()
        .couple(couple)
        .createdAt(localDateTime.format(dateTimeFormatter))
        .completedDate(createDTO.getCompleteDate())
        .status(BookingStatus.WAITING)
        .build();

    Booking bookingSaved = bookingRepository.save(booking);
    List<ServiceBookingDTO> serviceBookingResponse = new ArrayList<>();
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

      BookingDetail bookingDetail = new BookingDetail().builder()
          .service(service.get())
          .booking(bookingSaved)
          .price(serviceBooking.getPrice())
          .status(Status.ACTIVATED)
          .build();
      listBookingDetails.add(bookingDetail);
    }

    for (BookingDetail bookingDetail : listBookingDetails) {

      BookingDetail bookingDetailSaved = bookingDetailRepository.save(bookingDetail);
      ServiceBookingDTO serviceBookingDTO = new ServiceBookingDTO().builder()
          .serviceId(bookingDetail.getService().getId())
          .price(bookingDetailSaved.getPrice())
          .build();
      totalPrice += bookingDetailSaved.getPrice();
      serviceBookingResponse.add(serviceBookingDTO);
    }

    BookingHistory bookingHistory = new BookingHistory().builder()
        .createdAt(localDateTime.format(dateTimeFormatter))
        .booking(bookingSaved)
        .status(bookingSaved.getStatus())
        .build();
    bookingHistoryRepository.save(bookingHistory);

    BookingResponse response = modelMapper.map(bookingSaved, BookingResponse.class);
    response.setCoupleId(couple.getId());
    response.setServiceBookings(serviceBookingResponse);
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

    ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime localDateTime = LocalDateTime.now(vietnamZoneId);

    BookingHistory bookingHistory = new BookingHistory().builder()
        .createdAt(localDateTime.format(dateTimeFormatter))
        .booking(booking)
        .status(status)
        .build();
    bookingHistoryRepository.save(bookingHistory);

    return response;
  }

  @Override
  public BookingResponse convertBookingToBookingResponse(Booking booking) {
    BookingResponse response = modelMapper.map(booking, BookingResponse.class);
    response.setCoupleId(booking.getCouple().getId());

    int totalPrice = 0;

    List<ServiceBookingDTO> serviceBookingResponse = new ArrayList<>();

    for (BookingDetail bookingDetail : booking.getBookingDetails()) {
      ServiceBookingDTO serviceBookingDTO = new ServiceBookingDTO().builder()
          .serviceId(bookingDetail.getService().getId())
          .price(bookingDetail.getPrice())
          .build();

      totalPrice += bookingDetail.getPrice();
      serviceBookingResponse.add(serviceBookingDTO);
    }

    response.setServiceBookings(serviceBookingResponse);
    response.setTotalPrice(totalPrice);
    return response;
  }

  @Override
  public List<BookingResponse> getAllBookingBySupplier(String supplierId, int pageNo, int pageSize, String sortBy,
      boolean isAscending) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllBookingBySupplier'");
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

}
