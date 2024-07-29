package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.constant.feedback.FeedbackErrorMessage;
import com.fu.weddingplatform.constant.feedback.FeedbackStatus;
import com.fu.weddingplatform.constant.rating.RatingErrorMessage;
import com.fu.weddingplatform.constant.serviceSupplier.SupplierErrorMessage;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Feedback;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.repository.FeedbackRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.request.feedback.CreateFeedbackDTO;
import com.fu.weddingplatform.request.feedback.UpdateFeedbackDTO;
import com.fu.weddingplatform.response.feedback.FeedbackResponse;
import com.fu.weddingplatform.service.FeedbackService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    ServiceSupplierRepository serviceSupplierRepository;

    @Autowired
    CoupleRepository coupleRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<FeedbackResponse> getAllFeedback(String coupleId, String supplierId, int pageNo, int pageSize, String sortBy, boolean isAscending) {
        Specification<Feedback> specification = buildRatingSpecification(coupleId, supplierId);
        Page<Feedback> feedbackPage;
        if (isAscending) {
            feedbackPage = feedbackRepository.findAll(specification, PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
        } else {
            feedbackPage = feedbackRepository.findAll(specification, PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
        }
        return mapPageToListRating(feedbackPage);
    }

    private Specification<Feedback> buildRatingSpecification(String coupleId, String supplierId) {
        Specification<Feedback> specification = Specification.where(null);
        if(coupleId != null){
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("couple").get("id"), coupleId));
        }
        if(supplierId != null){
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("serviceSupplier").get("id"), supplierId));
        }
        return specification;
    }

    private List<FeedbackResponse> mapPageToListRating(Page<Feedback> feedbackPage){
        List<FeedbackResponse> feedbackResponseList = new ArrayList<>();
        if (feedbackPage.hasContent()) {
            for (Feedback feedback : feedbackPage) {
                feedbackResponseList.add(modelMapper.map(feedback, FeedbackResponse.class));
            }
        } else {
            throw new ErrorException(RatingErrorMessage.EMPTY_RATING_LIST);
        }
        return feedbackResponseList;
    }
    @Override
    public FeedbackResponse getFeedbackById(String id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ErrorException(FeedbackErrorMessage.NOT_FOUND_BY_ID));
        return modelMapper.map(feedback, FeedbackResponse.class);
    }

    @Override
    public FeedbackResponse createFeedback(CreateFeedbackDTO request) {
        Couple couple = coupleRepository.findById(request.getCoupleId())
                .orElseThrow(() -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));
        ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(request.getServiceSupplierId())
                .orElseThrow(() -> new ErrorException(SupplierErrorMessage.NOT_FOUND));
        Feedback feedback = Feedback.builder()
                .status(FeedbackStatus.ACTIVATED)
                .description(request.getDescription())
                .couple(couple)
                .serviceSupplier(serviceSupplier)
                .build();
        Feedback feedbackCreated = feedbackRepository.save(feedback);
        return modelMapper.map(feedbackCreated, FeedbackResponse.class);
    }

    @Override
    public FeedbackResponse updateFeedback(UpdateFeedbackDTO request) {
        Feedback feedbackUpdate = feedbackRepository.findFeedbackByCoupleAndServiceSupplier(request.getId(), request.getCoupleId(), request.getServiceSupplierId())
                .orElseThrow(() -> new ErrorException(FeedbackErrorMessage.NOT_FOUND_BY_COUPLE_AND_SUPPLIER));
        feedbackUpdate.setDescription(request.getDescription());
        Feedback feedbackUpdated = feedbackRepository.save(feedbackUpdate);
        return modelMapper.map(feedbackUpdated, FeedbackResponse.class);
    }
}
