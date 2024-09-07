package com.fu.weddingplatform.serviceImp;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.promotion.PromotionErrorMessage;
import com.fu.weddingplatform.constant.promotion.PromotionType;
import com.fu.weddingplatform.constant.service.ServiceErrorMessage;
import com.fu.weddingplatform.constant.supplier.SupplierErrorMessage;
import com.fu.weddingplatform.constant.validation.ValidationMessage;
import com.fu.weddingplatform.entity.Promotion;
import com.fu.weddingplatform.entity.PromotionServiceSupplier;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.entity.Supplier;
import com.fu.weddingplatform.exception.EmptyException;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.PromotionRepository;
import com.fu.weddingplatform.repository.PromotionServiceSupplierRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.repository.SupplierRepository;
import com.fu.weddingplatform.request.promotion.CreatePromotionDTO;
import com.fu.weddingplatform.response.promotion.PromotionByServiceResponse;
import com.fu.weddingplatform.response.promotion.PromotionBySupplierResponse;
import com.fu.weddingplatform.response.promotion.PromotionResponse;
import com.fu.weddingplatform.service.PromotionService;
import com.fu.weddingplatform.utils.Utils;

@Service
@EnableScheduling

public class PromotionServiceImp implements PromotionService {

  @Autowired
  private PromotionRepository promotionRepository;

  @Autowired
  private ServiceSupplierRepository serviceSupplierRepository;

  @Autowired
  private SupplierRepository supplierRepository;

  @Autowired
  private PromotionServiceSupplierRepository promotionServiceSupplierRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public PromotionResponse createPromotion(CreatePromotionDTO createDTO) {

    Supplier supplier = supplierRepository.findById(createDTO.getSupplierId()).orElseThrow(
        () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate startDate = null;
    LocalDate endDate = null;

    startDate = LocalDate.parse(createDTO.getStartDate().toString(), dateFormatter);
    endDate = LocalDate.parse(createDTO.getEndDate().toString(), dateFormatter);

    if (startDate.isBefore(Utils.getCurrentDate())) {
      throw new ErrorException(ValidationMessage.NOT_BEFORE_CURRENT_DATE);
    }

    if (endDate.isBefore(startDate)) {
      throw new ErrorException(ValidationMessage.START_DATE_AFTER_END_DATE);
    }

    if (createDTO.getValue() <= 0) {
      throw new ErrorException(ValidationMessage.GREATER_THAN_ZERO);
    }

    if (createDTO.getType().equalsIgnoreCase(PromotionType.PERCENT)) {
      if (createDTO.getValue() >= 100) {
        throw new ErrorException(ValidationMessage.PROMOTION_LESS_THEN_100);
      }
    }

    Promotion promotion = Promotion.builder()
        .value(createDTO.getValue())
        .type(createDTO.getType())
        .status(Status.ACTIVATED)
        .startDate(Date.valueOf(startDate))
        .endDate(Date.valueOf(endDate))
        .name(createDTO.getName())
        .supplier(supplier)
        .build();
    Promotion promotionSaved = promotionRepository.save(promotion);

    // for (String serviceSupplierId : createDTO.getListServiceSupplierId()) {
    // ServiceSupplier serviceSupplier =
    // serviceSupplierRepository.findById(serviceSupplierId).orElseThrow(
    // () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

    // PromotionServiceSupplier existPromotionServiceSupplier =
    // promotionServiceSupplierRepository
    // .findFirstByServiceSupplierAndStatus(serviceSupplier, Status.ACTIVATED);

    // if (existPromotionServiceSupplier != null) {
    // existPromotionServiceSupplier.setStatus(Status.DISABLED);
    // promotionServiceSupplierRepository.save(existPromotionServiceSupplier);
    // }

    // PromotionServiceSupplier promotionServiceSupplier =
    // PromotionServiceSupplier.builder()
    // .promotion(promotionSaved)
    // .serviceSupplier(serviceSupplier)
    // .status(Status.ACTIVATED)
    // .build();

    // promotionServiceSupplierRepository.save(promotionServiceSupplier);

    // }

    PromotionResponse promotionResponse = modelMapper.map(promotionSaved, PromotionResponse.class);
    return promotionResponse;
  }

  @Override
  public PromotionResponse getPromotionById(String id) {

    Promotion promotion = promotionRepository.findById(id).orElseThrow(
        () -> new ErrorException(PromotionErrorMessage.NOT_FOUND));
    PromotionResponse response = modelMapper.map(promotion, PromotionResponse.class);

    return response;
  }

  @Override
  public List<PromotionBySupplierResponse> getPromotionBySupplier(String supplierId) {

    Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
        () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

    List<PromotionBySupplierResponse> response = new ArrayList<PromotionBySupplierResponse>();

    List<Promotion> listPromotions = promotionRepository.findBySupplierAndStatus(supplier, Status.ACTIVATED);

    if (listPromotions.isEmpty()) {
      throw new EmptyException(PromotionErrorMessage.EMPTY);
    }

    for (Promotion promotion : listPromotions) {

      PromotionBySupplierResponse promotionBySupplierResponse = modelMapper.map(promotion,
          PromotionBySupplierResponse.class);
      response.add(promotionBySupplierResponse);
    }

    return response;
  }

  @Override
  public PromotionByServiceResponse getPromotionByServiceSupplier(String serviceSupplierId) {
    ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(serviceSupplierId).orElseThrow(
        () -> new ErrorException(ServiceErrorMessage.NOT_FOUND));

    PromotionServiceSupplier promotionServiceSupplier = promotionServiceSupplierRepository
        .findFirstByServiceSupplierAndStatus(serviceSupplier, Status.ACTIVATED);

    Promotion promotion = null;

    if (promotionServiceSupplier.getId() != null) {
      promotion = promotionServiceSupplier.getPromotion();
    }

    PromotionByServiceResponse response = null;
    if (promotion != null) {
      response = modelMapper.map(promotion, PromotionByServiceResponse.class);
    }
    return response;
  }

  @Override
  @Scheduled(cron = "59 59 23 * * ?", zone = "Asia/Ho_Chi_Minh")
  // @Scheduled(cron = "0 13 17 * * ?", zone = "Asia/Ho_Chi_Minh")
  public void expriedPromotion() {

    String currentDate = Utils.getCurrentDate().toString();

    System.out.println(currentDate);

    List<Promotion> promotionList = promotionRepository.findExpriedPromotion(currentDate);

    for (Promotion promotion : promotionList) {
      for (PromotionServiceSupplier promotionServiceSupplier : promotionServiceSupplierRepository
          .findByPromotion(promotion)) {
        promotionServiceSupplier.setStatus(Status.EXPRIED);
        promotionServiceSupplierRepository.save(promotionServiceSupplier);
      }
      promotion.setStatus(Status.EXPRIED);
      promotionRepository.save(promotion);
    }
  }

  @Override
  public PromotionResponse convertPromotionToResponse(Promotion promotion) {
    if (promotion == null)
      return null;
    PromotionResponse promotionResponse = modelMapper.map(promotion, PromotionResponse.class);
    return promotionResponse;
  }

  @Override
  public boolean validPromotion(Promotion promotion) {
    if (!(promotion.getStatus().equalsIgnoreCase(Status.ACTIVATED))) {
      return false;
    }

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate startDate = null;
    LocalDate endDate = null;

    startDate = LocalDate.parse(promotion.getStartDate().toString(), dateFormatter);
    endDate = LocalDate.parse(promotion.getEndDate().toString(), dateFormatter);

    if (startDate.isAfter(Utils.getCurrentDate())) {
      return false;
    }

    if (endDate.isBefore(Utils.getCurrentDate())) {
      return false;
    }

    return true;
  }

}
