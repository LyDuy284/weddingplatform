package com.fu.weddingplatform.service;

import com.fu.weddingplatform.response.couple.CoupleResponse;

import java.util.List;

public interface CoupleService {
    List<CoupleResponse> getAllCouple(int pageNo, int pageSize, String sortBy, boolean isAscending);

    CoupleResponse getCoupleById(String coupleId);

    void deleteCouple(String coupleId);
}
