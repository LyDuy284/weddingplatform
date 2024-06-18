package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.category.CategoryErrorMessage;
import com.fu.weddingplatform.constant.serviceSupplier.SupplierErrorMessage;
import com.fu.weddingplatform.entity.Category;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.entity.Services;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.CategoryRepository;
import com.fu.weddingplatform.repository.ServiceRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.request.service.CreateServiceDTO;
import com.fu.weddingplatform.request.service.UpdateServiceDTO;
import com.fu.weddingplatform.response.category.CategoryResponse;
import com.fu.weddingplatform.response.service.ServiceResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import com.fu.weddingplatform.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceServiceImp implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceSupplierRepository serviceSupplierRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public ServiceResponse createService(CreateServiceDTO createDTO) {

        ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(createDTO.getServiceSupplierId()).orElseThrow(
                () ->  new ErrorException(SupplierErrorMessage.NOT_FOUND)
        );

        Category category = categoryRepository.findById(createDTO.getCategoryId()).orElseThrow(
                () -> new ErrorException(CategoryErrorMessage.NOT_FOUND)
        );

        Services service = Services.builder()
                .name(createDTO.getName())
                .category(category)
                .serviceSupplier(serviceSupplier)
                .description(createDTO.getDescription())
                .price(createDTO.getPrice())
                .status(Status.ACTIVATED)
                .build();

        Services serviceSaved = serviceRepository.save(service);

        ServiceResponse response = modelMapper.map(serviceSaved, ServiceResponse.class);
        CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);
        ServiceSupplierResponse serviceSupplierResponse = modelMapper.map(serviceSupplier, ServiceSupplierResponse.class);
        response.setCategoryResponse(categoryResponse);
        response.setServiceSupplierResponse(serviceSupplierResponse);
        return response;
    }

    @Override
    public ServiceResponse updateService(UpdateServiceDTO updateDTO) {
        return null;
    }

    @Override
    public ServiceResponse getServiceById(String id) {
        return null;
    }

    @Override
    public List<ServiceResponse> getAllServices() {
        return List.of();
    }

    @Override
    public List<ServiceResponse> getAllActivateServices() {
        return List.of();
    }

    @Override
    public List<ServiceResponse> getAllServicesBySupplier(String supplierId) {
        return List.of();
    }

    @Override
    public ServiceResponse updateServiceStatus(String supplierId, String status) {
        return null;
    }
}
