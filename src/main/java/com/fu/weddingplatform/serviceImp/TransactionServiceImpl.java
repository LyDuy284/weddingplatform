package com.fu.weddingplatform.serviceImp;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.Expression;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fu.weddingplatform.constant.booking.BookingErrorMessage;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailStatus;
import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.constant.payment.PaymentTypeValue;
import com.fu.weddingplatform.constant.supplier.SupplierErrorMessage;
import com.fu.weddingplatform.constant.transaction.TransactionErrorMessage;

import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.repository.SupplierRepository;
import com.fu.weddingplatform.repository.TransactionRepository;
import com.fu.weddingplatform.response.statistic.DashboardStatistic;
import com.fu.weddingplatform.response.transaction.TransactionResponse;
import com.fu.weddingplatform.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    CoupleRepository coupleRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public List<TransactionResponse> getCoupleTransactionsHistoryByFilter(String coupleId, int pageNo, int pageSize, String sortBy,
                                                                          boolean isAscending, LocalDate timeFrom, LocalDate timeTo, String isDeposit) {
        Couple couple = coupleRepository.findById(coupleId)
                .orElseThrow(() -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));
        List<Booking> listBooking = bookingRepository.findByCouple(couple);
        List<String> listBookingId = listBooking.stream().map(Booking::getId).toList();
        Specification<Transaction> specification = buildComboSpecification(coupleId, timeFrom, timeTo, listBookingId, isDeposit);
        Page<Transaction> transactionPage;
        if (isAscending) {
            transactionPage = transactionRepository.findAll(specification,
                    PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
        } else {
            transactionPage = transactionRepository.findAll(specification,
                    PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
        }
        return transactionPage.stream().map(o -> mapper.map(o, TransactionResponse.class)).collect(Collectors.toList());
    }

    @Override
    public TransactionResponse getTransactionById(String id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ErrorException(TransactionErrorMessage.NOT_FOUND));
        return mapper.map(transaction, TransactionResponse.class);
    }

    @Override
    public List<TransactionResponse> getBookingTransactionsHistoryByFilter(String bookingId, int pageNo, int pageSize, String sortBy, boolean isAscending, LocalDate timeFrom, LocalDate timeTo, String isDeposit) {
        bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));
        List<String> listBookingId = new ArrayList<>();
        listBookingId.add(bookingId);
        Specification<Transaction> specification = buildComboSpecification(null, timeFrom, timeTo, listBookingId, isDeposit);
        Page<Transaction> transactionPage;
        if (isAscending) {
            transactionPage = transactionRepository.findAll(specification,
                    PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending()));
        } else {
            transactionPage = transactionRepository.findAll(specification,
                    PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()));
        }
        return transactionPage.stream().map(o -> mapper.map(o, TransactionResponse.class)).collect(Collectors.toList());
    }

    private Specification<Transaction> buildComboSpecification(String coupleId, LocalDate timeFrom, LocalDate timeTo, List<String> listBookingId, String isDeposit) {
        Specification<Transaction> specification = Specification.where(null);
        if (coupleId != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("invoiceDetail").get("bookingDetail").get("booking").get("couple").get("id"), coupleId));
        }

//        if (timeFrom != null && timeTo != null) {
//            specification = specification.and((root, query, builder) -> builder.between(root.get("dateCreated"), timeFrom.toString(), timeTo.toString()));
//        } else if (timeFrom != null) {
//            specification = specification.and((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("dateCreated"), timeFrom.toString()));
//        } else if (timeTo != null) {
//            specification = specification.and((root, query, builder) -> builder.lessThanOrEqualTo(root.get("dateCreated"), timeTo.toString()));
//        }

        if (!listBookingId.isEmpty()) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> root.get("invoiceDetail").get("bookingDetail").get("booking").get("id").in(listBookingId));
        }

        if (isDeposit != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("invoiceDetail").get("isDeposit"), Boolean.parseBoolean(isDeposit)));
        }

        Specification<Transaction> dateSpecification = dateCompare(timeFrom, timeTo);
        if (dateSpecification != null) {
            specification = specification.and(dateSpecification);
        }
        return specification;
    }

    public static Specification<Transaction> dateCompare(LocalDate timeFrom, LocalDate timeTo) {
        return (root, query, builder) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Expression<Date> dateCreatedAsDate = builder.function(
                    "STR_TO_DATE", Date.class,
                    root.get("dateCreated"),
                    builder.literal("%Y-%m-%d")
            );
            if (timeFrom != null && timeTo != null) {
                String formattedTimeFrom = timeFrom.format(formatter);
                String formattedTimeTo = timeTo.format(formatter);
                return builder.between(dateCreatedAsDate, java.sql.Date.valueOf(formattedTimeFrom), java.sql.Date.valueOf(formattedTimeTo));
            } else if (timeFrom != null) {
                String formattedTimeFrom = timeFrom.format(formatter);
                return builder.greaterThanOrEqualTo(dateCreatedAsDate, java.sql.Date.valueOf(formattedTimeFrom));
            } else if (timeTo != null) {
                String formattedTimeTo = timeTo.format(formatter);
                return builder.lessThanOrEqualTo(dateCreatedAsDate, java.sql.Date.valueOf(formattedTimeTo));
            }
            return null;
        };
    }

    @Override
    public DashboardStatistic getSupplierDashboardStatistic(int month, int quarter, int year, String supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ErrorException(SupplierErrorMessage.NOT_FOUND));
        Specification<Transaction> specification = buildSpecificationToSuppStatistic(month, quarter, year, supplier.getId());
        List<Transaction> transactionList = transactionRepository.findAll(specification);
        int totalAmountCouplePaid = transactionList.stream().mapToInt(Transaction::getAmount).sum();
        int totalAmountSupplierEarn = (int)(totalAmountCouplePaid * PaymentTypeValue.SUPPLIER_RECEIVE_VALUE);
        int totalAmountPlatformEarn = (int)(totalAmountCouplePaid * PaymentTypeValue.PLATFORM_FEE_VALUE);
        return DashboardStatistic.builder()
                .totalAmountSupplierEarn(totalAmountSupplierEarn)
                .totalAmountPlatformFee(totalAmountPlatformEarn)
                .totalAmountCouplePaid(totalAmountCouplePaid)
                .build();
    }

    private Specification<Transaction> buildSpecificationToSuppStatistic(int month, int quarter, int year, String supplierId) {
        Specification<Transaction> specification = Specification.where(null);

        specification = specification.and((root, query, builder) ->
                builder.equal(root.get("invoiceDetail")
                        .get("bookingDetail")
                        .get("serviceSupplier")
                        .get("supplier")
                        .get("id"), supplierId));
        specification = specification.and((root, query, builder) -> builder.equal(root.get("status"), TransactionStatus.COMPLETED));

        specification = specification.and((root, query, builder) ->
                builder.equal(root.get("invoiceDetail")
                        .get("bookingDetail")
                        .get("status"), BookingDetailStatus.COMPLETED));

        specification = specification.and((root, query, builder) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Expression<Date> dateCreatedAsDate = builder.function(
                    "STR_TO_DATE", Date.class,
                    root.get("dateCreated"),
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
