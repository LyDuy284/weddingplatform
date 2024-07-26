package com.fu.weddingplatform.service;

import com.fu.weddingplatform.request.service.CreateServiceDTO;
import com.fu.weddingplatform.request.service.UpdateServiceDTO;
import com.fu.weddingplatform.response.service.ServiceByCategoryAndSupplierResponse;
import com.fu.weddingplatform.response.service.ServiceByCategoryResponse;
import com.fu.weddingplatform.response.service.ServiceBySupplierResponse;
import com.fu.weddingplatform.response.service.ServiceResponse;

import java.util.List;

public interface ServiceService {

        public ServiceResponse createService(CreateServiceDTO createDTO);

        public ServiceResponse updateService(UpdateServiceDTO updateDTO);

        public ServiceResponse getServiceById(String id);

        public List<ServiceResponse> getAllServices(int pageNo, int pageSize, String sortBy, boolean isAscending);

        public List<ServiceResponse> getAllActivateServices(int pageNo, int pageSize, String sortBy,
                        boolean isAscending);

        public List<ServiceBySupplierResponse> getAllServicesBySupplier(String supplierId, int pageNo, int pageSize,
                        String sortBy,
                        boolean isAscending);

        public List<ServiceByCategoryAndSupplierResponse> getAllServicesByCategoryAndSupplier(String categoryId,
                        String supplierId,
                        int pageNo, int pageSize,
                        String sortBy,
                        boolean isAscending);

        public List<ServiceByCategoryResponse> getAllServicesByCategory(String categoryId,
                        int pageNo, int pageSize,
                        String sortBy,
                        boolean isAscending);

        public ServiceResponse updateServiceStatus(String supplierId, String status);

}
