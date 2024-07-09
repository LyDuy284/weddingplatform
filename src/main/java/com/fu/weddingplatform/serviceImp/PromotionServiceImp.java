package com.fu.weddingplatform.serviceImp;

import java.sql.Date;
import java.time.LocalDate;
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
import com.fu.weddingplatform.constant.promotion.PromotionErrorMessage;
import com.fu.weddingplatform.constant.service.ServiceErrorMessage;
import com.fu.weddingplatform.constant.serviceSupplier.SupplierErrorMessage;
import com.fu.weddingplatform.constant.validation.ValidationMessage;
import com.fu.weddingplatform.entity.Promotion;
import com.fu.weddingplatform.entity.PromotionServiceEntity;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.entity.Services;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.PromotionRepository;
import com.fu.weddingplatform.repository.PromotionServiceRepository;
import com.fu.weddingplatform.repository.ServiceRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.request.promotion.CreatePromotionDTO;
import com.fu.weddingplatform.response.promotion.PromotionResponse;
import com.fu.weddingplatform.response.promotion.PromotionByServiceResponse;
import com.fu.weddingplatform.response.promotion.PromotionBySupplierResponse;
import com.fu.weddingplatform.service.PromotionService;

@Service
public class PromotionServiceImp implements PromotionService {

  @Autowired
  private PromotionRepository promotionRepository;

  @Autowired
  private ServiceSupplierRepository serviceSupplierRepository;

  @Autowired
  private PromotionServiceRepository promotionServiceRepository;

  @Autowired
  private ServiceRepository serviceRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public PromotionResponse createPromotion(CreatePromotionDTO createDTO) {

    ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(createDTO.getSupplierId()).orElseThrow(
        () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate startDate = null;
    LocalDate endDate = null;

    startDate = LocalDate.parse(createDTO.getStartDate().toString(), dateFormatter);
    endDate = LocalDate.parse(createDTO.getEndDate().toString(), dateFormatter);

    if (endDate.isBefore(startDate)) {
      throw new ErrorException(ValidationMessage.START_DATE_AFTER_END_DATE);
    }

    if (createDTO.getPercent() <= 0) {
      throw new ErrorException("Percent" + ValidationMessage.GREATER_THAN_ZERO);
    }

    Promotion promotion = new Promotion().builder()
        .percent(createDTO.getPercent())
        .status(Status.ACTIVATED)
        .startDate(Date.valueOf(startDate))
        .endDate(Date.valueOf(endDate))
        .promotionDetails(createDTO.getPromotionDetails())
        .serviceSupplier(serviceSupplier)
        .build();

    Promotion promotionSaved = promotionRepository.save(promotion);

    PromotionResponse promotionResponse = modelMapper.map(promotionSaved, PromotionResponse.class);
    promotionResponse.setServiceSupplierId(serviceSupplier.getId());
    return promotionResponse;
  }

  @Override
  public PromotionResponse getPromotionById(String id) {

    Promotion promotion = promotionRepository.findById(id).orElseThrow(
        () -> new ErrorException(PromotionErrorMessage.NOT_FOUND));
    PromotionResponse response = modelMapper.map(promotion, PromotionResponse.class);
    response.setServiceSupplierId(promotion.getServiceSupplier().getId());

    List<PromotionServiceEntity> listPromotionService = promotionServiceRepository.findByPromotion(promotion);
    List<String> listServiceIds = new ArrayList<String>();
    for (PromotionServiceEntity promotionServiceEntity : listPromotionService) {
      listServiceIds.add(promotionServiceEntity.getService().getId());
    }
    response.setServices(listServiceIds);
    return response;
  }

  @Override
  public List<PromotionBySupplierResponse> getPromotionBySupplier(String supplierId, int pageNo, int pageSize,
      String sortBy,
      boolean isAscending) {

    ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(supplierId).orElseThrow(
        () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

    List<PromotionBySupplierResponse> response = new ArrayList<PromotionBySupplierResponse>();
    Page<Promotion> promotionPage;
    if (isAscending) {
      promotionPage = promotionRepository.findByServiceSupplierAndStatus(serviceSupplier, Status.ACTIVATED,
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
    } else {
      promotionPage = promotionRepository.findByServiceSupplierAndStatus(serviceSupplier, Status.ACTIVATED,
          PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
    }

    if (!promotionPage.hasContent()) {
      throw new ErrorException(PromotionErrorMessage.EMPTY);
    }

    for (Promotion promotion : promotionPage.getContent()) {

      PromotionBySupplierResponse promotionBySupplierResponse = modelMapper.map(promotion,
          PromotionBySupplierResponse.class);
      response.add(promotionBySupplierResponse);
    }

    return response;
  }

  @Override
  public List<PromotionByServiceResponse> getPromotionByService(String serviceId, int pageNo,
      int pageSize) {
    Services service = serviceRepository.findById(serviceId).orElseThrow(
        () -> new ErrorException(ServiceErrorMessage.NOT_FOUND));

    List<PromotionByServiceResponse> response = new ArrayList<PromotionByServiceResponse>();

    ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate localDate = LocalDate.now(vietnamZoneId);
    String currentDate = localDate.format(dateFormatter);
    List<Promotion> promotionResult = promotionRepository.findByService(serviceId, currentDate, pageNo * pageSize,
        pageSize);

    if (promotionResult.size() == 0) {
      throw new ErrorException(PromotionErrorMessage.EMPTY);
    }
    for (Promotion promotion : promotionResult) {
      PromotionByServiceResponse promotionResponse = modelMapper.map(promotion, PromotionByServiceResponse.class);
      response.add(promotionResponse);
    }

    return response;
  }

  @Override
  public List<PromotionByServiceResponse> getAllPromotionByService(String serviceId) {
    List<PromotionByServiceResponse> response = new ArrayList<PromotionByServiceResponse>();

    ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate localDate = LocalDate.now(vietnamZoneId);
    String currentDate = localDate.format(dateFormatter);

    List<Promotion> promotionResult = promotionRepository.findByService(serviceId, currentDate);
    for (Promotion promotion : promotionResult) {
      PromotionByServiceResponse promotionResponse = modelMapper.map(promotion, PromotionByServiceResponse.class);
      response.add(promotionResponse);
    }

    return response;
  }

}
