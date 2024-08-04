package com.fu.weddingplatform.serviceImp;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.supplier.SupplierErrorMessage;
import com.fu.weddingplatform.entity.Supplier;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.SupplierRepository;
import com.fu.weddingplatform.response.Account.SupplierResponse;
import com.fu.weddingplatform.service.SupplierService;

@Service
public class SupplierServiceImp implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SupplierResponse getById(String id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(
                () -> new ErrorException(SupplierErrorMessage.NOT_FOUND));

        SupplierResponse response = modelMapper.map(supplier, SupplierResponse.class);
        response.setArea(supplier.getAreas().stream().findFirst().get());
        return response;
    }

    @Override
    public SupplierResponse convertSupplierToSupplierResponse(Supplier supplier) {
        SupplierResponse response = modelMapper.map(supplier, SupplierResponse.class);
        response.setArea(supplier.getAreas().stream().findFirst().get());
        return response;
    }

}
