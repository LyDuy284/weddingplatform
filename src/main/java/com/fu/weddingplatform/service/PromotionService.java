package com.fu.weddingplatform.service;

import java.util.List;

import com.fu.weddingplatform.request.promotion.CreatePromotionDTO;
import com.fu.weddingplatform.response.promotion.PromotionResponse;
import com.fu.weddingplatform.response.promotion.PromotionBySupplierResponse;
import com.fu.weddingplatform.response.promotion.PromotionByServiceResponse;

public interface PromotionService {

        public PromotionResponse createPromotion(CreatePromotionDTO createDTO);

        public PromotionResponse getPromotionById(String id);

        public List<PromotionBySupplierResponse> getPromotionBySupplier(String supplierId, int pageNo, int pageSize,
                        String sortBy,
                        boolean isAscending);

        public List<PromotionByServiceResponse> getPromotionByService(String serviceId,
                        int pageNo,
                        int pageSize);

        public List<PromotionByServiceResponse> getAllPromotionByService(String serviceId);
}
