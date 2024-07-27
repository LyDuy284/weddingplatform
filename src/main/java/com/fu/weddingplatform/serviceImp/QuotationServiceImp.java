package com.fu.weddingplatform.serviceImp;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.constant.quotation.QuotationErrorMessage;
import com.fu.weddingplatform.constant.service.ServiceErrorMessage;
import com.fu.weddingplatform.constant.serviceSupplier.SupplierErrorMessage;
import com.fu.weddingplatform.constant.validation.ValidationMessage;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Quotation;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.entity.Services;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.repository.QuotationRepository;
import com.fu.weddingplatform.repository.ServiceRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.request.quotation.CreateQuoteRequestDTO;
import com.fu.weddingplatform.response.quotation.QuotationByCoupleResponse;
import com.fu.weddingplatform.response.quotation.QuotationBySupplierResponse;
import com.fu.weddingplatform.response.quotation.QuotationResponse;
import com.fu.weddingplatform.service.QuotationService;

@Service
public class QuotationServiceImp implements QuotationService {

  @Autowired
  private CoupleRepository coupleRepository;

  @Autowired
  private ServiceSupplierRepository serviceSupplierRepository;

  @Autowired
  private ServiceRepository serviceRepository;

  @Autowired
  private QuotationRepository quotationRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public QuotationResponse createQuoteResquest(CreateQuoteRequestDTO createDTO) {

    Couple couple = coupleRepository.findById(createDTO.getCoupleId()).orElseThrow(
        () -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));

    ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(createDTO.getSupplierId()).orElseThrow(
        () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

    Services service = serviceRepository.findById(createDTO.getServiceId()).orElseThrow(
        () -> new ErrorException(ServiceErrorMessage.NOT_FOUND));

    ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime localDateTime = LocalDateTime.now(vietnamZoneId);
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate now = LocalDate.now(vietnamZoneId);
    LocalDate eventDate = LocalDate.parse(createDTO.getEventDate().toString(), dateFormatter);

    if (eventDate.isBefore(now)) {
      throw new ErrorException(ValidationMessage.NOT_BEFORE_CURRENT_DATE);
    }

    Quotation quotation = new Quotation().builder()
        .couple(couple)
        .service(service)
        .serviceSupplier(serviceSupplier)
        .status(Status.WAITING)
        .message(createDTO.getMessage())
        .eventDate(Date.valueOf(eventDate))
        .createAt(localDateTime.format(dateTimeFormatter))
        .build();

    Quotation quotationSaved = quotationRepository.save(quotation);

    QuotationResponse response = modelMapper.map(quotationSaved, QuotationResponse.class);
    response.setCoupleId(createDTO.getCoupleId());
    response.setServiceId(createDTO.getServiceId());
    response.setServiceSupplierId(createDTO.getSupplierId());
    return response;
  }

  @Override
  public QuotationResponse quoteService(String quotationId, int price) {

    Quotation quotation = quotationRepository.findById(quotationId).orElseThrow(
        () -> new ErrorException(QuotationErrorMessage.NOT_FOUND));

    if (quotation.getStatus().equalsIgnoreCase(Status.QUOTED)) {
      throw new ErrorException(QuotationErrorMessage.IS_QUOTED);
    }

    if (price <= 0) {
      throw new ErrorException("price" + ValidationMessage.GREATER_THAN_ZERO);
    }

    quotation.setStatus(Status.QUOTED);
    quotation.setPrice(price);

    quotationRepository.save(quotation);

    QuotationResponse response = modelMapper.map(quotation, QuotationResponse.class);
    response.setCoupleId(quotation.getCouple().getId());
    response.setServiceId(quotation.getService().getId());
    response.setServiceSupplierId(quotation.getServiceSupplier().getId());

    return response;
  }

  @Override
  public List<QuotationByCoupleResponse> getQuoteRequestByCouple(String coupleId, int pageNo, int pageSize,
      String sortBy, boolean isAscending) {

    Couple couple = coupleRepository.findById(coupleId).orElseThrow(
        () -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));
    List<QuotationByCoupleResponse> response = new ArrayList<>();
    Page<Quotation> pageResult;

    if (isAscending) {
      pageResult = quotationRepository.findByCouple(couple,
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
    } else {
      pageResult = quotationRepository.findByCouple(couple,
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
    }

    if (pageResult.isEmpty()) {
      throw new ErrorException(QuotationErrorMessage.EMPTY);
    }

    for (Quotation quotation : pageResult.getContent()) {
      QuotationByCoupleResponse quotationByCoupleResponse = modelMapper.map(quotation, QuotationByCoupleResponse.class);
      quotationByCoupleResponse.setServiceId(quotation.getService().getId());
      quotationByCoupleResponse.setServiceSupplierId(quotation.getServiceSupplier().getId());
      response.add(quotationByCoupleResponse);
    }

    return response;
  }

  @Override
  public List<QuotationBySupplierResponse> getQuoteRequestBySupplier(String supplierId, int pageNo, int pageSize,
      String sortBy, boolean isAscending) {

    ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(supplierId).orElseThrow(
        () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

    List<QuotationBySupplierResponse> response = new ArrayList<>();
    Page<Quotation> pageResult;

    if (isAscending) {
      pageResult = quotationRepository.findByServiceSupplier(serviceSupplier,
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
    } else {
      pageResult = quotationRepository.findByServiceSupplier(serviceSupplier,
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
    }

    if (pageResult.isEmpty()) {
      throw new ErrorException(QuotationErrorMessage.EMPTY);
    }

    for (Quotation quotation : pageResult.getContent()) {
      QuotationBySupplierResponse quotationByCoupleResponse = modelMapper.map(quotation,
          QuotationBySupplierResponse.class);
      quotationByCoupleResponse.setServiceId(quotation.getService().getId());
      quotationByCoupleResponse.setCoupleId(quotation.getCouple().getId());
      response.add(quotationByCoupleResponse);
    }

    return response;

  }

}
