package com.fu.weddingplatform.service;

import com.fu.weddingplatform.request.feedback.CreateFeedbackDTO;
import com.fu.weddingplatform.request.feedback.UpdateFeedbackDTO;
import com.fu.weddingplatform.request.rating.CreateRatingDTO;
import com.fu.weddingplatform.request.rating.UpdateRatingDTO;
import com.fu.weddingplatform.response.feedback.FeedbackResponse;
import com.fu.weddingplatform.response.rating.RatingResponse;

import java.util.List;

public interface FeedbackService {
    List<FeedbackResponse> getAllFeedback(String coupleId, String supplierId, int pageNo, int pageSize, String sortBy, boolean isAscending);
    FeedbackResponse getFeedbackById(String id);
    FeedbackResponse createFeedback(CreateFeedbackDTO request);
    FeedbackResponse updateFeedback(UpdateFeedbackDTO request);
}
