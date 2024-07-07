package com.fu.weddingplatform.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.category.CategoryErrorMessage;
import com.fu.weddingplatform.constant.service.ServiceErrorMessage;
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

@Service
@RequiredArgsConstructor
public class ServiceServiceImp implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceSupplierRepository serviceSupplierRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public ServiceResponse createService(CreateServiceDTO createDTO) {

        ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(createDTO.getServiceSupplierId())
                .orElseThrow(
                        () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

        Category category = categoryRepository.findById(createDTO.getCategoryId()).orElseThrow(
                () -> new ErrorException(CategoryErrorMessage.NOT_FOUND));

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
        ServiceSupplierResponse serviceSupplierResponse = modelMapper.map(serviceSupplier,
                ServiceSupplierResponse.class);
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
        Services service = serviceRepository.findById(id).orElseThrow(
                () -> new ErrorException(ServiceErrorMessage.NOT_FOUND));

        ServiceResponse response = modelMapper.map(service, ServiceResponse.class);
        CategoryResponse categoryResponse = modelMapper.map(service.getCategory(), CategoryResponse.class);
        ServiceSupplierResponse serviceSupplierResponse = modelMapper.map(service.getServiceSupplier(),
                ServiceSupplierResponse.class);
        response.setCategoryResponse(categoryResponse);
        response.setServiceSupplierResponse(serviceSupplierResponse);

        return response;
    }

    @Override
    public List<ServiceResponse> getAllServices(int pageNo, int pageSize, String sortBy, boolean isAscending) {
        List<ServiceResponse> response = new ArrayList<ServiceResponse>();
        Page<Services> servicePages;

        if (isAscending) {
            servicePages = serviceRepository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
        } else {
            servicePages = serviceRepository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
        }

        if (servicePages.hasContent()) {
            for (Services service : servicePages) {
                ServiceResponse serviceResponse = modelMapper.map(service, ServiceResponse.class);
                CategoryResponse categoryResponse = modelMapper.map(service.getCategory(), CategoryResponse.class);
                ServiceSupplierResponse serviceSupplierResponse = modelMapper.map(service.getServiceSupplier(),
                        ServiceSupplierResponse.class);
                serviceResponse.setCategoryResponse(categoryResponse);
                serviceResponse.setServiceSupplierResponse(serviceSupplierResponse);
                response.add(serviceResponse);
            }
        } else {
            throw new ErrorException(ServiceErrorMessage.EMPTY);
        }

        return response;
    }

    @Override
    public List<ServiceResponse> getAllActivateServices(int pageNo, int pageSize, String sortBy, boolean isAscending) {
        List<ServiceResponse> response = new ArrayList<ServiceResponse>();
        Page<Services> servicePages;

        if (isAscending) {
            servicePages = serviceRepository
                    .findByStatus(PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()), Status.ACTIVATED);
        } else {
            servicePages = serviceRepository.findByStatus(
                    PageRequest.of(
                            pageNo, pageSize, Sort.by(sortBy).descending()),
                    Status.ACTIVATED);
        }

        if (servicePages.hasContent()) {
            for (Services service : servicePages) {
                ServiceResponse serviceResponse = modelMapper.map(service, ServiceResponse.class);
                CategoryResponse categoryResponse = modelMapper.map(service.getCategory(), CategoryResponse.class);
                ServiceSupplierResponse serviceSupplierResponse = modelMapper.map(service.getServiceSupplier(),
                        ServiceSupplierResponse.class);
                serviceResponse.setCategoryResponse(categoryResponse);
                serviceResponse.setServiceSupplierResponse(serviceSupplierResponse);
                response.add(serviceResponse);
            }
        } else {
            throw new ErrorException(ServiceErrorMessage.EMPTY);
        }

        return response;
    }

    @Override
    public List<ServiceResponse> getAllServicesBySupplier(String supplierId, int pageNo, int pageSize, String sortBy,
            boolean isAscending) {
        return List.of();
    }

    @Override
    public ServiceResponse updateServiceStatus(String supplierId, String status) {
        return null;
    }
}
