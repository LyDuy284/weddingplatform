package com.fu.weddingplatform.serviceImp;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fu.weddingplatform.enums.PaymentType;
import com.fu.weddingplatform.request.payment.PaymentRequestVNP;
import com.fu.weddingplatform.response.payment.PaymentResponse;
import com.fu.weddingplatform.response.payment.PaymentVNPResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.weddingplatform.constant.booking.BookingStatus;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailErrorMessage;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailStatus;
import com.fu.weddingplatform.constant.couple.CoupleErrorMessage;
import com.fu.weddingplatform.constant.invoice.InvoiceStatus;
import com.fu.weddingplatform.constant.invoiceDetail.InvoiceDetailErrorMessage;
import com.fu.weddingplatform.constant.invoiceDetail.InvoiceDetailStatus;
import com.fu.weddingplatform.constant.payment.PaymentErrorMessage;
import com.fu.weddingplatform.constant.payment.PaymentTypeValue;
import com.fu.weddingplatform.constant.payment.vnpay.VNPayConstant;
import com.fu.weddingplatform.constant.payment.vnpay.VNResponseCode;
import com.fu.weddingplatform.constant.transaction.TransactionStatus;
import com.fu.weddingplatform.constant.transaction.TransactionType;
import com.fu.weddingplatform.constant.walletHistory.WalletHistoryConstant;
import com.fu.weddingplatform.constant.walletHistory.WalletHistoryType;
import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Invoice;
import com.fu.weddingplatform.entity.InvoiceDetail;
import com.fu.weddingplatform.entity.Payment;
import com.fu.weddingplatform.entity.Supplier;
import com.fu.weddingplatform.entity.Transaction;
import com.fu.weddingplatform.entity.TransactionSummary;
import com.fu.weddingplatform.entity.Wallet;
import com.fu.weddingplatform.entity.WalletHistory;
import com.fu.weddingplatform.enums.PaymentMethod;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingDetailRepository;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.repository.InvoiceDetailRepository;
import com.fu.weddingplatform.repository.InvoiceRepository;
import com.fu.weddingplatform.repository.PaymentRepository;
import com.fu.weddingplatform.repository.TransactionRepository;
import com.fu.weddingplatform.repository.TransactionSummaryRepository;
import com.fu.weddingplatform.repository.WalletHistoryRepository;
import com.fu.weddingplatform.repository.WalletRepository;
import com.fu.weddingplatform.request.email.DepositedEmailForCouple;
import com.fu.weddingplatform.request.email.DepositedEmailForSupplierDTO;
import com.fu.weddingplatform.request.email.MailRefundForCoupleDTO;
import com.fu.weddingplatform.request.email.MailRefundForSupplierDTO;
import com.fu.weddingplatform.request.payment.CreatePaymentDTO;
import com.fu.weddingplatform.request.payment.UpdatePaymentStatusDTO;
import com.fu.weddingplatform.request.wallet.UpdateBalanceWallet;
import com.fu.weddingplatform.response.payment.PaymentVNPInfor;
import com.fu.weddingplatform.service.BookingDetailService;
import com.fu.weddingplatform.service.PaymentService;
import com.fu.weddingplatform.service.SentEmailService;
import com.fu.weddingplatform.service.WalletService;
import com.fu.weddingplatform.utils.Utils;
import com.fu.weddingplatform.utils.VNPayUtil;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    CoupleRepository coupleRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BookingDetailRepository bookingDetailRepository;

    @Autowired
    InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    WalletHistoryRepository walletHistoryRepository;

    @Autowired
    TransactionSummaryRepository transactionSummaryRepository;

    @Autowired
    BookingDetailService bookingDetailService;

    @Autowired
    WalletService walletService;

    @Autowired
    SentEmailService sentEmailService;

    @Override
    @Transactional
    public PaymentResponse requestPaymentVNP(HttpServletRequest req, HttpServletResponse resp,
                                             CreatePaymentDTO paymentRequest)
            throws JsonProcessingException {
        PaymentRequestVNP paymentRequestVNP = createPaymentRequest(req, paymentRequest);
        if (paymentRequestVNP.getAmountVNPay() == 0) {
            return PaymentResponse.builder()
                    .urlPaymentVNPay(null)
                    .amountPaymentVNPay(paymentRequestVNP.getAmountVNPay())
                    .amountPaidWallet(paymentRequestVNP.getAmountWalletPaid())
                    .build();
        }
        Map<String, String> vnp_Params = paymentRequestVNP.getVnpParams();

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayUtil.hmacSHA512(VNPayConstant.VNP_HASH_SECRET, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return PaymentResponse.builder()
                .urlPaymentVNPay(VNPayConstant.VNP_PAY_URL + queryUrl)
                .amountPaymentVNPay(paymentRequestVNP.getAmountVNPay())
                .amountPaidWallet(paymentRequestVNP.getAmountWalletPaid())
                .build();
    }

    @Override
    @Transactional
    public int refundDepositedTransaction(String coupleId, String bookingDetailId) {
        Optional<BookingDetail> optionalBookingDetail = bookingDetailRepository.findById(bookingDetailId);
        if (optionalBookingDetail.isPresent()) {
            if (optionalBookingDetail.get().getStatus().equals(BookingDetailStatus.COMPLETED)) {
                throw new ErrorException(String.format(PaymentErrorMessage.CANT_REFUND, bookingDetailId));
            }
            Optional<InvoiceDetail> optionalInvoiceDetail = invoiceDetailRepository
                    .findDepositedInvoiceDetailByBookingDetailId(bookingDetailId);
            if (optionalInvoiceDetail.isPresent()) {
                Optional<Transaction> optionalTransaction = transactionRepository
                        .findCompletedTransaction(optionalInvoiceDetail.get().getId());
                if (optionalTransaction.isPresent()) {
                    InvoiceDetail invoiceDetail = optionalInvoiceDetail.get();
                    // change transaction status
                    Transaction transaction = optionalTransaction.get();
                    transaction.setStatus(TransactionStatus.REFUNDED);
                    transactionRepository.saveAndFlush(transaction);

                    int refundPrice = transaction.getAmount();
                    Payment payment = transaction.getPayment();
                    payment.setAmount(payment.getAmount() - refundPrice);
                    paymentRepository.saveAndFlush(payment);

                    invoiceDetail.setStatus(InvoiceDetailStatus.REFUNDED);
                    invoiceDetailRepository.saveAndFlush(invoiceDetail);

                    Invoice invoice = payment.getInvoice();
                    invoice.setTotalPrice(invoice.getTotalPrice() - refundPrice);
                    if (invoice.getTotalPrice() == 0) {
                        invoice.setStatus(InvoiceStatus.REFUNDED);
                    }
                    invoiceRepository.saveAndFlush(invoice);

                    // InvoiceDetail newInvoiceDetail = InvoiceDetail.builder()
                    // .isDeposit(true)
                    // .invoice(invoice)
                    // .price(transaction.getAmount() - refundPrice)
                    // .status(InvoiceDetailStatus.COMPLETED)
                    // .bookingDetail(optionalBookingDetail.get())
                    // .createAt(Utils.formatVNDatetimeNow())
                    // .build();
                    // InvoiceDetail invoiceDetailSaved =
                    // invoiceDetailRepository.saveAndFlush(newInvoiceDetail);

                    Transaction transactionRefund = Transaction.builder()
                            .payment(payment)
                            .invoiceDetail(invoiceDetail)
                            .amount(refundPrice)
                            .transactionType(TransactionType.REFUND)
                            .status(TransactionStatus.COMPLETED)
                            .dateCreated(Utils.formatVNDatetimeNow())
                            .build();
                    transactionRepository.saveAndFlush(transactionRefund);

                    // Transaction newTransaction = Transaction.builder()
                    // .payment(payment)
                    // .invoiceDetail(invoiceDetailSaved)
                    // .amount(newInvoiceDetail.getPrice())
                    // .transactionType(TransactionType.PAYMENT)
                    // .status(TransactionStatus.COMPLETED)
                    // .dateCreated(Utils.formatVNDatetimeNow())
                    // .build();
                    // transactionRepository.saveAndFlush(newTransaction);

                    Couple couple = coupleRepository.findById(coupleId)
                            .orElseThrow(() -> new ErrorException(CoupleErrorMessage.COUPLE_NOT_FOUND));
                    UpdateBalanceWallet updateBalanceWallet = UpdateBalanceWallet.builder()
                            .accountId(couple.getAccount().getId())
                            .amount(refundPrice)
                            .type(WalletHistoryType.PlUS)
                            .description(String.format(WalletHistoryConstant.DESCRIPTION_PLUS_MONEY, refundPrice,
                                    bookingDetailId))
                            .build();
                    walletService.updateBalanceWallet(updateBalanceWallet);

                    BookingDetail bookingDetail = optionalBookingDetail.get();

                    int paymentAmount = transaction.getPayment().getAmount();
                    int platformAmount = (int) (paymentAmount * PaymentTypeValue.SUPPLIER_RECEIVE_VALUE);

                    MailRefundForCoupleDTO mail = MailRefundForCoupleDTO.builder()
                            .bookingDetail(bookingDetail)
                            .couple(bookingDetail.getBooking().getCouple())
                            .supplierName(bookingDetail.getServiceSupplier().getSupplier().getSupplierName())
                            .totalAmount(Utils.formatAmountToVND(paymentAmount))
                            .platformAmount(Utils.formatAmountToVND(platformAmount))
                            .supplierAmount(Utils.formatAmountToVND(paymentAmount - platformAmount - refundPrice))
                            .receivedAmount(Utils.formatAmountToVND(refundPrice))
                            .build();

                    sentEmailService.sentRefundEmailForCouple(mail);

                    refundTransactionSummary(invoice.getBooking(), refundPrice, transaction.getAmount());
                    return refundPrice;
                }
            }
        }
        return 0;
    }

    private PaymentRequestVNP createPaymentRequest(HttpServletRequest req, CreatePaymentDTO paymentRequest)
            throws JsonProcessingException {
        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = req.getRemoteAddr();
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConstant.VNP_VERSION);
        vnp_Params.put("vnp_Command", VNPayConstant.VNP_COMMAND_PAY);
        vnp_Params.put("vnp_TmnCode", VNPayConstant.VNP_TMN_CODE);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", VNPayConstant.VNP_ORDER_TYPE);
        PaymentVNPInfor paymentInfor = createInvoiceByEachSupplier(paymentRequest.getListBookingDetailId(),
                paymentRequest.isDeposit());
        // set order infor
        PaymentVNPResponse paymentDTO = PaymentVNPResponse.builder()
                .listInvoiceId(paymentInfor.getListInvoiceId())
                .listBookingDetailId(paymentInfor.getListVNPBookingDetailId())
                .isDeposit(paymentRequest.isDeposit())
                .build();
        vnp_Params.put("vnp_OrderInfo", objectMapper.writeValueAsString(paymentDTO));
        vnp_Params.put("vnp_Amount", String.valueOf(paymentInfor.getVnpAmount() * 100L));
        vnp_Params.put("vnp_ReturnUrl", VNPayConstant.VNP_RETURN_URL_SERVER);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        cld.add(Calendar.HOUR_OF_DAY, 7);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        return PaymentRequestVNP.builder()
                .vnpParams(vnp_Params)
                .amountVNPay(paymentInfor.getVnpAmount())
                .amountWalletPaid(paymentInfor.getAmountPaidWallet())
                .build();
    }

    private PaymentVNPInfor createInvoiceByEachSupplier(List<String> listBookingDetailId, boolean isDeposit) {
        Set<Supplier> setSupplier = getSuppliersInListBookingId(listBookingDetailId);
        List<BookingDetail> bookingDetailList = bookingDetailRepository
                .findListBookingDetailInList(listBookingDetailId);
        Map<Supplier, List<BookingDetail>> mapBooking = mapBookingDetailBySupplier(setSupplier, bookingDetailList);
        BookingDetail firstBookingDetail = bookingDetailRepository.findById(listBookingDetailId.get(0))
                .orElseThrow(() -> new ErrorException(
                        String.format(BookingDetailErrorMessage.NOT_FOUND_BOOKING, listBookingDetailId.get(0))));
        PaymentVNPInfor paymentVNPInfor = PaymentVNPInfor.builder()
                .listInvoiceId(new ArrayList<>())
                .listVNPBookingDetailId(new ArrayList<>())
                .build();
        for (Supplier supplier : setSupplier) {
            List<BookingDetail> listBookingBySupplier = mapBooking.get(supplier);
            PaymentVNPInfor paymentInfor = createInvoice(firstBookingDetail.getBooking(), listBookingBySupplier,
                    isDeposit);
            paymentVNPInfor.setVnpAmount(paymentVNPInfor.getVnpAmount() + paymentInfor.getVnpAmount());
            paymentVNPInfor.getListVNPBookingDetailId().addAll(paymentInfor.getListVNPBookingDetailId());
            paymentVNPInfor.getListInvoiceId().addAll(paymentInfor.getListInvoiceId());
            paymentVNPInfor
                    .setAmountPaidWallet(paymentVNPInfor.getAmountPaidWallet() + paymentInfor.getAmountPaidWallet());
        }
        if(paymentVNPInfor.getAmountPaidWallet() != 0){
            if(!isDeposit){
                transferAmountToSupplier(firstBookingDetail.getBooking(), PaymentMethod.WALLET);
            }
        }
        return paymentVNPInfor;
    }

    private PaymentVNPInfor createInvoice(Booking booking, List<BookingDetail> listBookingDetail, boolean isDeposit) {
        List<String> listVNPBookingDetailId = new ArrayList<>();
        Map<Payment, List<Transaction>> mapPaymentTransactions = new HashMap<>();
        Wallet wallet = walletRepository.findByAccountId(booking.getCouple().getAccount().getId()).get();
        int vnpAmount = 0;
        int walletAmount = 0;
        int countVNPTransaction = 0;
        Invoice invoice = Invoice.builder()
                .booking(booking)
                .createAt(Utils.formatVNDatetimeNow())
                .status(InvoiceStatus.PENDING)
                .build();
        Invoice invoiceSaved = invoiceRepository.saveAndFlush(invoice);
        Payment paymentVNPay = Payment.builder()
                .invoice(invoiceSaved)
                .paymentMethod(PaymentMethod.VNPAY)
                .dateCreated(Utils.formatVNDatetimeNow())
                .build();
        Payment paymentWallet = Payment.builder()
                .invoice(invoiceSaved)
                .paymentMethod(PaymentMethod.WALLET)
                .dateCreated(Utils.formatVNDatetimeNow())
                .build();
        for (BookingDetail bookingDetail : listBookingDetail) {
            int price;
            if (isDeposit) {
                if (!bookingDetail.getStatus().equals(BookingDetailStatus.APPROVED)) {
                    throw new ErrorException(
                            String.format(BookingDetailErrorMessage.CANT_PAYMENT, bookingDetail.getId()));
                }
                price = (int) (bookingDetail.getPrice() * PaymentTypeValue.DEPOSIT_VALUE);

            } else {
                invoiceDetailRepository.findDepositedInvoiceDetailByBookingDetailId(bookingDetail.getId())
                        .orElseThrow(() -> new ErrorException(String
                                .format(InvoiceDetailErrorMessage.NOT_FOUND_DEPOSITED_INVOICE, bookingDetail.getId())));
                if (!bookingDetail.getStatus().equals(BookingDetailStatus.DONE)) {
                    throw new ErrorException(String.format(BookingDetailErrorMessage.NOT_DONE, bookingDetail.getId()));
                }
                price = (int) (bookingDetail.getPrice() * PaymentTypeValue.FINAL_PAYMENT_VALUE);
            }
            InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                    .bookingDetail(bookingDetail)
                    .price(price)
                    .isDeposit(isDeposit)
                    .invoice(invoice)
                    .createAt(Utils.formatVNDatetimeNow())
                    .status(InvoiceDetailStatus.PENDING)
                    .build();
            InvoiceDetail invoiceDetailSaved = invoiceDetailRepository.saveAndFlush(invoiceDetail);
            // check balance wallet with price
            if (wallet.getBalance() < price) {
                Transaction transaction = Transaction.builder()
                        .payment(paymentVNPay)
                        .transactionType(TransactionType.PAYMENT)
                        .amount(invoiceDetailSaved.getPrice())
                        .dateCreated(Utils.formatVNDatetimeNow())
                        .status(TransactionStatus.PROCESSING)
                        .invoiceDetail(invoiceDetailSaved)
                        .build();
                List<Transaction> listTransaction = mapPaymentTransactions.get(paymentVNPay);
                if (listTransaction == null || listTransaction.isEmpty()) {
                    listTransaction = new ArrayList<>();
                }
                listTransaction.add(transaction);
                mapPaymentTransactions.put(paymentVNPay, listTransaction);

                ++countVNPTransaction;
                vnpAmount += price;
                listVNPBookingDetailId.add(bookingDetail.getId());
            } else {
                // update balance wallet
                UpdateBalanceWallet updateBalanceWallet = UpdateBalanceWallet.builder()
                        .accountId(wallet.getAccount().getId())
                        .amount(price)
                        .type(WalletHistoryType.MINUS)
                        .description(String.format(WalletHistoryConstant.DESCRIPTION_MINUS_MONEY, price,
                                bookingDetail.getId()))
                        .build();
                wallet = walletService.updateBalanceWallet(updateBalanceWallet);
                invoiceDetailSaved.setStatus(InvoiceDetailStatus.COMPLETED);
                invoiceDetailRepository.save(invoiceDetailSaved);
                // update deposited status booking detail
                if (isDeposit) {
                    bookingDetailService.depositBookingDetail(bookingDetail.getId());
                } else {
                    bookingDetailService.completeBookingDetail(bookingDetail.getId());
                }
                // create transaction
                Transaction transaction = Transaction.builder()
                        .payment(paymentWallet)
                        .transactionType(TransactionType.PAYMENT)
                        .amount(invoiceDetailSaved.getPrice())
                        .dateCreated(Utils.formatVNDatetimeNow())
                        .status(TransactionStatus.COMPLETED)
                        .invoiceDetail(invoiceDetailSaved)
                        .build();
                List<Transaction> listTransaction = mapPaymentTransactions.get(paymentWallet);
                if (listTransaction == null || listTransaction.isEmpty()) {
                    listTransaction = new ArrayList<>();
                }
                listTransaction.add(transaction);
                mapPaymentTransactions.put(paymentWallet, listTransaction);
                walletAmount += price;
            }
        }
        if (vnpAmount != 0) {
            paymentVNPay.setAmount(vnpAmount);
            Payment paymentSaved = paymentRepository.saveAndFlush(paymentVNPay);
            for (Transaction transaction : mapPaymentTransactions.get(paymentVNPay)) {
                transaction.setPayment(paymentSaved);
                transactionRepository.saveAndFlush(transaction);
            }
        }
        if (walletAmount != 0) {
            paymentWallet.setAmount(walletAmount);
            Payment paymentSaved = paymentRepository.saveAndFlush(paymentWallet);
            for (Transaction transaction : mapPaymentTransactions.get(paymentWallet)) {
                transaction.setPayment(paymentSaved);
                transactionRepository.saveAndFlush(transaction);
            }
            // set transaction summary
            setTransactionSummary(booking, walletAmount);
        }
        if (countVNPTransaction == 0) {
            invoiceSaved.setStatus(InvoiceStatus.PAID);
        }
        invoiceSaved.setTotalPrice(vnpAmount + walletAmount);
        invoiceRepository.save(invoiceSaved);
        List<String> listInvoiceId = new ArrayList<>();
        listInvoiceId.add(invoice.getId());
        return PaymentVNPInfor.builder()
                .listInvoiceId(listInvoiceId)
                .listVNPBookingDetailId(listVNPBookingDetailId)
                .vnpAmount(vnpAmount)
                .amountPaidWallet(walletAmount)
                .build();
    }

    // private Map<Supplier, List<BookingDetail>>
    // mapBookingDetailBySupplier(Set<Supplier> setSupplier, List<String>
    // listBookingDetailId) {
    // Map<Supplier, List<BookingDetail>> mapGroup = new HashMap<>();
    // for (Supplier supplier : setSupplier) {
    // List<BookingDetail> listBookingDetailBySupplier =
    // bookingDetailRepository.findListBookingDetailBySupplierId(supplier.getId(),
    // listBookingDetailId);
    // mapGroup.put(supplier, listBookingDetailBySupplier);
    // }
    // return mapGroup;
    // }

    private Map<Supplier, List<BookingDetail>> mapBookingDetailBySupplier(Set<Supplier> setSupplier,
                                                                          List<BookingDetail> listBookingDetail) {
        Map<Supplier, List<BookingDetail>> mapGroup = new HashMap<>();
        for (Supplier supplier : setSupplier) {
            List<BookingDetail> listBookingDetailBySupplier = bookingDetailRepository
                    .findListBookingDetailBySupplierId(supplier.getId(), listBookingDetail);
            mapGroup.put(supplier, listBookingDetailBySupplier);
        }
        return mapGroup;
    }

    private Set<Supplier> getSuppliersInListBookingId(List<String> listBookingDetailId) {
        Set<Supplier> setSupplier = new HashSet<>();
        for (String bookingDetailId : listBookingDetailId) {
            BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId)
                    .orElseThrow(() -> new ErrorException(
                            String.format(BookingDetailErrorMessage.NOT_FOUND_BY_ID, bookingDetailId)));
            setSupplier.add(bookingDetail.getServiceSupplier().getSupplier());
        }
        return setSupplier;
    }

    @Override
    @Transactional
    public void responsePaymentVNP(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // String vnpTransactionNo = request.getParameter("vnp_TransactionNo");
        // int amount = (int) (Long.parseLong(request.getParameter("vnp_Amount")) /
        // 100);

        // invoice infor
        String paymentInfor = request.getParameter("vnp_OrderInfo");
        PaymentVNPResponse paymentDTO = objectMapper.readValue(paymentInfor, PaymentVNPResponse.class);
        BookingDetail firstBookingDetail = bookingDetailRepository.findById(paymentDTO.getListBookingDetailId().get(0))
                .orElseThrow(() -> new ErrorException(String.format(BookingDetailErrorMessage.NOT_FOUND_BOOKING,
                        paymentDTO.getListBookingDetailId().get(0))));
        Booking booking = firstBookingDetail.getBooking();
        if (!VNResponseCode.PAYMENT_SUCCESSFULLY_VALE.equals(request.getParameter("vnp_ResponseCode"))) {
            UpdatePaymentStatusDTO updatePaymentStatusDTO = UpdatePaymentStatusDTO.builder()
                    .invoiceStatus(InvoiceStatus.CANCELLED)
                    .invoiceDetailStatus(InvoiceDetailStatus.CANCELLED)
                    .transactionStatus(TransactionStatus.CANCELLED)
                    .build();
            // update invoice
            updateInvoicePending(paymentDTO.getListInvoiceId(), updatePaymentStatusDTO);
        } else {
            UpdatePaymentStatusDTO updatePaymentStatusDTO = UpdatePaymentStatusDTO.builder()
                    .invoiceStatus(InvoiceStatus.PAID)
                    .invoiceDetailStatus(InvoiceDetailStatus.COMPLETED)
                    .transactionStatus(TransactionStatus.COMPLETED)
                    .build();
            // update invoice
            updateInvoicePending(paymentDTO.getListInvoiceId(), updatePaymentStatusDTO);
            List<BookingDetail> bookingDetailList = bookingDetailRepository
                    .findListBookingDetailInList(paymentDTO.getListBookingDetailId());
            int totalAmount = 0;
            // update booking
            if (paymentDTO.isDeposit()) {
                // bookingDetailList.forEach(bd ->
                // bd.setStatus(BookingDetailStatus.PROCESSING));
                bookingDetailList.forEach(bd -> bookingDetailService.depositBookingDetail(bd.getId()));
                totalAmount = (int) (bookingDetailList.stream().mapToInt(BookingDetail::getPrice).sum()
                        * PaymentTypeValue.DEPOSIT_VALUE);

                for (BookingDetail bookingDetail : bookingDetailList) {
                    DepositedEmailForSupplierDTO depositedEmailForSupplierDTO = DepositedEmailForSupplierDTO.builder()
                            .bookingDetail(bookingDetail)
                            .couple(bookingDetail.getBooking().getCouple())
                            .paymentAmount(Utils.formatAmountToVND(0))
                            .remainingAmount(Utils.formatAmountToVND(0))
                            .build();

                    Optional<InvoiceDetail> invoiceDetail = invoiceDetailRepository
                            .findDepositedInvoiceDetailByBookingDetailId(bookingDetail.getId());

                    if (invoiceDetail.isPresent()) {
                        depositedEmailForSupplierDTO
                                .setPaymentAmount(Utils.formatAmountToVND(invoiceDetail.get().getPrice()));
                        depositedEmailForSupplierDTO
                                .setRemainingAmount(Utils
                                        .formatAmountToVND(bookingDetail.getPrice() - invoiceDetail.get().getPrice()));
                    }
                    sentEmailService.sentDepositedEmailForSupplier(depositedEmailForSupplierDTO);
                }

                DepositedEmailForCouple depositedEmailForCouple = DepositedEmailForCouple.builder()
                        .booking(booking)
                        .couple(booking.getCouple())
                        .listBookingDetails(bookingDetailList)
                        .paymentAmount(Utils.formatAmountToVND(totalAmount))
                        .remainingAmount(Utils.formatAmountToVND(booking.getTotalPrice() - totalAmount))
                        .totalAmount(Utils.formatAmountToVND(booking.getTotalPrice()))
                        .build();

                sentEmailService.sentDepositedEmailForCouple(depositedEmailForCouple);

            } else {
                bookingDetailList.forEach(bd -> bookingDetailService.completeBookingDetail(bd.getId()));
                totalAmount = (int) (bookingDetailList.stream().mapToInt(BookingDetail::getPrice).sum()
                        * PaymentTypeValue.FINAL_PAYMENT_VALUE);
                transferAmountToSupplier(booking, PaymentMethod.VNPAY);
            }
            // bookingDetailRepository.saveAll(bookingDetailList);
            setTransactionSummary(booking, totalAmount);
            boolean check = checkBookingComplete(booking);
            if (check) {
                booking.setStatus(BookingStatus.COMPLETED);
                bookingRepository.save(booking);
                // transfer amount to wallet supplier
                // transferAmountToSupplier(booking);
            }
        }
        response.sendRedirect(VNPayConstant.VNP_REDIRECT_CLIENT);
    }

    private boolean checkBookingComplete(Booking booking) {
        List<Invoice> allInvoice = invoiceRepository.findByBookingIdAndStatus(booking.getId(), InvoiceStatus.PAID);
        int amount = allInvoice.stream().mapToInt(Invoice::getTotalPrice).sum();
        return amount == booking.getTotalPrice();
    }

    private void updateInvoicePending(List<String> listInvoiceId, UpdatePaymentStatusDTO updatePaymentStatusDTO) {
//        List<Invoice> listInvoice = invoiceRepository.findByBookingIdAndStatus(booking.getId(), InvoiceStatus.PENDING);
        for (String invoiceId : listInvoiceId) {
            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new ErrorException("Invoice not found"));
            int amountPaid = 0;
            // update invoice detail status
            List<InvoiceDetail> listInvoiceDetail = (List<InvoiceDetail>) invoice.getInvoiceDetails();
            for (InvoiceDetail invoiceDetail : listInvoiceDetail) {
                if (invoiceDetail.getStatus().equals(InvoiceDetailStatus.PENDING)) {
                    invoiceDetail.setStatus(updatePaymentStatusDTO.getInvoiceDetailStatus());
                } else if (invoiceDetail.getStatus().equals(InvoiceDetailStatus.COMPLETED)) {
                    amountPaid += invoiceDetail.getPrice();
                }
            }
            // change transactions status
            Payment payment = paymentRepository.findByInvoiceIdAndPaymentMethod(invoice.getId(), PaymentMethod.VNPAY);
            if(payment != null){
                List<Transaction> listTransaction = transactionRepository.findByPaymentId(payment.getId());
                listTransaction.forEach(t -> t.setStatus(updatePaymentStatusDTO.getTransactionStatus()));
            }
            if (updatePaymentStatusDTO.getInvoiceStatus().equals(InvoiceStatus.CANCELLED)) {
                if (amountPaid == 0) {
                    invoice.setStatus(InvoiceStatus.CANCELLED);
                } else {
                    invoice.setStatus(InvoiceStatus.PAID);
                    invoice.setTotalPrice(amountPaid);
                }
            } else {
                invoice.setStatus(updatePaymentStatusDTO.getInvoiceStatus());
            }
            invoiceRepository.save(invoice);
        }
//        invoiceRepository.saveAll(listInvoice);
    }

    private void transferAmountToSupplier(Booking booking, PaymentMethod paymentMethod) {
        // List<BookingDetail> allBookingDetail =
        // bookingDetailRepository.findByBookingAndStatus(booking,
        // BookingDetailStatus.COMPLETED);
//        List<BookingDetail> bookingDetailsCompleted = bookingDetailRepository.findByBooking(booking);
        Set<Supplier> setSupplier = new HashSet<>();
        booking.getBookingDetails().forEach(bd -> setSupplier.add(bd.getServiceSupplier().getSupplier()));
        Map<Supplier, List<BookingDetail>> mapSupplierBookingDetail = mapBookingDetailBySupplier(setSupplier,
                booking.getBookingDetails().stream().toList());
        for (Supplier supplier : setSupplier) {
            int totalAmount = 0;
            List<BookingDetail> listBookingDetailBySupplier = mapSupplierBookingDetail.get(supplier);
            for (BookingDetail bookingDetail : listBookingDetailBySupplier){
                List<InvoiceDetail> listInvoiceDetailCompleted = invoiceDetailRepository.findCompletedInvoiceDetail(bookingDetail.getId());
                for(InvoiceDetail invoiceDetail : listInvoiceDetailCompleted){
                    Optional<Transaction> optionalTransaction = transactionRepository.findCompletedTransaction(invoiceDetail.getId());
                    if(optionalTransaction.isPresent()){
                        Transaction transaction = optionalTransaction.get();
                        if(transaction.getPayment().getPaymentMethod().equals(paymentMethod)){
                            totalAmount += transaction.getAmount();
                        }
                    }
                }
            }
            if(totalAmount != 0){
                int supplierAmount = (int) (totalAmount * PaymentTypeValue.SUPPLIER_RECEIVE_VALUE);
                Wallet supplierWallet = supplier.getAccount().getWallet();
                supplierWallet.setBalance(supplierWallet.getBalance() + supplierAmount);
                Wallet walletSaved = walletRepository.saveAndFlush(supplierWallet);

                for (BookingDetail bookingDetail : listBookingDetailBySupplier) {
                    MailRefundForSupplierDTO mailRefundForSupplierDTO = MailRefundForSupplierDTO.builder()
                            .bookingDetail(bookingDetail)
                            .couple(bookingDetail.getBooking().getCouple())
                            .totalAmount(Utils.formatAmountToVND(bookingDetail.getPrice()))
                            .platformAmount(Utils.formatAmountToVND(
                                    (int) (bookingDetail.getPrice() * PaymentTypeValue.PLATFORM_FEE_VALUE)))
                            .receivedAmount(Utils.formatAmountToVND(
                                    (int) (bookingDetail.getPrice() * PaymentTypeValue.SUPPLIER_RECEIVE_VALUE)))
                            .build();

                    sentEmailService.sentRefundEmailForSupplier(mailRefundForSupplierDTO);
                }

                WalletHistory walletHistory = WalletHistory.builder()
                        .wallet(walletSaved)
                        .type(WalletHistoryType.PlUS)
                        .createDate(Utils.formatVNDatetimeNow())
                        .amount(supplierAmount)
                        .description(String.format(WalletHistoryConstant.DESCRIPTION_PLUS_MONEY,
                                supplierAmount, booking.getId()))
                        .build();
                walletHistoryRepository.save(walletHistory);
            }
        }
    }

    private void setTransactionSummary(Booking booking, int totalAmount) {
        int supplierAmount = (int) (totalAmount * PaymentTypeValue.SUPPLIER_RECEIVE_VALUE);
        int platformFee = (int) (totalAmount * PaymentTypeValue.PLATFORM_FEE_VALUE);
        Optional<TransactionSummary> optionalTransactionSummary = transactionSummaryRepository
                .findByBookingId(booking.getId());
        TransactionSummary transactionSummary;
        if (optionalTransactionSummary.isPresent()) {
            transactionSummary = optionalTransactionSummary.get();
            totalAmount += transactionSummary.getTotalAmount();
            supplierAmount += transactionSummary.getSupplierAmount();
            platformFee += transactionSummary.getPlatformFee();
        } else {
            transactionSummary = new TransactionSummary();
            transactionSummary.setDateCreated(Utils.formatVNDatetimeNow());
        }
        transactionSummary.setBooking(booking);
        transactionSummary.setSupplierAmount(supplierAmount);
        transactionSummary.setTotalAmount(totalAmount);
        transactionSummary.setPlatformFee(platformFee);
        transactionSummary.setDateModified(Utils.formatVNDatetimeNow());
        transactionSummaryRepository.save(transactionSummary);
    }

    private void refundTransactionSummary(Booking booking, int refundPrice, int servicePrice) {
        int platformFee = (int) ((servicePrice - refundPrice) * PaymentTypeValue.PLATFORM_FEE_VALUE);
        int supplierAmount = (int) ((servicePrice - refundPrice) * PaymentTypeValue.SUPPLIER_RECEIVE_VALUE);
        TransactionSummary transactionSummary = transactionSummaryRepository.findByBookingId(booking.getId())
                .orElseThrow(() -> new ErrorException("TransactionSummary notfound"));
        transactionSummary.setPlatformFee(transactionSummary.getPlatformFee()
                - (int) (servicePrice * PaymentTypeValue.PLATFORM_FEE_VALUE) + platformFee);
        transactionSummary.setSupplierAmount(transactionSummary.getSupplierAmount()
                - (int) (servicePrice * PaymentTypeValue.SUPPLIER_RECEIVE_VALUE) + supplierAmount);
        transactionSummary.setTotalAmount(transactionSummary.getTotalAmount() - refundPrice);
        transactionSummary.setDateModified(Utils.formatVNDatetimeNow());
        transactionSummaryRepository.save(transactionSummary);
    }

    @Override
    @Scheduled(cron = "0 */5 * * * *", zone = "Asia/Ho_Chi_Minh")
    public void expiredPaymentTransaction() {
        String dateTimeNowStr = Utils.formatVNDatetimeNow();
        LocalDateTime dateTimeNow = Utils.convertStringToLocalDateTime(dateTimeNowStr);
        LocalDateTime timeChecked = dateTimeNow.minusMinutes(15);
        List<Invoice> listInvoiceExpired = invoiceRepository
                .findByCreateAtLessThanEqualAndStatus(Utils.formatLocalDateTimeToString(timeChecked), InvoiceStatus.PENDING);
        if (!listInvoiceExpired.isEmpty()) {
            for (Invoice invoice : listInvoiceExpired) {
                int amountPaid = 0;
                if (invoice.getStatus().equals(InvoiceStatus.PENDING)) {
                    List<InvoiceDetail> invoiceDetails = invoiceDetailRepository.findAllByInvoiceId(invoice.getId());
                    for (InvoiceDetail invoiceDetail : invoiceDetails) {
                        if (invoiceDetail.getStatus().equals(InvoiceDetailStatus.PENDING)) {
                            invoiceDetail.setStatus(InvoiceDetailStatus.OVERDUE);
                        } else if (invoiceDetail.getStatus().equals(InvoiceDetailStatus.COMPLETED)) {
                            amountPaid += invoiceDetail.getPrice();
                        }
                    }
                    invoiceDetailRepository.saveAll(invoiceDetails);

                    Payment payment = paymentRepository.findByInvoiceIdAndPaymentMethod(invoice.getId(), PaymentMethod.VNPAY);
                    List<Transaction> transactionsPending = transactionRepository.findByPaymentId(payment.getId());
                    for (Transaction transaction : transactionsPending) {
                        if (transaction.getStatus().equals(TransactionStatus.PROCESSING)) {
                            transaction.setStatus(TransactionStatus.OVERDUE);
                        }
                    }
                    transactionRepository.saveAll(transactionsPending);
                    if (amountPaid == 0) {
                        invoice.setStatus(InvoiceStatus.OVERDUE);
                    } else {
                        invoice.setStatus(InvoiceStatus.PAID);
                        invoice.setTotalPrice(amountPaid);
                    }
                }
                invoiceRepository.save(invoice);
            }
        }
    }
}
