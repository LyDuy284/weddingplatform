package com.fu.weddingplatform.serviceImp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.constant.rating.RatingErrorMessage;
import com.fu.weddingplatform.constant.rating.RatingStatus;
import com.fu.weddingplatform.constant.service.ServiceErrorMessage;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Rating;
import com.fu.weddingplatform.entity.Services;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.repository.RatingRepository;
import com.fu.weddingplatform.repository.ServiceRepository;
import com.fu.weddingplatform.request.rating.CreateRatingDTO;
import com.fu.weddingplatform.request.rating.UpdateRatingDTO;
import com.fu.weddingplatform.response.couple.CoupleResponse;
import com.fu.weddingplatform.response.rating.RatingResponse;
import com.fu.weddingplatform.service.RatingService;
import com.fu.weddingplatform.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    CoupleRepository coupleRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RatingRepository ratingRepository;

    @Override
    public List<RatingResponse> getRatingByFilter(String coupleId, String serviceId, int pageNo, int pageSize, String sortBy, boolean isAscending) {
        Specification<Rating> specification = buildRatingSpecification(coupleId, serviceId);
        Page<Rating> ratingPage;
        if (isAscending) {
            ratingPage = ratingRepository.findAll(specification, PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
        } else {
            ratingPage = ratingRepository.findAll(specification, PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
        }
        return mapPageToListRating(ratingPage);
    }

    private Specification<Rating> buildRatingSpecification(String coupleId, String serviceId){
        Specification<Rating> specification = Specification.where(null);
        if(coupleId != null){
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("couple").get("id"), coupleId));
        }

        if(serviceId != null){
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("service").get("id"), serviceId));
        }
        return specification;
    }

    private List<RatingResponse> mapPageToListRating(Page<Rating> ratingPage){
        List<RatingResponse> ratingResponseList = new ArrayList<>();
        if (ratingPage.hasContent()) {
            for (Rating rating : ratingPage) {
                ratingResponseList.add(modelMapper.map(rating, RatingResponse.class));
            }
        } else {
            throw new ErrorException(RatingErrorMessage.EMPTY_RATING_LIST);
        }
        return ratingResponseList;
    }

    @Override
    public RatingResponse createRating(CreateRatingDTO request) {
        Couple couple = coupleRepository.findById(request.getCoupleId())
                .orElseThrow(() -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));
        Services service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ErrorException(ServiceErrorMessage.NOT_FOUND));
        Rating rating = Rating.builder()
                .ratingQualityValue(request.getRatingQualityValue())
                .ratingQuantityValue(request.getRatingQuantityValue())
                .ratingTimeValue(request.getRatingTimeValue())
                .description(request.getDescription())
                .service(service)
                .couple(couple)
                .status(RatingStatus.ACTIVATED)
                .dateCreated(Utils.formatVNDatetimeNow())
                .build();
        Rating ratingCreated = ratingRepository.save(rating);
        return modelMapper.map(ratingCreated, RatingResponse.class);
    }


    @Override
    public RatingResponse updateRating(UpdateRatingDTO request) {
        Rating ratingUpdate = ratingRepository.findByIdAndCouple(request.getId(), request.getCoupleId())
                .orElseThrow(() -> new ErrorException(RatingErrorMessage.NOT_FOUND_BY_COUPLE));
        ratingUpdate.setRatingQualityValue(request.getRatingQualityValue());
        ratingUpdate.setRatingQuantityValue(request.getRatingQuantityValue());
        ratingUpdate.setRatingTimeValue(request.getRatingTimeValue());
        ratingUpdate.setDescription(request.getDescription());
        Rating ratingUpdated = ratingRepository.save(ratingUpdate);
        return modelMapper.map(ratingUpdated, RatingResponse.class);
    }

    @Override
    public RatingResponse getRatingById(String id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new ErrorException(RatingErrorMessage.NOT_FOUND_BY_ID));
        return modelMapper.map(rating, RatingResponse.class);
    }

}
