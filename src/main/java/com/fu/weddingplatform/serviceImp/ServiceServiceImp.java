package com.fu.weddingplatform.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.category.CategoryErrorMessage;
import com.fu.weddingplatform.constant.service.ServiceErrorMessage;
import com.fu.weddingplatform.entity.Category;
import com.fu.weddingplatform.entity.Services;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.CategoryRepository;
import com.fu.weddingplatform.repository.ServiceRepository;
import com.fu.weddingplatform.request.service.CreateServiceDTO;
import com.fu.weddingplatform.request.service.UpdateServiceDTO;
import com.fu.weddingplatform.response.category.CategoryResponse;
import com.fu.weddingplatform.response.service.ServiceResponse;
import com.fu.weddingplatform.service.ServiceService;
import com.fu.weddingplatform.utils.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceServiceImp implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public ServiceResponse createService(CreateServiceDTO createDTO) {

        Category category = categoryRepository.findById(createDTO.getCategoryId()).orElseThrow(
                () -> new ErrorException(CategoryErrorMessage.NOT_FOUND));

        Services service = Services.builder()
                .name(createDTO.getName())
                .category(category)
                .description(createDTO.getDescription())
                .images(createDTO.getImages())
                .createAt(Utils.formatVNDatetimeNow())
                .status(Status.ACTIVATED)
                .build();

        Services serviceSaved = serviceRepository.save(service);

        List<String> listImages = new ArrayList<String>();
        if (serviceSaved.getImages() != null
                && !(serviceSaved.getImages().trim().equalsIgnoreCase(""))) {
            String[] imageArray = serviceSaved.getImages().split("\n,");
            for (String image : imageArray) {
                if (image.trim() != "") {
                    listImages.add(image.trim());
                }
            }
        }
        ServiceResponse response = modelMapper.map(serviceSaved, ServiceResponse.class);
        CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);

        response.setCategoryResponse(categoryResponse);
        response.setListImages(listImages);
        return response;
    }

    @Override
    public ServiceResponse updateService(UpdateServiceDTO updateDTO) {

        Services service = serviceRepository.findById(updateDTO.getId()).orElseThrow(
                () -> new ErrorException(ServiceErrorMessage.NOT_FOUND));

        Category category = service.getCategory();
        service.setName(updateDTO.getName());
        service.setDescription(updateDTO.getDescription());
        service.setImages(updateDTO.getImages());

        serviceRepository.save(service);
        List<String> listImages = new ArrayList<String>();
        if (service.getImages() != null && !(service.getImages().trim().equalsIgnoreCase(""))) {
            String[] imageArray = service.getImages().split("\n,");
            for (String image : imageArray) {
                if (image.trim() != "") {
                    listImages.add(image.trim());
                }
            }
        }
        ServiceResponse response = modelMapper.map(service, ServiceResponse.class);
        CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);
        response.setCategoryResponse(categoryResponse);
        response.setListImages(listImages);
        return response;
    }

    @Override
    public ServiceResponse getServiceById(String id) {
        Services service = serviceRepository.findById(id).orElseThrow(
                () -> new ErrorException(ServiceErrorMessage.NOT_FOUND));
        ServiceResponse response = convertServiceToReponse(service);
        return response;
    }

    @Override
    public ServiceResponse convertServiceToReponse(Services service) {
        if (service == null) {
            return null;
        }
        List<String> listImages = new ArrayList<String>();
        if (service.getImages() != null &&
                !(service.getImages().trim().equalsIgnoreCase(""))) {
            String[] imageArray = service.getImages().split("\n,");
            for (String image : imageArray) {
                if (image.trim() != "") {
                    listImages.add(image.trim());
                }
            }
        }
        ServiceResponse response = modelMapper.map(service, ServiceResponse.class);
        response.setListImages(listImages);
        CategoryResponse categoryResponse = modelMapper.map(service.getCategory(),
                CategoryResponse.class);
        response.setCategoryResponse(categoryResponse);
        return response;
    }

    @Override
    public List<ServiceResponse> getAllServices() {

        List<Services> listServices = serviceRepository.findByStatus(Status.ACTIVATED);

        List<ServiceResponse> response = new ArrayList<>();

        for (Services service : listServices) {
            ServiceResponse serviceResponse = convertServiceToReponse(service);
            response.add(serviceResponse);
        }

        return response;

    }

    @Override
    public List<ServiceResponse> getByCategory(String categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ErrorException(CategoryErrorMessage.NOT_FOUND));

        List<Services> listServices = serviceRepository.findByCategory(category);

        List<ServiceResponse> response = new ArrayList<>();

        for (Services service : listServices) {
            ServiceResponse serviceResponse = convertServiceToReponse(service);
            response.add(serviceResponse);
        }

        return response;
    }

}
