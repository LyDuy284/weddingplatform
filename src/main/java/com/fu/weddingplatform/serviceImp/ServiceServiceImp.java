package com.fu.weddingplatform.serviceImp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.category.CategoryErrorMessage;
import com.fu.weddingplatform.constant.promotion.PromotionErrorMessage;
import com.fu.weddingplatform.constant.service.ServiceErrorMessage;
import com.fu.weddingplatform.constant.serviceSupplier.SupplierErrorMessage;
import com.fu.weddingplatform.constant.validation.ValidationMessage;
import com.fu.weddingplatform.entity.Category;
import com.fu.weddingplatform.entity.Promotion;
import com.fu.weddingplatform.entity.PromotionServiceEntity;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.entity.Services;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.CategoryRepository;
import com.fu.weddingplatform.repository.PromotionRepository;
import com.fu.weddingplatform.repository.PromotionServiceRepository;
import com.fu.weddingplatform.repository.ServiceRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.request.service.CreateServiceDTO;
import com.fu.weddingplatform.request.service.UpdateServiceDTO;
import com.fu.weddingplatform.response.category.CategoryResponse;
import com.fu.weddingplatform.response.promotion.PromotionByServiceResponse;
import com.fu.weddingplatform.response.service.ServiceByCategoryAndSupplierResponse;
import com.fu.weddingplatform.response.service.ServiceByCategoryResponse;
import com.fu.weddingplatform.response.service.ServiceBySupplierResponse;
import com.fu.weddingplatform.response.service.ServiceResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import com.fu.weddingplatform.service.PromotionService;
import com.fu.weddingplatform.service.ServiceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceServiceImp implements ServiceService {

        private final ServiceRepository serviceRepository;
        private final ServiceSupplierRepository serviceSupplierRepository;
        private final CategoryRepository categoryRepository;
        private final ModelMapper modelMapper;
        private final PromotionService promotionService;
        private final PromotionServiceRepository promotionServiceRepository;
        private final PromotionRepository promotionRepository;

        @Override
        public ServiceResponse createService(CreateServiceDTO createDTO) {

                ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(createDTO.getServiceSupplierId())
                                .orElseThrow(
                                                () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

                Category category = categoryRepository.findById(createDTO.getCategoryId()).orElseThrow(
                                () -> new ErrorException(CategoryErrorMessage.NOT_FOUND));

                if (createDTO.getPrice() <= 0) {
                        throw new ErrorException("Price" + ValidationMessage.GREATER_THAN_ZERO);
                }

                Services service = Services.builder()
                                .name(createDTO.getName())
                                .category(category)
                                .serviceSupplier(serviceSupplier)
                                .description(createDTO.getDescription())
                                .type(createDTO.getType())
                                .price(createDTO.getPrice())
                                .status(Status.ACTIVATED)
                                .build();

                Services serviceSaved = serviceRepository.save(service);

                List<String> listPromotionIds = Arrays.stream(createDTO.getListPromotionIds().split(","))
                                .map(String::trim)
                                .collect(Collectors.toList());
                List<PromotionByServiceResponse> lisitPromotionResponse = new ArrayList<>();
                if (listPromotionIds.size() > 0 && createDTO.getListPromotionIds().trim() != "") {
                        for (String promotionId : listPromotionIds) {
                                Promotion promotion = promotionRepository.findById(promotionId).orElseThrow(
                                                () -> new ErrorException(PromotionErrorMessage.NOT_FOUND));

                                PromotionByServiceResponse promotionResponse = modelMapper.map(promotion,
                                                PromotionByServiceResponse.class);

                                lisitPromotionResponse.add(promotionResponse);

                                PromotionServiceEntity promotionService = new PromotionServiceEntity().builder()
                                                .promotion(promotion)
                                                .service(serviceSaved).build();

                                promotionServiceRepository.save(promotionService);
                        }
                }
                ServiceResponse response = modelMapper.map(serviceSaved, ServiceResponse.class);
                CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);
                ServiceSupplierResponse serviceSupplierResponse = modelMapper.map(serviceSupplier,
                                ServiceSupplierResponse.class);
                response.setCategoryResponse(categoryResponse);
                response.setServiceSupplierResponse(serviceSupplierResponse);
                response.setPromotions(lisitPromotionResponse);

                return response;
        }

        @Override
        public ServiceResponse updateService(UpdateServiceDTO updateDTO) {

                Services service = serviceRepository.findById(updateDTO.getId()).orElseThrow(
                                () -> new ErrorException(ServiceErrorMessage.NOT_FOUND));

                Category category = categoryRepository.findById(updateDTO.getCategoryId()).orElseThrow(
                                () -> new ErrorException(CategoryErrorMessage.NOT_FOUND));

                if (updateDTO.getPrice() <= 0) {
                        throw new ErrorException("Price" + ValidationMessage.GREATER_THAN_ZERO);
                }
                service.setName(updateDTO.getName());
                service.setDescription(updateDTO.getDescription());
                service.setPrice(updateDTO.getPrice());
                service.setImages(updateDTO.getImages());
                service.setCategory(category);
                service.setType(updateDTO.getType());

                serviceRepository.save(service);

                ServiceResponse response = modelMapper.map(service, ServiceResponse.class);
                CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);
                ServiceSupplierResponse serviceSupplierResponse = modelMapper.map(
                                service.getServiceSupplier(),
                                ServiceSupplierResponse.class);
                response.setCategoryResponse(categoryResponse);
                response.setServiceSupplierResponse(serviceSupplierResponse);
                List<PromotionByServiceResponse> promotions = promotionService
                                .getAllPromotionByService(updateDTO.getId());
                response.setPromotions(promotions);
                return response;
        }

        @Override
        public ServiceResponse getServiceById(String id) {
                Services service = serviceRepository.findById(id).orElseThrow(
                                () -> new ErrorException(ServiceErrorMessage.NOT_FOUND));

                ServiceResponse response = modelMapper.map(service, ServiceResponse.class);
                CategoryResponse categoryResponse = modelMapper.map(service.getCategory(), CategoryResponse.class);
                ServiceSupplierResponse serviceSupplierResponse = modelMapper.map(service.getServiceSupplier(),
                                ServiceSupplierResponse.class);
                response.setCategoryResponse(categoryResponse);
                response.setServiceSupplierResponse(serviceSupplierResponse);
                List<PromotionByServiceResponse> promotions = promotionService.getAllPromotionByService(id);
                response.setPromotions(promotions);
                return response;
        }

        @Override
        public List<ServiceResponse> getAllServices(int pageNo, int pageSize, String sortBy, boolean isAscending) {
                List<ServiceResponse> response = new ArrayList<ServiceResponse>();
                Page<Services> servicePages;

                if (isAscending) {
                        servicePages = serviceRepository
                                        .findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
                } else {
                        servicePages = serviceRepository
                                        .findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
                }

                if (servicePages.hasContent()) {
                        for (Services service : servicePages) {
                                ServiceResponse serviceResponse = modelMapper.map(service, ServiceResponse.class);
                                CategoryResponse categoryResponse = modelMapper.map(service.getCategory(),
                                                CategoryResponse.class);
                                ServiceSupplierResponse serviceSupplierResponse = modelMapper.map(
                                                service.getServiceSupplier(),
                                                ServiceSupplierResponse.class);
                                serviceResponse.setCategoryResponse(categoryResponse);
                                serviceResponse.setServiceSupplierResponse(serviceSupplierResponse);
                                List<PromotionByServiceResponse> promotions = promotionService
                                                .getAllPromotionByService(service.getId());
                                serviceResponse.setPromotions(promotions);
                                response.add(serviceResponse);
                        }
                } else {
                        throw new ErrorException(ServiceErrorMessage.EMPTY);
                }

                return response;
        }

        @Override
        public List<ServiceResponse> getAllActivateServices(int pageNo, int pageSize, String sortBy,
                        boolean isAscending) {
                List<ServiceResponse> response = new ArrayList<ServiceResponse>();
                Page<Services> servicePages;

                if (isAscending) {
                        servicePages = serviceRepository
                                        .findByStatus(PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()),
                                                        Status.ACTIVATED);
                } else {
                        servicePages = serviceRepository.findByStatus(
                                        PageRequest.of(
                                                        pageNo, pageSize, Sort.by(sortBy).descending()),
                                        Status.ACTIVATED);
                }

                if (servicePages.hasContent()) {
                        for (Services service : servicePages) {
                                ServiceResponse serviceResponse = modelMapper.map(service, ServiceResponse.class);
                                CategoryResponse categoryResponse = modelMapper.map(service.getCategory(),
                                                CategoryResponse.class);
                                ServiceSupplierResponse serviceSupplierResponse = modelMapper.map(
                                                service.getServiceSupplier(),
                                                ServiceSupplierResponse.class);
                                serviceResponse.setCategoryResponse(categoryResponse);
                                serviceResponse.setServiceSupplierResponse(serviceSupplierResponse);
                                List<PromotionByServiceResponse> promotions = promotionService
                                                .getAllPromotionByService(service.getId());
                                serviceResponse.setPromotions(promotions);
                                response.add(serviceResponse);
                        }
                } else {
                        throw new ErrorException(ServiceErrorMessage.EMPTY);
                }

                return response;
        }

        @Override
        public ServiceResponse updateServiceStatus(String supplierId, String status) {
                return null;
        }

        @Override
        public List<ServiceBySupplierResponse> getAllServicesBySupplier(String supplierId, int pageNo, int pageSize,
                        String sortBy, boolean isAscending) {

                ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(supplierId).orElseThrow(
                                () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

                List<ServiceBySupplierResponse> response = new ArrayList<ServiceBySupplierResponse>();
                Page<Services> servicePages;

                if (isAscending) {
                        servicePages = serviceRepository
                                        .findByServiceSupplier(
                                                        PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()),
                                                        serviceSupplier);
                } else {
                        servicePages = serviceRepository
                                        .findByServiceSupplier(
                                                        PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()),
                                                        serviceSupplier);
                }

                if (servicePages.hasContent()) {
                        for (Services service : servicePages) {
                                ServiceBySupplierResponse serviceResponse = modelMapper.map(service,
                                                ServiceBySupplierResponse.class);
                                CategoryResponse categoryResponse = modelMapper.map(service.getCategory(),
                                                CategoryResponse.class);
                                serviceResponse.setCategoryResponse(categoryResponse);
                                List<PromotionByServiceResponse> promotions = promotionService
                                                .getAllPromotionByService(service.getId());
                                serviceResponse.setPromotions(promotions);
                                response.add(serviceResponse);
                        }
                } else {
                        throw new ErrorException(ServiceErrorMessage.EMPTY);
                }

                return response;
        }

        @Override
        public List<ServiceByCategoryAndSupplierResponse> getAllServicesByCategoryAndSupplier(String categoryId,
                        String supplierId, int pageNo,
                        int pageSize, String sortBy, boolean isAscending) {
                ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(supplierId).orElseThrow(
                                () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

                Category category = categoryRepository.findById(categoryId).orElseThrow(
                                () -> new ErrorException(CategoryErrorMessage.NOT_FOUND));

                List<ServiceByCategoryAndSupplierResponse> response = new ArrayList<ServiceByCategoryAndSupplierResponse>();
                Page<Services> servicePages;

                if (isAscending) {
                        servicePages = serviceRepository
                                        .findByCategoryAndServiceSupplier(
                                                        PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()),
                                                        category,
                                                        serviceSupplier);
                } else {
                        servicePages = serviceRepository
                                        .findByCategoryAndServiceSupplier(
                                                        PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()),
                                                        category,
                                                        serviceSupplier);
                }

                if (servicePages.hasContent()) {
                        for (Services service : servicePages) {
                                ServiceByCategoryAndSupplierResponse serviceResponse = modelMapper.map(service,
                                                ServiceByCategoryAndSupplierResponse.class);
                                List<PromotionByServiceResponse> promotions = promotionService
                                                .getAllPromotionByService(service.getId());
                                serviceResponse.setPromotions(promotions);
                                response.add(serviceResponse);
                        }
                } else {
                        throw new ErrorException(ServiceErrorMessage.EMPTY);
                }

                return response;
        }

        @Override
        public List<ServiceByCategoryResponse> getAllServicesByCategory(String categoryId, int pageNo, int pageSize,
                        String sortBy, boolean isAscending) {

                Category category = categoryRepository.findById(categoryId).orElseThrow(
                                () -> new ErrorException(CategoryErrorMessage.NOT_FOUND));

                List<ServiceByCategoryResponse> response = new ArrayList<ServiceByCategoryResponse>();
                Page<Services> servicePages;

                if (isAscending) {
                        servicePages = serviceRepository
                                        .findByCategory(PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()),
                                                        category);
                } else {
                        servicePages = serviceRepository
                                        .findByCategory(PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()),
                                                        category);
                }

                if (servicePages.hasContent()) {
                        for (Services service : servicePages) {
                                ServiceByCategoryResponse serviceResponse = modelMapper.map(service,
                                                ServiceByCategoryResponse.class);
                                List<PromotionByServiceResponse> promotions = promotionService
                                                .getAllPromotionByService(service.getId());
                                serviceResponse.setPromotions(promotions);
                                response.add(serviceResponse);
                                ServiceSupplierResponse serviceSupplierResponse = modelMapper.map(
                                                service.getServiceSupplier(),
                                                ServiceSupplierResponse.class);
                                serviceResponse.setServiceSupplierResponse(serviceSupplierResponse);
                                response.add(serviceResponse);
                        }
                } else {
                        throw new ErrorException(ServiceErrorMessage.EMPTY);
                }

                return response;
        }

}
