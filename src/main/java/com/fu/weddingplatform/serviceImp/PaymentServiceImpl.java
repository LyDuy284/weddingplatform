package com.fu.weddingplatform.serviceImp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.weddingplatform.constant.booking.BookingConstant;
import com.fu.weddingplatform.constant.booking.BookingErrorMessage;
import com.fu.weddingplatform.constant.booking.BookingStatus;
import com.fu.weddingplatform.constant.bookingDetail.BookingDetailStatus;
import com.fu.weddingplatform.constant.payment.PaymentErrorMessage;
import com.fu.weddingplatform.constant.payment.PaymentStatus;
import com.fu.weddingplatform.constant.payment.PaymentSuccessMessage;
import com.fu.weddingplatform.constant.payment.PaymentTypeValue;
import com.fu.weddingplatform.constant.payment.vnpay.VNPayConstant;
import com.fu.weddingplatform.constant.serviceSupplier.SupplierErrorMessage;
import com.fu.weddingplatform.constant.transaction.TransactionSuccessMessage;
import com.fu.weddingplatform.constant.wallet.WalletErrorMessage;
import com.fu.weddingplatform.entity.*;
import com.fu.weddingplatform.enums.PaymentMethod;
import com.fu.weddingplatform.enums.PaymentType;
import com.fu.weddingplatform.enums.TransactionType;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.*;
import com.fu.weddingplatform.request.payment.CreatePaymentDTO;
import com.fu.weddingplatform.service.PaymentService;
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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    CoupleRepository coupleRepository;

    @Autowired
    QuotationRepository quotationRepository;

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
    PaymentBookingServiceRepository paymentBookingServiceRepository;

    @Override
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
        // set order infor
        CreatePaymentDTO paymentDTO = CreatePaymentDTO.builder()
                .listBookingDetailId(paymentRequest.getListBookingDetailId())
                .paymentType(paymentRequest.getPaymentType())
                .build();
        vnp_Params.put("vnp_OrderInfo", objectMapper.writeValueAsString(paymentDTO));

        int amount = 0;
        switch (paymentRequest.getPaymentType()) {
            case DEPOSIT -> {
                for (String bookingDetailId: paymentRequest.getListBookingDetailId()) {
                    BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByIdAndStatus(bookingDetailId, BookingDetailStatus.CONFIRM)
                            .orElseThrow(() -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));
                    amount += bookingDetail.getPrice();
                }
                vnp_Params.put("vnp_Amount",
                        String.valueOf((int) (amount * PaymentTypeValue.DEPOSIT_VALUE) * 100L));
            }
            case FINAL_PAYMENT -> {
                for (String bookingDetailId: paymentRequest.getListBookingDetailId()) {
                    BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByIdAndStatus(bookingDetailId, BookingDetailStatus.DONE)
                            .orElseThrow(() -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));
                    amount += bookingDetail.getPrice();
                }
                vnp_Params.put("vnp_Amount",
                        String.valueOf((int) (amount * PaymentTypeValue.FINAL_PAYMENT_VALUE) * 100L));
            }
            default -> throw new ErrorException("Sth error");
        }

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

    @Override
    @Transactional
    public void responsePaymentVNP(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"00".equals(request.getParameter("vnp_ResponseCode"))) {
            response.sendRedirect(
                    String.format(VNPayConstant.VNP_RETURN_CLIENT_URL, false, request.getParameter("vnp_Amount")));
        } else {
            String paymentInfor = request.getParameter("vnp_OrderInfo");
            String vnpTransactionNo = request.getParameter("vnp_TransactionNo");
            int amount = (int)(Long.parseLong(request.getParameter("vnp_Amount")) / 100);
            CreatePaymentDTO paymentDTO = objectMapper.readValue(paymentInfor, CreatePaymentDTO.class);

            BookingDetail firstBookingDetail = bookingDetailRepository.findById(paymentDTO.getListBookingDetailId().get(0))
                    .orElseThrow(() -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));
            Payment payment = Payment.builder()
                    .tradingCode(Integer.parseInt(vnpTransactionNo))
                    .description(String.format(PaymentSuccessMessage.PAYMENT_DESCRIPTION, firstBookingDetail.getBooking().getCouple().getId(),
                            amount, paymentDTO.getListBookingDetailId().toString()))
                    .paymentMethod(PaymentMethod.VNPAY)
                    .amount(amount)
                    .paymentType(paymentDTO.getPaymentType())
                    .dateCreated(Utils.formatVNDatetimeNow())
                    .paymentStatus(PaymentStatus.COMPLETED)
                    .couple(firstBookingDetail.getBooking().getCouple())
                    .build();
            Payment paymentSave = paymentRepository.saveAndFlush(payment);

            for (String bookingDetailId: paymentDTO.getListBookingDetailId()) {
                BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId)
                        .orElseThrow(() -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));
                switch (paymentDTO.getPaymentType()) {
                    case DEPOSIT -> bookingDetail.setStatus(BookingDetailStatus.DEPOSITED);
                    case FINAL_PAYMENT -> bookingDetail.setStatus(BookingDetailStatus.COMPLETED);
                }
                PaymentBookingService paymentBookingService = PaymentBookingService.builder()
                        .createAt(Utils.formatVNDatetimeNow())
                        .bookingDetail(bookingDetail)
                        .payment(paymentSave)
                        .status("ACTIVE")
                        .build();
                paymentBookingServiceRepository.saveAndFlush(paymentBookingService);
            }

            if (paymentDTO.getPaymentType().equals(PaymentType.FINAL_PAYMENT)) {
                completeMoneyForServiceSupplier(amount, firstBookingDetail.getService().getServiceSupplier().getId(), paymentDTO.getListBookingDetailId());
            }
            response.sendRedirect("localhost:3000");
        }
    }

    private void completeMoneyForServiceSupplier(int amount, String serviceSupplierId, List<String> bookingDetailId) {
        Wallet wallet = walletRepository.findByServiceSupplierId(serviceSupplierId)
                .orElseThrow(() -> new ErrorException(WalletErrorMessage.NOT_FOUND));
        wallet.setBalance(wallet.getBalance() + amount);
        Transaction transaction = Transaction.builder()
                .dateCreated(Utils.formatVNDatetimeNow())
                .amount(amount)
                .description(String.format(TransactionSuccessMessage.TRANSACTION_COMPLETED_DESCRIPTION, bookingDetailId,
                        amount))
                .transactionType(TransactionType.PLUS)
                .wallet(wallet)
                .build();
        transactionRepository.save(transaction);
    }
}
