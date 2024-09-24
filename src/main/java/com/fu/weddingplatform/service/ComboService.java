package com.fu.weddingplatform.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.fu.weddingplatform.request.combo.CreateComboService;
import com.fu.weddingplatform.request.combo.UpdateComboInfor;
import com.fu.weddingplatform.response.combo.ComboResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierFilterResponse;

public interface ComboService {
    List<ComboResponse> getComboByFilter(String comboName, int pageNo, int pageSize, String sortBy,
            boolean isAscending);

    @Transactional
    ComboResponse createComboService(CreateComboService request);

    ComboResponse updateComboInfor(UpdateComboInfor request);

    ComboResponse updateStatusCombo(String id, String status);

    ComboResponse getById(String comboId);

    List<ServiceSupplierFilterResponse> getByComboServiceId(String comboId);

    List<ComboResponse> getAllActiveCombo(int pageNo, int pageSize);
}
