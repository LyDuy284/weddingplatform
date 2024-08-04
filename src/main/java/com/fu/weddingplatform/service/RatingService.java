package com.fu.weddingplatform.service;

import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.request.rating.CreateRatingDTO;
import com.fu.weddingplatform.response.rating.RatingResponse;

public interface RatingService {

    RatingResponse getRatingById(String id);

    RatingResponse createRating(CreateRatingDTO request);

    float getRatingByServiceSupplier(ServiceSupplier serviceSupplier);

}
