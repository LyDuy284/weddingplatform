package com.fu.weddingplatform.serviceImp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fu.weddingplatform.constant.Status;
import com.fu.weddingplatform.constant.blogPost.BlogPostErrorMessage;
import com.fu.weddingplatform.constant.comboService.ComboErrorMessage;
import com.fu.weddingplatform.constant.comboService.ComboServiceStatus;
import com.fu.weddingplatform.constant.rating.RatingErrorMessage;
import com.fu.weddingplatform.constant.staff.StaffErrorMessage;
import com.fu.weddingplatform.constant.supplier.SupplierErrorMessage;
import com.fu.weddingplatform.entity.Combo;
import com.fu.weddingplatform.entity.ComboServices;
import com.fu.weddingplatform.entity.PromotionServiceSupplier;
import com.fu.weddingplatform.entity.ServiceSupplier;
import com.fu.weddingplatform.entity.Staff;
import com.fu.weddingplatform.exception.EmptyException;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.ComboRepository;
import com.fu.weddingplatform.repository.ComboServiceRepository;
import com.fu.weddingplatform.repository.PromotionServiceSupplierRepository;
import com.fu.weddingplatform.repository.ServiceSupplierRepository;
import com.fu.weddingplatform.repository.StaffRepository;
import com.fu.weddingplatform.request.combo.CreateComboService;
import com.fu.weddingplatform.request.combo.UpdateComboInfor;
import com.fu.weddingplatform.response.combo.ComboResponse;
import com.fu.weddingplatform.response.comboService.ComboServiceResponse;
import com.fu.weddingplatform.response.promotion.PromotionResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierFilterResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import com.fu.weddingplatform.service.ComboService;
import com.fu.weddingplatform.service.PromotionService;
import com.fu.weddingplatform.service.RatingService;
import com.fu.weddingplatform.service.ServiceSupplierService;
import com.fu.weddingplatform.utils.Utils;

@Service
public class ComboServiceImpl implements ComboService {

    @Autowired
    ComboRepository comboRepository;

    @Autowired
    ComboServiceRepository comboServiceRepository;

    @Autowired
    ServiceSupplierRepository serviceSupplierRepository;

    @Autowired
    RatingService ratingService;

    @Autowired
    ServiceSupplierService serviceSupplierService;

    @Autowired
    PromotionServiceSupplierRepository promotionServiceSupplierRepository;

    @Autowired
    PromotionService promotionService;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ComboResponse> getComboByFilter(String comboName, int pageNo, int pageSize, String sortBy,
            boolean isAscending) {
        Specification<Combo> specification = buildComboSpecification(comboName);
        Page<Combo> comboPage;
        if (isAscending) {
            comboPage = comboRepository.findAll(specification,
                    PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
        } else {
            comboPage = comboRepository.findAll(specification,
                    PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
        }
        return mapPageToListCombo(comboPage);
    }

    private List<ComboResponse> mapPageToListCombo(Page<Combo> comboPage) {
        List<ComboResponse> comboResponses = new ArrayList<>();
        if (comboPage.hasContent()) {
            for (Combo rating : comboPage) {
                comboResponses.add(modelMapper.map(rating, ComboResponse.class));
            }
        } else {
            throw new EmptyException(RatingErrorMessage.EMPTY_RATING_LIST);
        }
        return comboResponses;
    }

    private Specification<Combo> buildComboSpecification(String comboName) {
        Specification<Combo> specification = Specification.where(null);
        if (comboName != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + comboName + "%"));
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
                .createAt(Utils.formatVNDatetimeNow())
                .image(request.getImage())
                .build();
        Combo comboCreated = comboRepository.saveAndFlush(combo);
        List<ComboServices> comboServicesList = new ArrayList<>();
        for (String serviceSupplierID : request.getListServiceSupplierId()) {
            ServiceSupplier serviceSupplier = serviceSupplierRepository.findById(serviceSupplierID)
                    .orElseThrow(() -> new ErrorException(SupplierErrorMessage.NOT_FOUND));
            ComboServices comboService = ComboServices.builder()
                    .serviceSupplier(serviceSupplier)
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
                .orElseThrow(() -> new ErrorException(ComboErrorMessage.NOT_FOUND_BY_ID));
        combo.setName(request.getName());
        combo.setDescription(request.getDescription());
        combo.setImage(request.getImage());
        Combo comboCreated = comboRepository.save(combo);
        return modelMapper.map(comboCreated, ComboResponse.class);
    }

    @Override
    public ComboResponse updateStatusCombo(String id, String status) {
        Combo combo = comboRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ComboErrorMessage.NOT_FOUND_BY_ID));
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

    @Override
    public List<ServiceSupplierFilterResponse> getByComboServiceId(String comboId) {
        List<ServiceSupplier> listServiceSuppliers = serviceSupplierRepository.getServiceSupplierByCombo(comboId);

        if (listServiceSuppliers.size() == 0) {
            throw new EmptyException(SupplierErrorMessage.EMPTY);
        }

        List<ServiceSupplierFilterResponse> response = new ArrayList<>();

        for (ServiceSupplier serviceSupplier : listServiceSuppliers) {
            ServiceSupplierFilterResponse filterResponse = modelMapper.map(serviceSupplier,
                    ServiceSupplierFilterResponse.class);
            filterResponse.setRating(ratingService.getRatingByServiceSupplier(serviceSupplier));

            PromotionServiceSupplier promotionServiceSupplier = promotionServiceSupplierRepository
                    .findFirstByServiceSupplierAndStatus(serviceSupplier,
                            Status.ACTIVATED);

            if (promotionServiceSupplier != null) {
                PromotionResponse promotionResponse = promotionService
                        .convertPromotionToResponse(promotionServiceSupplier.getPromotion());
                filterResponse.setPromotion(promotionResponse);
            }

            List<String> images = Utils.parseStringToListImages(serviceSupplier.getImages());
            filterResponse.setListImages(images);
            response.add(filterResponse);
        }

        return response;
    }

    @Override
    public List<ComboResponse> getAllActiveCombo(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Combo> pageResult = comboRepository.findByStatus(Status.ACTIVATED,
                pageable);

        List<ComboResponse> response = new ArrayList<>();
        if (!pageResult.hasContent()) {
            throw new EmptyException(BlogPostErrorMessage.EMPTY_MESSAGE);
        }

        for (Combo combo : pageResult.getContent()) {
            ComboResponse comboResponse = modelMapper.map(combo,
                    ComboResponse.class);

            if (combo.getStaff() != null) {
                comboResponse.setStaffId(combo.getStaff().getId());
            }

            List<ComboServiceResponse> listComboServices = new ArrayList<ComboServiceResponse>();

            List<ComboServices> comboServices = combo.getComboServices().stream().collect(Collectors.toList());

            for (ComboServices comboService : comboServices) {
                ComboServiceResponse comboServiceResponse = modelMapper.map(comboService, ComboServiceResponse.class);
                ServiceSupplierResponse serviceSupplierResponse = serviceSupplierService.convertServiceSupplierToResponse(comboService.getServiceSupplier());
                comboServiceResponse.setServiceSupplier(serviceSupplierResponse);
                listComboServices.add(comboServiceResponse);
            }
            comboResponse.setComboServices(listComboServices);
            response.add(comboResponse);
        }
        return response;
    }

}
