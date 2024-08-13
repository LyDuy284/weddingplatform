package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.entity.Transaction;
import com.fu.weddingplatform.entity.WalletHistory;
import com.fu.weddingplatform.repository.WalletHistoryRepository;
import com.fu.weddingplatform.repository.WalletRepository;
import com.fu.weddingplatform.response.transaction.TransactionResponse;
import com.fu.weddingplatform.response.walletHistory.WalletHistoryResponse;
import com.fu.weddingplatform.service.WalletHistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Expression;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletHistoryServiceImpl implements WalletHistoryService {

    @Autowired
    private WalletHistoryRepository walletHistoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<WalletHistoryResponse> getWalletHistory(String walletId, int pageNo, int pageSize, String sortBy,
                                                        boolean isAscending, LocalDate timeFrom, LocalDate timeTo, String type) {
        Specification<WalletHistory> specification  = buildWalletSpecification(walletId, type, timeFrom, timeTo);
        Page<WalletHistory> walletHistoryPage;
        if(isAscending){
            walletHistoryPage = walletHistoryRepository.findAll(specification,
                    PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
        }else {
            walletHistoryPage = walletHistoryRepository.findAll(specification,
                    PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
        }
        return walletHistoryPage.stream().map(o ->  modelMapper.map(o, WalletHistoryResponse.class)).collect(Collectors.toList());
    }

    private Specification<WalletHistory> buildWalletSpecification(String walletId, String type, LocalDate timeFrom, LocalDate timeTo) {
        Specification<WalletHistory> specification = Specification.where(null);
        if(walletId != null){
            specification  = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("wallet").get("id"), walletId));
        }

        if(type != null){
            specification  = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type));
        }

        Specification<WalletHistory> dateSpecification = dateCompare(timeFrom, timeTo);
        if(dateSpecification != null){
            specification = specification.and(dateSpecification);
        }

        return specification;
    }

    public static Specification<WalletHistory> dateCompare(LocalDate timeFrom, LocalDate timeTo) {
        return (root, query, builder) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Expression<Date> dateCreatedAsDate = builder.function(
                    "STR_TO_DATE", Date.class,
                    root.get("createDate"),
                    builder.literal("%Y-%m-%d")
            );
            if (timeFrom != null && timeTo != null) {
                String formattedTimeFrom = timeFrom.format(formatter);
                String formattedTimeTo = timeTo.format(formatter);
                return builder.between(dateCreatedAsDate, java.sql.Date.valueOf(formattedTimeFrom), java.sql.Date.valueOf(formattedTimeTo));
            }else if(timeFrom != null){
                String formattedTimeFrom = timeFrom.format(formatter);
                return builder.greaterThanOrEqualTo(dateCreatedAsDate, java.sql.Date.valueOf(formattedTimeFrom));
            }else if (timeTo != null) {
                String formattedTimeTo = timeTo.format(formatter);
                return builder.lessThanOrEqualTo(dateCreatedAsDate, java.sql.Date.valueOf(formattedTimeTo));
            }
            return null;
        };
    }
}
