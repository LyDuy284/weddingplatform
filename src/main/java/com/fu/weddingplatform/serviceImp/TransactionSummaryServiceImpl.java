package com.fu.weddingplatform.serviceImp;

import com.fu.weddingplatform.constant.booking.BookingErrorMessage;
import com.fu.weddingplatform.constant.booking.BookingStatus;
import com.fu.weddingplatform.constant.invoice.InvoiceStatus;
import com.fu.weddingplatform.constant.invoiceDetail.InvoiceDetailStatus;
import com.fu.weddingplatform.constant.payment.PaymentTypeValue;
import com.fu.weddingplatform.constant.transaction.TransactionErrorMessage;
import com.fu.weddingplatform.constant.transactionSummary.TransactionSummaryErrorMessage;
import com.fu.weddingplatform.entity.*;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.repository.InvoiceRepository;
import com.fu.weddingplatform.repository.SupplierRepository;
import com.fu.weddingplatform.repository.TransactionSummaryRepository;
import com.fu.weddingplatform.request.transactionSummary.TransactionSummaryResponse;
import com.fu.weddingplatform.response.Account.SupplierResponse;
import com.fu.weddingplatform.response.statistic.DashboardStatistic;
import com.fu.weddingplatform.service.TransactionSummaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionSummaryServiceImpl implements TransactionSummaryService {
    @Autowired
    TransactionSummaryRepository transactionSummaryRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    ModelMapper mapper;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public DashboardStatistic getStaffDashboardStatistic(int year) {
        int totalAmountCouplePaid = 0;
        int totalAmountSupplierEarn = 0;
        int totalAmountPlatformEarn = 0;
        Map<Integer, DashboardStatistic> amountEachMonths = new HashMap<>();
        for(int i = 1; i <= 12; i++){
            List<TransactionSummary> transactionSummaryList = transactionSummaryRepository.findAllByMonthAndYear(year, i);
            if (transactionSummaryList.isEmpty()) {
                continue;
            }
            int totalAmountCouplePaidEachMonth = transactionSummaryList.stream()
                    .mapToInt(TransactionSummary::getTotalAmount)
                    .sum();
            int totalAmountPlatformEarnEachMonth = (int) (totalAmountCouplePaidEachMonth * PaymentTypeValue.PLATFORM_FEE_VALUE);
            int totalAmountSupplierEarnEachMonth = (int) (totalAmountCouplePaidEachMonth * PaymentTypeValue.SUPPLIER_RECEIVE_VALUE);
            DashboardStatistic dashboardStatisticEachMonth  = DashboardStatistic.builder()
                                .totalAmountSupplierEarn(totalAmountSupplierEarnEachMonth)
                                .totalAmountPlatformFee(totalAmountPlatformEarnEachMonth)
                                .totalAmountCouplePaid(totalAmountCouplePaidEachMonth)
                                .build();
            amountEachMonths.put(i, dashboardStatisticEachMonth);
            totalAmountCouplePaid += totalAmountCouplePaidEachMonth;
            totalAmountSupplierEarn += totalAmountSupplierEarnEachMonth;
            totalAmountPlatformEarn += totalAmountPlatformEarnEachMonth;
        }
        return DashboardStatistic.builder()
                .totalAmountSupplierEarn(totalAmountSupplierEarn)
                .totalAmountPlatformFee(totalAmountPlatformEarn)
                .totalAmountCouplePaid(totalAmountCouplePaid)
                .amountEachMonths(amountEachMonths)
                .build();
    }

    @Override
    public TransactionSummaryResponse getransactionSummary(String bookingId) {
        bookingRepository.findByIdAndStatus(bookingId, BookingStatus.COMPLETED)
                .orElseThrow(() -> new ErrorException(BookingErrorMessage.BOOKING_NOT_COMPLETED));
        TransactionSummary transactionSummary = transactionSummaryRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ErrorException(TransactionSummaryErrorMessage.NOT_FOUND));
        return mapTransactionSummaryResponse(transactionSummary);
    }

    private TransactionSummaryResponse mapTransactionSummaryResponse(TransactionSummary transactionSummary){
        TransactionSummaryResponse transactionSummaryResponse = TransactionSummaryResponse.builder()
                .id(transactionSummary.getId())
                .platformFee(transactionSummary.getPlatformFee())
                .totalAmount(transactionSummary.getTotalAmount())
                .supplierTotalEarn(transactionSummary.getSupplierAmount())
                .dateCreated(transactionSummary.getDateCreated())
                .dateModified(transactionSummary.getDateModified())
                .build();
        List<Invoice> allInvoices = invoiceRepository.findByBookingIdAndStatus(transactionSummary.getBooking().getId(), InvoiceStatus.PAID);
        if(allInvoices.isEmpty()){
            return transactionSummaryResponse;
        }
        Map<SupplierResponse, Integer> supplierAmountDetails = new HashMap<>();
        for(BookingDetail bookingDetail : transactionSummary.getBooking().getBookingDetails()){
            int amountPaid = bookingDetail.getInvoiceDetails().stream()
                    .filter(element -> element.getStatus().equals(InvoiceDetailStatus.COMPLETED))
                    .mapToInt(InvoiceDetail::getPrice).sum();
            if(amountPaid > 0){
                int amountEarn = (int)(amountPaid * PaymentTypeValue.SUPPLIER_RECEIVE_VALUE);
                Supplier supplier = bookingDetail.getServiceSupplier().getSupplier();
                SupplierResponse supplierResponse = mapper.map(supplier, SupplierResponse.class);
                supplierAmountDetails.merge(supplierResponse, amountEarn, Integer::sum);
            }
        }
        transactionSummaryResponse.setSupplierAmountDetails(supplierAmountDetails);
        return transactionSummaryResponse;
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
