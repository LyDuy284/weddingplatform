package com.fu.weddingplatform.service;

import java.util.List;

import com.fu.weddingplatform.entity.Promotion;
import com.fu.weddingplatform.request.promotion.CreatePromotionDTO;
import com.fu.weddingplatform.response.promotion.PromotionResponse;
import com.fu.weddingplatform.response.promotion.PromotionBySupplierResponse;
import com.fu.weddingplatform.response.promotion.PromotionByServiceResponse;

public interface PromotionService {

        public PromotionResponse createPromotion(CreatePromotionDTO createDTO);

        public PromotionResponse getPromotionById(String id);

        public List<PromotionBySupplierResponse> getPromotionBySupplier(String supplierId);

        public PromotionByServiceResponse getPromotionByServiceSupplier(String serviceSupplierId);

        public PromotionResponse convertPromotionToResponse(Promotion promotion);

        public void expriedPromotion();

        public boolean validPromotion(Promotion promotion);

}
