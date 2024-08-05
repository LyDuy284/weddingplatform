package com.fu.weddingplatform.serviceImp;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.booking.BookingStatus;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailErrorMessage;
import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.constant.rating.RatingErrorMessage;
import com.fu.weddingplatform.constant.rating.RatingStatus;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Rating;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingDetailRepository;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.repository.RatingRepository;
import com.fu.weddingplatform.repository.ServiceRepository;
import com.fu.weddingplatform.request.rating.CreateRatingDTO;
import com.fu.weddingplatform.response.rating.RatingResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import com.fu.weddingplatform.service.RatingService;
import com.fu.weddingplatform.service.ServiceSupplierService;
import com.fu.weddingplatform.utils.Utils;

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

    @Autowired
    BookingDetailRepository bookingDetailRepository;

    @Autowired
    private ServiceSupplierService serviceSupplierService;

    @Override
    public RatingResponse createRating(CreateRatingDTO request) {
        Couple couple = coupleRepository.findById(request.getCoupleId())
                .orElseThrow(() -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));

        BookingDetail bookingDetail = bookingDetailRepository.findById(request.getBookingDetailId()).orElseThrow(
                () -> new ErrorException(BookingDetailErrorMessage.NOT_FOUND));

        if (!(bookingDetail.getStatus().equalsIgnoreCase(BookingStatus.COMPLETED))) {
            throw new ErrorException(BookingDetailErrorMessage.NOT_COMPLETED);
        }

        Rating rating = Rating.builder()
                .comment(request.getDescription())
                .rating(request.getRatingValue())
                .bookingDetail(bookingDetail)
                .couple(couple)
                .status(RatingStatus.ACTIVATED)
                .dateCreated(Utils.formatVNDatetimeNow())
                .build();

        Rating ratingSaved = ratingRepository.save(rating);

        ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService
                .convertServiceSupplierToResponse(bookingDetail.getServiceSupplier());

        RatingResponse response = modelMapper.map(ratingSaved, RatingResponse.class);
        response.setServiceSupplierResponse(serviceSupplierResponse);
        return response;
    }

    @Override
    public RatingResponse getRatingById(String id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new ErrorException(RatingErrorMessage.NOT_FOUND_BY_ID));
        return modelMapper.map(rating, RatingResponse.class);
    }

    @Override
    public float getRatingByServiceSupplier(ServiceSupplier serviceSupplier) {

        float response = ratingRepository.getRatingByServiceSupplier(serviceSupplier.getId());

        return response;
    }

}
