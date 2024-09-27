package com.fu.weddingplatform.serviceImp;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import com.fu.weddingplatform.constant.bookingDetail.BookingDetailStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.weddingplatform.constant.booking.BookingErrorMessage;
import com.fu.weddingplatform.constant.booking.BookingStatus;
import com.fu.weddingplatform.constant.invoice.InvoiceStatus;
import com.fu.weddingplatform.constant.invoiceDetail.InvoiceDetailStatus;
import com.fu.weddingplatform.constant.payment.PaymentTypeValue;
import com.fu.weddingplatform.constant.transactionSummary.TransactionSummaryErrorMessage;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.Invoice;
import com.fu.weddingplatform.entity.InvoiceDetail;
import com.fu.weddingplatform.entity.Supplier;
import com.fu.weddingplatform.entity.TransactionSummary;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.repository.InvoiceRepository;
import com.fu.weddingplatform.repository.SupplierRepository;
import com.fu.weddingplatform.repository.TransactionSummaryRepository;
import com.fu.weddingplatform.request.transactionSummary.SupplierAmountDetails;
import com.fu.weddingplatform.request.transactionSummary.TransactionSummaryResponse;
import com.fu.weddingplatform.response.Account.SupplierResponse;
import com.fu.weddingplatform.response.statistic.DashboardStatistic;
import com.fu.weddingplatform.service.TransactionSummaryService;

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
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DashboardStatistic getStaffDashboardStatistic(int year) {
        int totalAmountCouplePaid = 0;
        int totalAmountSupplierEarn = 0;
        int totalAmountPlatformEarn = 0;
        Map<Integer, DashboardStatistic> amountEachMonths = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            List<TransactionSummary> transactionSummaryList = transactionSummaryRepository.findAllByMonthAndYear(year,
                    i);
            if (transactionSummaryList.isEmpty()) {
                continue;
            }
            int totalAmountCouplePaidEachMonth = transactionSummaryList.stream()
                    .mapToInt(TransactionSummary::getTotalAmount)
                    .sum();
            int totalAmountPlatformEarnEachMonth = (int) (totalAmountCouplePaidEachMonth
                    * PaymentTypeValue.PLATFORM_FEE_VALUE);
            int totalAmountSupplierEarnEachMonth = (int) (totalAmountCouplePaidEachMonth
                    * PaymentTypeValue.SUPPLIER_RECEIVE_VALUE);
            DashboardStatistic dashboardStatisticEachMonth = DashboardStatistic.builder()
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
    public TransactionSummaryResponse getransactionSummary(String bookingId) throws JsonProcessingException {
        bookingRepository.findByIdAndStatus(bookingId, BookingStatus.COMPLETED)
                .orElseThrow(() -> new ErrorException(BookingErrorMessage.BOOKING_NOT_COMPLETED));
        TransactionSummary transactionSummary = transactionSummaryRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ErrorException(TransactionSummaryErrorMessage.NOT_FOUND));
        return mapTransactionSummaryResponse(transactionSummary);
    }

    private TransactionSummaryResponse mapTransactionSummaryResponse(TransactionSummary transactionSummary)
            throws JsonProcessingException {
        TransactionSummaryResponse transactionSummaryResponse = TransactionSummaryResponse.builder()
                .id(transactionSummary.getId())
                .platformFee(transactionSummary.getPlatformFee())
                .totalAmount(transactionSummary.getTotalAmount())
                .supplierTotalEarn(transactionSummary.getSupplierAmount())
                .dateCreated(transactionSummary.getDateCreated())
                .dateModified(transactionSummary.getDateModified())
                .bookingId(transactionSummary.getBooking().getId())
                .build();
        List<Invoice> allInvoices = invoiceRepository.findByBookingIdAndStatus(transactionSummary.getBooking().getId(),
                InvoiceStatus.PAID);
        if (allInvoices.isEmpty()) {
            return transactionSummaryResponse;
        }
        
        Map<String, SupplierAmountDetails> mapSupplierAmountDetails = new HashMap<>();
        for (BookingDetail bookingDetail : transactionSummary.getBooking().getBookingDetails()) {
//            if(bookingDetail.getStatus().equals((BookingDetailStatus.COMPLETED))){
                Map<String, Integer> hasMap = new HashMap<>();
                int amountPaid = bookingDetail.getInvoiceDetails().stream()
                        .filter(element -> element.getStatus().equals(InvoiceDetailStatus.COMPLETED))
                        .mapToInt(InvoiceDetail::getPrice).sum();
                if (amountPaid > 0) {
                    int amountEarn = (int) (amountPaid * PaymentTypeValue.SUPPLIER_RECEIVE_VALUE);
                    Supplier supplier = bookingDetail.getServiceSupplier().getSupplier();
                    SupplierResponse supplierResponse = mapper.map(supplier, SupplierResponse.class);
                    String supplierJsonStr = objectMapper.writeValueAsString(supplierResponse);
                    hasMap.merge(supplierJsonStr, amountEarn, Integer::sum);
                    String json = hasMap.keySet().iterator().next();
                    Integer price = hasMap.values().iterator().next();

                    JsonNode jsonNode = objectMapper.readTree(json);
                    SupplierAmountDetails details = objectMapper.treeToValue(jsonNode, SupplierAmountDetails.class);
                    details.setPrice(price);

                    SupplierAmountDetails suppDetailsExisted = mapSupplierAmountDetails.get(details.getSupplierId());
                    if(suppDetailsExisted != null){
                        details.setPrice(details.getPrice() + suppDetailsExisted.getPrice());
                    }
                    mapSupplierAmountDetails.put(details.getSupplierId(), details);
                }

        }
        transactionSummaryResponse.setSupplierAmountDetails(new ArrayList<>(mapSupplierAmountDetails.values()));
        return transactionSummaryResponse;
    }

    private Specification<TransactionSummary> buildSpecificationToStaffStatistic(int month, int quarter, int year) {
        Specification<TransactionSummary> specification = Specification.where(null);
        specification = specification.and((root, query, builder) -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Expression<Date> dateCreatedAsDate = builder.function(
                    "STR_TO_DATE", Date.class,
                    root.get("dateModified"),
                    builder.literal("%Y-%m-%d"));
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
