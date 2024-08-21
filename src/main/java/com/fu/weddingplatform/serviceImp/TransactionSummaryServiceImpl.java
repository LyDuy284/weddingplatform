package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.payment.PaymentTypeValue;
import com.fu.weddingplatform.entity.*;
import com.fu.weddingplatform.repository.SupplierRepository;
import com.fu.weddingplatform.repository.TransactionSummaryRepository;
import com.fu.weddingplatform.response.statistic.DashboardStatistic;
import com.fu.weddingplatform.service.TransactionSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class TransactionSummaryServiceImpl implements TransactionSummaryService {
    @Autowired
    TransactionSummaryRepository transactionSummaryRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public DashboardStatistic getStaffDashboardStatistic(int month, int quarter, int year) {
        Specification<TransactionSummary> specification = buildSpecificationToStaffStatistic(month, quarter, year);
        List<TransactionSummary> transactionSummaryList = transactionSummaryRepository.findAll(specification);
        if (transactionSummaryList.isEmpty()) {
            return null;
        }
        int totalAmountCouplePaid = transactionSummaryList.stream()
                .mapToInt(TransactionSummary::getTotalAmount)
                .sum();
        int totalAmountPlatformEarn = (int) (totalAmountCouplePaid * PaymentTypeValue.PLATFORM_FEE_VALUE);
        int totalAmountSupplierEarn = (int) (totalAmountCouplePaid * PaymentTypeValue.SUPPLIER_RECEIVE_VALUE);
        return DashboardStatistic.builder()
                .totalAmountSupplierEarn(totalAmountSupplierEarn)
                .totalAmountPlatformFee(totalAmountPlatformEarn)
                .totalAmountCouplePaid(totalAmountCouplePaid)
                .build();
    }

    private Specification<TransactionSummary> buildSpecificationToStaffStatistic(int month, int quarter, int year) {
        Specification<TransactionSummary> specification = Specification.where(null);
        specification = specification.and((root, query, builder) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Expression<Date> dateCreatedAsDate = builder.function(
                    "STR_TO_DATE", Date.class,
                    root.get("dateModified"),
                    builder.literal("%Y-%m-%d")
            );
            Predicate predicate = builder.conjunction();
            if (year != 0) {
                Expression<Integer> yearExpression = builder.function("YEAR", Integer.class, dateCreatedAsDate);
                predicate = builder.and(builder.equal(yearExpression, year));
            }
            if (month != 0) {
                Expression<Integer> monthExpression = builder.function("MONTH", Integer.class, dateCreatedAsDate);
                predicate = builder.and(builder.equal(monthExpression, month));
            }
            if (quarter != 0) {
                Expression<Integer> monthExpression = builder.function("MONTH", Integer.class, dateCreatedAsDate);
                List<Integer> monthList = Arrays.asList(1, 2, 3);
                monthList = monthList.stream().map(element -> (quarter - 1) * 3 + element).toList();
                predicate = builder.and(predicate, monthExpression.in(monthList));
            }
            return predicate;
        });
        return specification;
    }
}
