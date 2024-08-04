package com.fu.weddingplatform.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.promotion.PromotionErrorMessage;
import com.fu.weddingplatform.constant.service.ServiceErrorMessage;
import com.fu.weddingplatform.constant.supplier.SupplierErrorMessage;
import com.fu.weddingplatform.constant.validation.ValidationMessage;
import com.fu.weddingplatform.entity.Promotion;
import com.fu.weddingplatform.entity.PromotionServiceSupplier;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.entity.Services;
import com.fu.weddingplatform.entity.Supplier;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.PromotionRepository;
import com.fu.weddingplatform.repository.PromotionServiceSupplierRepository;
import com.fu.weddingplatform.repository.ServiceRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.repository.SupplierRepository;
import com.fu.weddingplatform.request.serviceSupplier.CreateServiceSupplier;
import com.fu.weddingplatform.request.serviceSupplier.UpdateServiceSupplier;
import com.fu.weddingplatform.response.Account.SupplierResponse;
import com.fu.weddingplatform.response.service.ServiceResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import com.fu.weddingplatform.response.supplier.ServiceSupplierByService;
import com.fu.weddingplatform.response.supplier.ServiceSupplierBySupplierReponse;
import com.fu.weddingplatform.service.ServiceService;
import com.fu.weddingplatform.service.ServiceSupplierService;
import com.fu.weddingplatform.service.SupplierService;
import com.fu.weddingplatform.utils.Utils;

@Service
public class ServiceSupplierServiceImp implements ServiceSupplierService {

    @Autowired
    private ServiceSupplierRepository serviceSupplierRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private PromotionServiceSupplierRepository promotionServiceSupplierRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public ServiceSupplierResponse createServiceSupplier(CreateServiceSupplier createDTO) {

        Supplier supplier = supplierRepository.findById(createDTO.getSupplierId()).orElseThrow(
                () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

        Services service = serviceRepository.findById(createDTO.getServiceId()).orElseThrow(
                () -> new ErrorException(ServiceErrorMessage.NOT_FOUND));

        if (createDTO.getPrice() <= 0) {
            throw new ErrorException("Price" + ValidationMessage.GREATER_THAN_ZERO);
        }

        ServiceSupplier serviceSupplier = ServiceSupplier.builder()
                .description(createDTO.getDescription())
                .name(createDTO.getName())
                .type(createDTO.getType())
                .price(createDTO.getPrice())
                .service(service)
                .images(createDTO.getImages())
                .supplier(supplier)
                .status(Status.ACTIVATED)
                .createAt(Utils.formatVNDatetimeNow())
                .build();

        if (createDTO.getPromotionId() != null && createDTO.getPromotionId().trim() != "") {
            Promotion promotion = promotionRepository.findById(createDTO.getPromotionId()).orElseThrow(
                    () -> new ErrorException(PromotionErrorMessage.NOT_FOUND));

            PromotionServiceSupplier promotionServiceSupplier = PromotionServiceSupplier
                    .builder()
                    .serviceSupplier(serviceSupplier)
                    .promotion(promotion)
                    .build();

            promotionServiceSupplierRepository.save(promotionServiceSupplier);
        }

        ServiceSupplier serviceSupplierSaved = serviceSupplierRepository.save(serviceSupplier);
        List<String> listImages = new ArrayList<String>();
        if (createDTO.getImages() != null && createDTO.getImages() != "") {
            String[] imageArray = createDTO.getImages().split("\n,");
            for (String image : imageArray) {
                listImages.add(image.trim());
            }
        }
        ServiceSupplierResponse response = modelMapper.map(serviceSupplierSaved, ServiceSupplierResponse.class);

        ServiceResponse serviceResponse = serviceService.convertServiceToReponse(service);
        SupplierResponse supplierResponse = supplierService.convertSupplierToSupplierResponse(supplier);
        response.setSupplierResponse(supplierResponse);
        response.setServiceResponse(serviceResponse);
        response.setListImages(listImages);

        return response;
    }

    @Override
    public ServiceSupplierResponse updateServiceSupplier(UpdateServiceSupplier updateDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateServiceSupplier'");
    }

    @Override
    public ServiceSupplierResponse getServiceSupplierByID(String id) {
        ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(id).orElseThrow(
                () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));
        List<String> listImages = new ArrayList<String>();
        if (serviceSupplier.getImages() != null && serviceSupplier.getImages() != "") {
            String[] imageArray = serviceSupplier.getImages().split("\n,");
            for (String image : imageArray) {
                listImages.add(image.trim());
            }
        }
        ServiceSupplierResponse response = modelMapper.map(serviceSupplier, ServiceSupplierResponse.class);

        ServiceResponse serviceResponse = serviceService.convertServiceToReponse(serviceSupplier.getService());
        SupplierResponse supplierResponse = supplierService
                .convertSupplierToSupplierResponse(serviceSupplier.getSupplier());
        response.setSupplierResponse(supplierResponse);
        response.setServiceResponse(serviceResponse);
        response.setListImages(listImages);

        return response;
    }

    @Override
    public List<ServiceSupplierBySupplierReponse> getBySupplier(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBySupplier'");
    }

    @Override
    public List<ServiceSupplierByService> filterByService(String id, String type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'filterByService'");
    }

    @Override
    public ServiceSupplierResponse convertServiceSupplierToResponse(ServiceSupplier serviceSupplier) {
        ServiceSupplierResponse response = modelMapper.map(serviceSupplier, ServiceSupplierResponse.class);
        List<String> listImages = new ArrayList<String>();
        if (serviceSupplier.getImages() != null && serviceSupplier.getImages() != "") {
            String[] imageArray = serviceSupplier.getImages().split("\n,");
            for (String image : imageArray) {
                listImages.add(image.trim());
            }
        }
        ServiceResponse serviceResponse = serviceService.convertServiceToReponse(serviceSupplier.getService());
        SupplierResponse supplierResponse = supplierService
                .convertSupplierToSupplierResponse(serviceSupplier.getSupplier());
        response.setSupplierResponse(supplierResponse);
        response.setServiceResponse(serviceResponse);
        response.setListImages(listImages);

        return response;
    }

}
