package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.response.couple.CoupleResponse;
import com.fu.weddingplatform.service.CoupleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CoupleServiceImp implements CoupleService {

    @Autowired
    CoupleRepository coupleRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<CoupleResponse> getAllCouple(int pageNo, int pageSize, String sortBy, boolean isAscending) {
        List<CoupleResponse> coupleResponseList = new ArrayList<>();
        Page<Couple> couplePage;
        if (isAscending) {
            couplePage = coupleRepository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
        } else {
            couplePage = coupleRepository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
        }

        if (couplePage.hasContent()) {
            for (Couple couple : couplePage) {
                coupleResponseList.add(modelMapper.map(couple, CoupleResponse.class));
            }
        } else {
            throw new ErrorException(CoupleErrorMessage.EMPTY_COUPLE_LIST);
        }
        return coupleResponseList;
    }

    @Override
    public CoupleResponse getCoupleById(String coupleId) {
        Optional<Couple> coupleOptional = coupleRepository.findById(coupleId);
        if (coupleOptional.isPresent()) {
            return modelMapper.map(coupleOptional.get(), CoupleResponse.class);
        } else {
            throw new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND);
        }
    }

    @Override
    public void deleteCouple(String coupleId) {
        Optional<Couple> coupleOptional = coupleRepository.findById(coupleId);
        if (coupleOptional.isPresent()) {
            Couple couple = coupleOptional.get();
            couple.setStatus(Status.DISABLED);
            coupleRepository.save(couple);
        } else {
            throw new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND);
        }
    }
}
