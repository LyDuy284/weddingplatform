package com.fu.weddingplatform.service;

import java.util.List;

import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.request.serviceSupplier.CreateServiceSupplier;
import com.fu.weddingplatform.request.serviceSupplier.UpdateServiceSupplier;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierBySupplierReponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import com.fu.weddingplatform.response.supplier.ServiceSupplierByService;

public interface ServiceSupplierService {
    public ServiceSupplierResponse createServiceSupplier(CreateServiceSupplier createDTO);

    public ServiceSupplierResponse updateServiceSupplier(UpdateServiceSupplier updateDTO);

    public ServiceSupplierResponse getServiceSupplierByID(String id);

    public List<ServiceSupplierBySupplierReponse> getBySupplier(String id);

    public List<ServiceSupplierByService> filterByService(String id, String type);

    public ServiceSupplierResponse convertServiceSupplierToResponse(ServiceSupplier serviceSupplier);
}
