package com.fu.weddingplatform.service;

import com.fu.weddingplatform.request.rating.CreateRatingDTO;
import com.fu.weddingplatform.request.rating.UpdateRatingDTO;
import com.fu.weddingplatform.response.rating.RatingResponse;

import java.util.List;

public interface RatingService {
    List<RatingResponse> getRatingByFilter(String coupleId, String serviceId, int pageNo, int pageSize, String sortBy, boolean isAscending);
    RatingResponse getRatingById(String id);
    RatingResponse createRating(CreateRatingDTO request);
    RatingResponse updateRating(UpdateRatingDTO request);

}
