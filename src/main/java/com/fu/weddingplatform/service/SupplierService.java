package com.fu.weddingplatform.service;

import com.fu.weddingplatform.entity.Supplier;
import com.fu.weddingplatform.response.Account.SupplierResponse;

public interface SupplierService {
    public SupplierResponse getById(String id);

    public SupplierResponse convertSupplierToSupplierResponse(Supplier supplier);
}
