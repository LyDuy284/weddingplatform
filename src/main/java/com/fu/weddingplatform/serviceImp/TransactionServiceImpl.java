package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.booking.BookingErrorMessage;
import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.constant.transaction.TransactionErrorMessage;
import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.Combo;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Transaction;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.repository.TransactionRepository;
import com.fu.weddingplatform.response.transaction.TransactionResponse;
import com.fu.weddingplatform.service.TransactionService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        if(isDeposit != null){
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("invoiceDetail").get("isDeposit"), Boolean.parseBoolean(isDeposit)));
        }

        Specification<Transaction> dateSpecification = dateCompare(timeFrom, timeTo);
        if(dateSpecification != null){
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
