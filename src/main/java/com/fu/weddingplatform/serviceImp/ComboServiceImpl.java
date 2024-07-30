package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.comboService.ComboErrorMessage;
import com.fu.weddingplatform.constant.comboService.ComboServiceStatus;
import com.fu.weddingplatform.constant.rating.RatingErrorMessage;
import com.fu.weddingplatform.constant.service.ServiceErrorMessage;
import com.fu.weddingplatform.constant.staff.StaffErrorMessage;
import com.fu.weddingplatform.entity.*;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.ComboRepository;
import com.fu.weddingplatform.repository.ComboServiceRepository;
import com.fu.weddingplatform.repository.ServiceRepository;
import com.fu.weddingplatform.repository.StaffRepository;
import com.fu.weddingplatform.request.combo.CreateComboService;
import com.fu.weddingplatform.request.combo.UpdateComboInfor;
import com.fu.weddingplatform.response.combo.ComboResponse;
import com.fu.weddingplatform.service.ComboService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ComboServiceImpl implements ComboService {

    @Autowired
    ComboRepository comboRepository;

    @Autowired
    ComboServiceRepository comboServiceRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public List<ComboResponse> getComboByFilter(String comboName, int pageNo, int pageSize, String sortBy, boolean isAscending) {
        Specification<Combo> specification = buildComboSpecification(comboName);
        Page<Combo> comboPage;
        if (isAscending) {
            comboPage = comboRepository.findAll(specification, PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
        } else {
            comboPage = comboRepository.findAll(specification, PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
        }
        return mapPageToListCombo(comboPage);
    }

    private List<ComboResponse> mapPageToListCombo(Page<Combo> comboPage){
        List<ComboResponse> comboResponses = new ArrayList<>();
        if (comboPage.hasContent()) {
            for (Combo rating : comboPage) {
                comboResponses.add(modelMapper.map(rating, ComboResponse.class));
            }
        } else {
            throw new ErrorException(RatingErrorMessage.EMPTY_RATING_LIST);
        }
        return comboResponses;
    }

    private Specification<Combo> buildComboSpecification(String comboName){
        Specification<Combo> specification = Specification.where(null);
        if(comboName != null ){
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + comboName + "%"));
        }
        return specification;
    }

    @Override
    @Transactional
    public ComboResponse createComboService(CreateComboService request) {
        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new ErrorException(StaffErrorMessage.NOT_FOUND));
        Combo combo = Combo.builder()
                .name(request.getName())
                .status(ComboServiceStatus.ACTIVATED)
                .description(request.getDescription())
                .staff(staff)
                .build();
        Combo comboCreated = comboRepository.saveAndFlush(combo);
        List<ComboServices> comboServicesList = new ArrayList<>();
        for (String serviceID: request.getListServiceId() ) {
            Services service = serviceRepository.findById(serviceID)
                    .orElseThrow(()  -> new ErrorException(ServiceErrorMessage.NOT_FOUND));
            ComboServices comboService = ComboServices.builder()
                    .service(service)
                    .status(ComboServiceStatus.ACTIVATED)
                    .combo(comboCreated)
                    .build();
            comboServicesList.add(comboService);
            comboServiceRepository.saveAndFlush(comboService);
        }
        comboCreated.setComboServices(comboServicesList);
        return modelMapper.map(comboCreated, ComboResponse.class);
    }

    @Override
    public ComboResponse updateComboInfor(UpdateComboInfor request) {
        Combo combo = comboRepository.findById(request.getId())
                .orElseThrow(()  -> new ErrorException(ComboErrorMessage.NOT_FOUND_BY_ID));
        combo.setName(request.getName());
        combo.setDescription(request.getDescription());
        Combo comboCreated = comboRepository.save(combo);
        return modelMapper.map(comboCreated, ComboResponse.class);
    }

    @Override
    public ComboResponse updateStatusCombo(String id, String status) {
        Combo combo = comboRepository.findById(id)
                .orElseThrow(()  -> new ErrorException(ComboErrorMessage.NOT_FOUND_BY_ID));
        combo.setStatus(status);
        Combo comboUpdated = comboRepository.save(combo);
        return modelMapper.map(comboUpdated, ComboResponse.class);
    }

    @Override
    public ComboResponse getById(String comboId) {
        Combo combo = comboRepository.findById(comboId)
                .orElseThrow(() -> new ErrorException(ComboErrorMessage.NOT_FOUND_BY_ID));
        return modelMapper.map(combo, ComboResponse.class);
    }


}
