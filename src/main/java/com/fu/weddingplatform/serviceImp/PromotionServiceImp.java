// package com.fu.weddingplatform.serviceImp;

// import java.sql.Date;
// import java.time.LocalDate;
// import java.time.ZoneId;
// import java.time.format.DateTimeFormatter;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.util.stream.Collectors;

// import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.fu.weddingplatform.constant.Status;
// import com.fu.weddingplatform.constant.promotion.PromotionErrorMessage;
// import com.fu.weddingplatform.constant.service.ServiceErrorMessage;
// import com.fu.weddingplatform.constant.serviceSupplier.SupplierErrorMessage;
// import com.fu.weddingplatform.constant.validation.ValidationMessage;
// import com.fu.weddingplatform.entity.Promotion;
// import com.fu.weddingplatform.entity.PromotionServiceEntity;
// import com.fu.weddingplatform.entity.ServiceSupplier;
// import com.fu.weddingplatform.entity.Services;
// import com.fu.weddingplatform.exception.ErrorException;
// import com.fu.weddingplatform.repository.PromotionRepository;
// import com.fu.weddingplatform.repository.PromotionServiceRepository;
// import com.fu.weddingplatform.repository.ServiceRepository;
// import com.fu.weddingplatform.repository.ServiceSupplierRepository;
// import com.fu.weddingplatform.request.promotion.CreatePromotionDTO;
// import com.fu.weddingplatform.response.promotion.PromotionByServiceResponse;
// import com.fu.weddingplatform.response.promotion.PromotionBySupplierResponse;
// import com.fu.weddingplatform.response.promotion.PromotionResponse;
// import com.fu.weddingplatform.service.PromotionService;

// @Service
// public class PromotionServiceImp implements PromotionService {

//   @Autowired
//   private PromotionRepository promotionRepository;

//   @Autowired
//   private ServiceSupplierRepository serviceSupplierRepository;

//   @Autowired
//   private PromotionServiceRepository promotionServiceRepository;

//   @Autowired
//   private ServiceRepository serviceRepository;

//   @Autowired
//   private ModelMapper modelMapper;

//   @Override
//   public PromotionResponse createPromotion(CreatePromotionDTO createDTO) {

//     ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(createDTO.getSupplierId()).orElseThrow(
//         () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

//     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//     LocalDate startDate = null;
//     LocalDate endDate = null;

//     startDate = LocalDate.parse(createDTO.getStartDate().toString(), dateFormatter);
//     endDate = LocalDate.parse(createDTO.getEndDate().toString(), dateFormatter);

//     if (endDate.isBefore(startDate)) {
//       throw new ErrorException(ValidationMessage.START_DATE_AFTER_END_DATE);
//     }

//     if (createDTO.getPercent() <= 0) {
//       throw new ErrorException("Percent" + ValidationMessage.GREATER_THAN_ZERO);
//     }

//     Promotion promotion = new Promotion().builder()
//         .percent(createDTO.getPercent())
//         .status(Status.ACTIVATED)
//         .startDate(Date.valueOf(startDate))
//         .endDate(Date.valueOf(endDate))
//         .promotionDetails(createDTO.getPromotionDetails())
//         .serviceSupplier(serviceSupplier)
//         .build();

//     Promotion promotionSaved = promotionRepository.save(promotion);

//     List<String> listSeviceIds = Arrays.stream(createDTO.getListServiceIds().split(","))
//         .map(String::trim)
//         .collect(Collectors.toList());
//     if (listSeviceIds.size() > 0) {
//       for (String serviceId : listSeviceIds) {
//         Services service = serviceRepository.findById(serviceId.trim()).orElseThrow(
//             () -> new ErrorException(ServiceErrorMessage.NOT_FOUND));

//         PromotionServiceEntity promotionService = new PromotionServiceEntity().builder().promotion(promotionSaved)
//             .service(service).build();
//         promotionServiceRepository.save(promotionService);
//       }
//     }

//     PromotionResponse promotionResponse = modelMapper.map(promotionSaved, PromotionResponse.class);
//     promotionResponse.setListServiceIds(listSeviceIds);
//     promotionResponse.setServiceSupplierId(serviceSupplier.getId());
//     return promotionResponse;
//   }

//   @Override
//   public PromotionResponse getPromotionById(String id) {

//     Promotion promotion = promotionRepository.findById(id).orElseThrow(
//         () -> new ErrorException(PromotionErrorMessage.NOT_FOUND));
//     PromotionResponse response = modelMapper.map(promotion, PromotionResponse.class);
//     response.setServiceSupplierId(promotion.getServiceSupplier().getId());

//     List<PromotionServiceEntity> listPromotionService = promotionServiceRepository.findByPromotion(promotion);
//     List<String> listServiceIds = new ArrayList<String>();
//     for (PromotionServiceEntity promotionServiceEntity : listPromotionService) {
//       listServiceIds.add(promotionServiceEntity.getService().getId());
//     }
//     response.setListServiceIds(listServiceIds);
//     return response;
//   }

//   @Override
//   public List<PromotionBySupplierResponse> getPromotionBySupplier(String supplierId) {

//     serviceSupplierRepository.findById(supplierId).orElseThrow(
//         () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

//     List<PromotionBySupplierResponse> response = new ArrayList<PromotionBySupplierResponse>();

//     ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
//     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//     LocalDate localDate = LocalDate.now(vietnamZoneId);
//     String currentDate = localDate.format(dateFormatter);

//     List<Promotion> listPromotions = promotionRepository.findBySupplier(supplierId, currentDate);

//     if (listPromotions.isEmpty()) {
//       throw new ErrorException(PromotionErrorMessage.EMPTY);
//     }

//     for (Promotion promotion : listPromotions) {

//       PromotionBySupplierResponse promotionBySupplierResponse = modelMapper.map(promotion,
//           PromotionBySupplierResponse.class);
//       response.add(promotionBySupplierResponse);
//     }

//     return response;
//   }

//   @Override
//   public PromotionByServiceResponse getPromotionByService(String serviceId) {

//     serviceRepository.findById(serviceId).orElseThrow(
//         () -> new ErrorException(ServiceErrorMessage.NOT_FOUND));

//     ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
//     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//     LocalDate localDate = LocalDate.now(vietnamZoneId);
//     String currentDate = localDate.format(dateFormatter);
//     Promotion promotion = promotionRepository.findByService(serviceId, currentDate);
//     PromotionByServiceResponse response = null;
//     if (promotion != null) {
//       response = modelMapper.map(promotion, PromotionByServiceResponse.class);
//     }
//     return response;
//   }

// }
