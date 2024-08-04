package com.fu.weddingplatform.serviceImp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.BookingDetailHistory;
import com.fu.weddingplatform.entity.BookingHistory;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Invoice;
import com.fu.weddingplatform.entity.InvoiceDetail;
import com.fu.weddingplatform.entity.Promotion;
import com.fu.weddingplatform.entity.PromotionServiceSupplier;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingDetailHistoryRepository;
import com.fu.weddingplatform.repository.BookingDetailRepository;
import com.fu.weddingplatform.repository.BookingHistoryRepository;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.repository.InvoiceDetailRepository;
import com.fu.weddingplatform.repository.InvoiceRepository;
import com.fu.weddingplatform.repository.PromotionServiceSupplierRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.request.booking.CreateBookingDTO;
import com.fu.weddingplatform.request.booking.ServiceSupplierBookingDTO;
import com.fu.weddingplatform.response.booking.BookingDetailResponse;
import com.fu.weddingplatform.response.booking.BookingResponse;
import com.fu.weddingplatform.response.couple.CoupleResponse;
import com.fu.weddingplatform.response.promotion.PromotionResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import com.fu.weddingplatform.service.BookingService;
import com.fu.weddingplatform.service.CoupleService;
import com.fu.weddingplatform.service.PromotionService;
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
  private InvoiceRepository invoiceRepository;

  @Autowired
  private InvoiceDetailRepository invoiceDetailRepository;

  @Autowired
  private PromotionService promotionService;

  @Autowired
  private PromotionServiceSupplierRepository promotionServiceSupplierRepository;

  @Override
  public BookingResponse createBooking(CreateBookingDTO createDTO) {

    Couple couple = coupleRepository.findById(createDTO.getCoupleId()).orElseThrow(
        () -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));

    if (createDTO.getListServiceSupplier().size() <= 0) {
      throw new ErrorException(BookingErrorMessage.EMPTY_LIST_SERVICE);
    }

    LocalDate currentDate = Utils.getCurrentDate();

    Booking booking = Booking.builder()
        .couple(couple)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(BookingStatus.PENDING)
        .build();

    Booking bookingSaved = bookingRepository.save(booking);

    List<BookingDetailResponse> listBookingDetailResponse = new ArrayList<>();

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
      BookingDetail bookingDetail = BookingDetail.builder()
          .booking(bookingSaved)
          .serviceSupplier(serviceSupplier.get())
          .completedDate(completeDate.toString())
          .note(serviceSupplierBookingDTO.getNote())
          .price(serviceSupplier.get().getPrice())
          .status(BookingDetailStatus.PENDING)
          .build();

      totalPrice += serviceSupplier.get().getPrice();

      listBookingDetails.add(bookingDetail);
    }

    bookingSaved.setTotalPrice(totalPrice);
    bookingRepository.save(bookingSaved);
    Invoice invoice = Invoice.builder()
        .createAt(Utils.formatVNDatetimeNow())
        .booking(bookingSaved)
        .status(Status.PENDING)
        .build();

    Invoice invoiceSaved = invoiceRepository.save(invoice);

    int invoicePrice = 0;

    for (BookingDetail bookingDetail : listBookingDetails) {
      BookingDetail bookingDetailSaved = bookingDetailRepository.save(bookingDetail);

      PromotionServiceSupplier promotionServiceSupplier = promotionServiceSupplierRepository
          .findFirstByServiceSupplierAndStatus(bookingDetail.getServiceSupplier(),
              Status.ACTIVATED);

      Promotion promotion = null;
      int price = 0;
      if (promotionServiceSupplier != null) {
        promotion = promotionServiceSupplier.getPromotion();

        switch (promotion.getType()) {
          case PromotionType.PERCENT:
            price = (int) (bookingDetail.getPrice() * (100 - promotion.getValue()) * 0.01);
            break;
          case PromotionType.MONEY:
            price = bookingDetail.getPrice() - promotion.getValue();
            break;
          default:
            break;
        }
      } else {
        price = bookingDetail.getPrice();
      }

      invoicePrice += price;

      BookingDetailHistory bookingDetailHistory = BookingDetailHistory
          .builder()
          .createdAt(Utils.formatVNDatetimeNow())
          .status(bookingDetail.getStatus())
          .bookingDetail(bookingDetailSaved)
          .build();

      bookingDetailHistoryRepository.save(bookingDetailHistory);

      InvoiceDetail invoiceDetail = InvoiceDetail.builder()
          .bookingDetail(bookingDetailSaved)
          .createAt(Utils.formatVNDatetimeNow())
          .price(invoicePrice)
          .status(Status.PENDING)
          .invoice(invoiceSaved)
          .promotionServiceSupplier(promotionServiceSupplier)
          .build();

      invoiceDetailRepository.save(invoiceDetail);

      ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
          .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

      PromotionResponse promotionResponse = promotionService.convertPromotionToResponse(promotion);

      BookingDetailResponse bookingDetailResponse = modelMapper.map(bookingDetail, BookingDetailResponse.class);
      bookingDetailResponse.setPromotionResponse(promotionResponse);
      bookingDetailResponse.setServiceSupplierResponse(serviceSupplierResponse);
      listBookingDetailResponse.add(bookingDetailResponse);

    }

    invoiceSaved.setTotalPrice(invoicePrice);
    invoiceRepository.save(invoiceSaved);

    BookingHistory bookingHistory = BookingHistory.builder()
        .booking(bookingSaved)
        .createdAt(Utils.formatVNDatetimeNow())
        .status(Status.ACTIVATED)
        .build();

    bookingHistoryRepository.save(bookingHistory);

    BookingResponse response = modelMapper.map(bookingSaved,
        BookingResponse.class);
    CoupleResponse coupleResponse = coupleService.getCoupleById(couple.getId());
    response.setListBookingDetail(listBookingDetailResponse);
    response.setCouple(coupleResponse);
    response.setTotalPrice(totalPrice);

    return response;
  }

  // @Override
  // public BookingResponse getBookingById(String bookingId) {
  // Booking booking = bookingRepository.findById(bookingId).orElseThrow(
  // () -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));
  // BookingResponse response = convertBookingToBookingResponse(booking);
  // return response;
  // }

  // @Override
  // public BookingResponse updateBookingStatus(String bookingId, String status) {

  // Booking booking = saveStatus(bookingId, status);

  // BookingResponse response = convertBookingToBookingResponse(booking);
  // return response;
  // }

  // @Override
  // public Booking saveStatus(String bookingId, String status) {
  // Booking booking = bookingRepository.findById(bookingId).orElseThrow(
  // () -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));

  // booking.setStatus(status);
  // Booking response = bookingRepository.save(booking);

  // // ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");

  // // DateTimeFormatter dateTimeFormatter =
  // DateTimeFormatter.ofPattern("yyyy-MM-dd
  // // HH:mm:ss");
  // // LocalDateTime localDateTime = LocalDateTime.now(vietnamZoneId);

  // // BookingHistory bookingHistory = new BookingHistory().builder()
  // // .createdAt(localDateTime.format(dateTimeFormatter))
  // // .booking(booking)
  // // .status(status)
  // // .build();
  // // bookingHistoryRepository.save(bookingHistory);

  // return response;
  // }

  // @Override
  // public BookingResponse convertBookingToBookingResponse(Booking booking) {
  // BookingResponse response = modelMapper.map(booking, BookingResponse.class);
  // CoupleResponse coupleResponse =
  // coupleService.getCoupleById(booking.getCouple().getId());

  // int totalPrice = 0;

  // List<ServiceBookingResponse> listServiceBookingResponses = new ArrayList<>();

  // for (BookingDetail bookingDetail : booking.getBookingDetails()) {

  // // ServiceResponse serviceResponse = serviceService
  // // .getServiceById(bookingDetail.getService().getId());

  // ServiceBookingResponse serviceBookingResponse = new
  // ServiceBookingResponse().builder()
  // // .service(serviceResponse)
  // .completedDate(bookingDetail.getCompletedDate())
  // .originalPrice(bookingDetail.getOriginalPrice())
  // .bookingPrice(bookingDetail.getPrice())
  // .status(bookingDetail.getStatus())
  // .build();

  // totalPrice += bookingDetail.getPrice();
  // listServiceBookingResponses.add(serviceBookingResponse);

  // }
  // response.setCouple(coupleResponse);
  // response.setServiceBookings(listServiceBookingResponses);
  // response.setTotalPrice(totalPrice);
  // return response;
  // }

  // @Override
  // public List<BookingResponse> getAllBookingByCouple(String coupleId, int
  // pageNo, int pageSize, String sortBy,
  // boolean isAscending) {

  // Couple couple = coupleRepository.findById(coupleId).orElseThrow(
  // () -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));

  // Page<Booking> pageResult;

  // if (isAscending) {
  // pageResult = bookingRepository.findByCouple(couple,
  // PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
  // } else {
  // pageResult = bookingRepository.findByCouple(couple,
  // PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
  // }

  // List<BookingResponse> response = new ArrayList<>();

  // if (pageResult.hasContent()) {
  // for (Booking booking : pageResult.getContent()) {
  // BookingResponse bookingResponse = convertBookingToBookingResponse(booking);
  // response.add(bookingResponse);
  // }
  // } else {
  // throw new ErrorException(BookingErrorMessage.EMPTY_LIST);
  // }
  // return response;
  // }

  // @Override
  // public List<BookingResponse> getAllBookingBySupplier(String supplierId) {

  // serviceSupplierRepository.findById(supplierId).orElseThrow(
  // () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

  // List<String> listBookingIds =
  // bookingRepository.findBookingIdBySupplierId(supplierId);

  // if (listBookingIds.size() == 0) {
  // throw new ErrorException(BookingErrorMessage.EMPTY_LIST);
  // }

  // List<BookingResponse> response = new ArrayList<>();

  // for (String bookingId : listBookingIds) {
  // BookingResponse bookingResponse = getBookingById(bookingId);
  // response.add(bookingResponse);
  // }

  // return response;
  // }

  // @Override
  // public List<BookingStatusResponse> getBookingStatusById(String bookingId) {
  // Booking booking = bookingRepository.findById(bookingId).orElseThrow(
  // () -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));

  // List<BookingStatusResponse> response = new
  // ArrayList<BookingStatusResponse>();

  // // for (BookingHistory bookingHistory : booking.getBookingHistories()) {
  // // BookingStatusResponse bookingStatus = new
  // BookingStatusResponse().builder()
  // // .status(bookingHistory.getStatus())
  // // .createAt(bookingHistory.getCreatedAt())
  // // .build();

  // // response.add(bookingStatus);
  // // }

  // return response;
  // }

}
