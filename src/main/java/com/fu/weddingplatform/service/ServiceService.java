package com.fu.weddingplatform.service;

import java.util.List;

import com.fu.weddingplatform.entity.Services;
import com.fu.weddingplatform.request.service.CreateServiceDTO;
import com.fu.weddingplatform.request.service.UpdateServiceDTO;
import com.fu.weddingplatform.response.service.ServiceResponse;

public interface ServiceService {

        public ServiceResponse createService(CreateServiceDTO createDTO);

        public ServiceResponse updateService(UpdateServiceDTO updateDTO);

        public ServiceResponse getServiceById(String id);

        public ServiceResponse convertServiceToReponse(Services service);

        public List<ServiceResponse> getAllServices();

        public List<ServiceResponse> getByCategory(String categoryId);

        // public List<ServiceResponse> getAllActivateServices(int pageNo, int pageSize,
        // String sortBy,
        // boolean isAscending);

        // public List<ServiceBySupplierResponse> getAllServicesBySupplier(String
        // supplierId, int pageNo, int pageSize,
        // String sortBy,
        // boolean isAscending);

        // public List<ServiceByCategoryAndSupplierResponse>
        // getAllServicesByCategoryAndSupplier(String categoryId,
        // String supplierId,
        // int pageNo, int pageSize,
        // String sortBy,
        // boolean isAscending);

        // public List<ServiceByCategoryResponse> getAllServicesByCategory(String
        // categoryId,
        // int pageNo, int pageSize,
        // String sortBy,
        // boolean isAscending);

        // public ServiceResponse updateServiceStatus(String supplierId, String status);

        // public List<ServiceResponse> filterService(String categoryId, String type,
        // int minPrice, int maxPrice);

}
