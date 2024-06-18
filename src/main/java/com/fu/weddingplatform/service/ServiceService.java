package com.fu.weddingplatform.service;

import com.fu.weddingplatform.request.service.CreateServiceDTO;
import com.fu.weddingplatform.request.service.UpdateServiceDTO;
import com.fu.weddingplatform.response.service.ServiceResponse;

import java.util.List;

public interface ServiceService {

    public ServiceResponse createService(CreateServiceDTO createDTO);

    public ServiceResponse updateService(UpdateServiceDTO updateDTO);

    public ServiceResponse getServiceById(String id);

    public List<ServiceResponse> getAllServices();

    public List<ServiceResponse> getAllActivateServices();

    public List<ServiceResponse> getAllServicesBySupplier(String supplierId);

    public ServiceResponse updateServiceStatus(String supplierId, String status);

}
