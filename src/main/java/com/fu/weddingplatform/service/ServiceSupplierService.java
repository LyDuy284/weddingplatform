package com.fu.weddingplatform.service;

import java.util.List;

import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.request.serviceSupplier.CreateServiceSupplier;
import com.fu.weddingplatform.request.serviceSupplier.UpdateServiceSupplier;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierBySupplierReponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierFilterResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;

public interface ServiceSupplierService {
    public ServiceSupplierResponse createServiceSupplier(CreateServiceSupplier createDTO);

    public ServiceSupplierFilterResponse updateServiceSupplier(UpdateServiceSupplier updateDTO);

    public ServiceSupplierResponse getServiceSupplierByID(String id);

    public List<ServiceSupplierBySupplierReponse> getBySupplier(String id);

    public List<ServiceSupplierFilterResponse> filterByService(String categoryId, String serviceId, String type,
            int minPrice, int maxPrice, String supplierId);

    public ServiceSupplierResponse convertServiceSupplierToResponse(ServiceSupplier serviceSupplier);
}
