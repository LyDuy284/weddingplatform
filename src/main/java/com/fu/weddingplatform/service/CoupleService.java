package com.fu.weddingplatform.service;



import com.fu.weddingplatform.response.couple.CoupleResponse;

import java.util.List;

public interface CoupleService {
    List<CoupleResponse> getAllCouple(int pageSize, int size, String sortBy, boolean isAscending);

    CoupleResponse getCoupleById(String coupleId);
}
