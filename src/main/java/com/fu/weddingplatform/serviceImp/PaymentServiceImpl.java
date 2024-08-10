package com.fu.weddingplatform.serviceImp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.weddingplatform.constant.booking.BookingStatus;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailErrorMessage;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailStatus;
import com.fu.weddingplatform.constant.invoice.InvoiceStatus;
import com.fu.weddingplatform.constant.invoiceDetail.InvoiceDetailErrorMessage;
import com.fu.weddingplatform.constant.invoiceDetail.InvoiceDetailStatus;
import com.fu.weddingplatform.constant.payment.PaymentTypeValue;
import com.fu.weddingplatform.constant.payment.vnpay.VNPayConstant;
import com.fu.weddingplatform.constant.payment.vnpay.VNResponseCode;
import com.fu.weddingplatform.constant.transaction.TransactionStatus;
import com.fu.weddingplatform.constant.transaction.TransactionType;
import com.fu.weddingplatform.constant.walletHistory.WalletHistoryConstant;
import com.fu.weddingplatform.constant.walletHistory.WalletHistoryType;
import com.fu.weddingplatform.entity.*;
import com.fu.weddingplatform.enums.PaymentMethod;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.*;
import com.fu.weddingplatform.request.payment.CreatePaymentDTO;
import com.fu.weddingplatform.request.payment.UpdatePaymentStatusDTO;
import com.fu.weddingplatform.request.wallet.UpdateBalanceWallet;
import com.fu.weddingplatform.response.payment.PaymentVNPInfor;
import com.fu.weddingplatform.service.BookingDetailService;
import com.fu.weddingplatform.service.PaymentService;
import com.fu.weddingplatform.service.WalletService;
import com.fu.weddingplatform.utils.Utils;
import com.fu.weddingplatform.utils.VNPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    @Transactional
    public String requestPaymentVNP(HttpServletRequest req, HttpServletResponse resp, CreatePaymentDTO paymentRequest)
            throws JsonProcessingException {
        Map<String, String> vnp_Params = setVNPParams(req, paymentRequest);

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
        return VNPayConstant.VNP_PAY_URL + queryUrl;
    }

    private Map<String, String> setVNPParams(HttpServletRequest req, CreatePaymentDTO paymentRequest)
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
        PaymentVNPInfor paymentInfor = createInvoiceByEachSupplier(paymentRequest.getListBookingDetailId(), paymentRequest.isDeposit());
        // set order infor
        CreatePaymentDTO paymentDTO = CreatePaymentDTO.builder()
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

        return vnp_Params;
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
                .listVNPBookingDetailId(new ArrayList<>())
                .build();
        for (Supplier supplier : setSupplier) {
            List<BookingDetail> listBookingBySupplier = mapBooking.get(supplier);
            PaymentVNPInfor paymentInfor = createInvoice(firstBookingDetail.getBooking(), listBookingBySupplier, isDeposit);
            paymentVNPInfor.setVnpAmount(paymentVNPInfor.getVnpAmount() + paymentInfor.getVnpAmount());
            paymentVNPInfor.getListVNPBookingDetailId().addAll(paymentInfor.getListVNPBookingDetailId());
        }
        return paymentVNPInfor;
    }

    private PaymentVNPInfor createInvoice(Booking booking, List<BookingDetail> listBookingDetail, boolean isDeposit) {
        List<String> listVNPBookingDetailId = new ArrayList<>();
        Wallet wallet = walletRepository.findByAccountId(booking.getCouple().getAccount().getId()).get();
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
        Map<Payment, List<Transaction>> mapPaymentTransactions = new HashMap<>();
        int vnpAmount = 0;
        int walletAmount = 0;
        int countVNPTransaction = 0;
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
            //check balance wallet with price
            if(wallet.getBalance() < price){
                Transaction transaction = Transaction.builder()
                        .payment(paymentVNPay)
                        .transactionType(TransactionType.PAYMENT)
                        .amount(invoiceDetailSaved.getPrice())
                        .dateCreated(Utils.formatVNDatetimeNow())
                        .status(TransactionStatus.PROCESSING)
                        .invoiceDetail(invoiceDetailSaved)
                        .build();
                List<Transaction> listTransaction = mapPaymentTransactions.get(paymentVNPay);
                if(listTransaction == null ||  listTransaction.isEmpty()){
                    listTransaction = new ArrayList<>();
                }
                listTransaction.add(transaction);
                mapPaymentTransactions.put(paymentVNPay, listTransaction);
                vnpAmount += price;
                ++countVNPTransaction;
                listVNPBookingDetailId.add(bookingDetail.getId());
            }else{
                //update balance wallet
                UpdateBalanceWallet updateBalanceWallet = UpdateBalanceWallet.builder()
                        .accountId(wallet.getAccount().getId())
                        .amount(price)
                        .type(WalletHistoryType.MINUS)
                        .description(String.format(WalletHistoryConstant.DESCRIPTION_MINUS_MONEY_FROM_BOOKING, price, bookingDetail.getId()))
                        .build();
                wallet = walletService.updateBalanceWallet(updateBalanceWallet);
                invoiceDetailSaved.setStatus(InvoiceDetailStatus.COMPLETED);
                invoiceDetailRepository.save(invoiceDetailSaved);
                //update deposited status booking detail
                if(isDeposit){
                    bookingDetailService.depositBookingDetail(bookingDetail.getId());
                }else{
                    bookingDetailService.completeBookingDetail(bookingDetail.getId());
                }
                //create transaction
                Transaction transaction = Transaction.builder()
                        .payment(paymentWallet)
                        .transactionType(TransactionType.PAYMENT)
                        .amount(invoiceDetailSaved.getPrice())
                        .dateCreated(Utils.formatVNDatetimeNow())
                        .status(TransactionStatus.COMPLETED)
                        .invoiceDetail(invoiceDetailSaved)
                        .build();
                List<Transaction> listTransaction = mapPaymentTransactions.get(paymentWallet);
                if(listTransaction == null ||  listTransaction.isEmpty()){
                    listTransaction = new ArrayList<>();
                }
                listTransaction.add(transaction);
                mapPaymentTransactions.put(paymentWallet, listTransaction);
                //set transaction summary

                walletAmount += price;
            }
        }
        if(vnpAmount != 0){
            paymentVNPay.setAmount(vnpAmount);
            Payment paymentSaved = paymentRepository.saveAndFlush(paymentVNPay);
            mapPaymentTransactions.get(paymentVNPay).forEach(o -> o.setPayment(paymentSaved));
            transactionRepository.saveAllAndFlush(mapPaymentTransactions.get(paymentVNPay));
        }
        if(walletAmount != 0){
            paymentWallet.setAmount(walletAmount);
            Payment paymentSaved = paymentRepository.saveAndFlush(paymentWallet);
            mapPaymentTransactions.get(paymentWallet).forEach(o -> o.setPayment(paymentSaved));
            transactionRepository.saveAllAndFlush(mapPaymentTransactions.get(paymentWallet));
            setTransactionSummary(booking, walletAmount);
        }
        if(countVNPTransaction == 0){
            invoiceSaved.setStatus(InvoiceStatus.PAID);
        }
        invoiceSaved.setTotalPrice(vnpAmount + walletAmount);
        invoiceRepository.save(invoiceSaved);
        return PaymentVNPInfor.builder()
                .listVNPBookingDetailId(listVNPBookingDetailId)
                .vnpAmount(vnpAmount)
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
        UpdatePaymentStatusDTO updatePaymentStatusDTO = new UpdatePaymentStatusDTO();
        if (VNResponseCode.PAYMENT_SUCCESSFULLY_VALE.equals(request.getParameter("vnp_ResponseCode"))) {
            updatePaymentStatusDTO.setInvoiceStatus(InvoiceStatus.PAID);
            updatePaymentStatusDTO.setInvoiceDetailStatus(InvoiceDetailStatus.COMPLETED);
            updatePaymentStatusDTO.setTransactionStatus(TransactionStatus.COMPLETED);
        } else {
            updatePaymentStatusDTO.setInvoiceStatus(InvoiceStatus.CANCELLED);
            updatePaymentStatusDTO.setInvoiceDetailStatus(InvoiceDetailStatus.CANCELLED);
            updatePaymentStatusDTO.setTransactionStatus(TransactionStatus.CANCELLED);
        }
        // invoice infor
        String paymentInfor = request.getParameter("vnp_OrderInfo");
        CreatePaymentDTO paymentDTO = objectMapper.readValue(paymentInfor, CreatePaymentDTO.class);
        BookingDetail firstBookingDetail = bookingDetailRepository.findById(paymentDTO.getListBookingDetailId().get(0))
                .orElseThrow(() -> new ErrorException(String.format(BookingDetailErrorMessage.NOT_FOUND_BOOKING,
                        paymentDTO.getListBookingDetailId().get(0))));
        Booking booking = firstBookingDetail.getBooking();
        // update invoice
        updateInvoicePending(booking, updatePaymentStatusDTO);
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
        } else {
            // bookingDetailList.forEach(bd -> bd.setStatus(BookingDetailStatus.COMPLETED));
            bookingDetailList.forEach(bd -> bookingDetailService.completeBookingDetail(bd.getId()));
            totalAmount = (int) (bookingDetailList.stream().mapToInt(BookingDetail::getPrice).sum()
                    * PaymentTypeValue.FINAL_PAYMENT_VALUE);
        }
        // bookingDetailRepository.saveAll(bookingDetailList);
        setTransactionSummary(booking, totalAmount);
        boolean check = checkBookingComplete(booking);
        if (check) {
            booking.setStatus(BookingStatus.COMPLETED);
            bookingRepository.save(booking);
            // transfer amount to wallet supplier
            transferAmountToSupplier(booking);
        }

        response.sendRedirect("http://localhost:3000/booking-history");
    }

    private boolean checkBookingComplete(Booking booking) {
        List<Invoice> allInvoice = invoiceRepository.findByBookingIdAndStatus(booking.getId(), InvoiceStatus.PAID);
        int amount = allInvoice.stream().mapToInt(Invoice::getTotalPrice).sum();
        return amount == booking.getTotalPrice();
    }

    private void updateInvoicePending(Booking booking, UpdatePaymentStatusDTO updatePaymentStatusDTO) {
        List<Invoice> listInvoice = invoiceRepository.findByBookingIdAndStatus(booking.getId(), InvoiceStatus.PENDING);
        for (Invoice invoice : listInvoice) {
            // change invoice status
            invoice.setStatus(updatePaymentStatusDTO.getInvoiceStatus());
            // update invoice detail status
            List<InvoiceDetail> listInvoiceDetail = (List<InvoiceDetail>) invoice.getInvoiceDetails();
            for (InvoiceDetail invoiceDetail : listInvoiceDetail) {
                if(invoiceDetail.getStatus().equals(InvoiceDetailStatus.PENDING)){
                    invoiceDetail.setStatus(updatePaymentStatusDTO.getInvoiceDetailStatus());
                }
            }
            // change transactions status
            Payment payment = paymentRepository.findByInvoiceIdAndPaymentMethod(invoice.getId(), PaymentMethod.VNPAY);
            List<Transaction> listTransaction = transactionRepository.findByPaymentId(payment.getId());
            listTransaction.forEach(t -> t.setStatus(updatePaymentStatusDTO.getTransactionStatus()));
        }
        invoiceRepository.saveAll(listInvoice);
    }

    private void transferAmountToSupplier(Booking booking) {
        List<BookingDetail> allBookingDetail = bookingDetailRepository.findByBookingAndStatus(booking,
                BookingDetailStatus.COMPLETED);
        Set<Supplier> setSupplier = new HashSet<>();
        allBookingDetail.forEach(bd -> setSupplier.add(bd.getServiceSupplier().getSupplier()));
        Map<Supplier, List<BookingDetail>> mapSupplierBookingDetail = mapBookingDetailBySupplier(setSupplier,
                allBookingDetail);
        for (Supplier supplier : setSupplier) {
            List<BookingDetail> listBookingDetailBySupplier = mapSupplierBookingDetail.get(supplier);
            int totalAmount = listBookingDetailBySupplier.stream().mapToInt(BookingDetail::getPrice).sum();
            int supplierAmount = (int) (totalAmount * PaymentTypeValue.FINAL_PAYMENT_VALUE);
            Wallet supplierWallet = supplier.getAccount().getWallet();
            supplierWallet.setBalance(supplierWallet.getBalance() + supplierAmount);
            Wallet walletSaved = walletRepository.saveAndFlush(supplierWallet);
            WalletHistory walletHistory = WalletHistory.builder()
                    .wallet(walletSaved)
                    .type(WalletHistoryType.PlUS)
                    .createDate(Utils.formatVNDatetimeNow())
                    .amount(supplierAmount)
                    .description(String.format(WalletHistoryConstant.DESCRIPTION_PLUS_MONEY_FROM_BOOKING,
                            supplierAmount, booking.getId()))
                    .build();
            walletHistoryRepository.save(walletHistory);
        }
    }

    private void setTransactionSummary(Booking booking, int totalAmount) {
        int supplierAmount = (int) (totalAmount * PaymentTypeValue.FINAL_PAYMENT_VALUE);
        int platformFee = (int) (totalAmount * PaymentTypeValue.DEPOSIT_VALUE);
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
}
