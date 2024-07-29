package com.fu.weddingplatform.serviceImp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.weddingplatform.constant.booking.BookingConstant;
import com.fu.weddingplatform.constant.booking.BookingErrorMessage;
import com.fu.weddingplatform.constant.booking.BookingStatus;
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
        Booking booking = bookingRepository.findById(paymentRequest.getBookingId()).
                orElseThrow(() -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));
        if (!booking.getStatus().equals(BookingStatus.CONFIRM)) {
            throw new ErrorException(PaymentErrorMessage.CONDITIONS_NOT_VALID);
        }
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
                .bookingId(booking.getId())
                .paymentType(paymentRequest.getPaymentType())
                .build();
        vnp_Params.put("vnp_OrderInfo", objectMapper.writeValueAsString(paymentDTO));

        int amount = booking.getBookingDetails().stream().mapToInt(BookingDetail::getPrice).sum();
        switch (paymentRequest.getPaymentType()) {
            case DEPOSIT -> vnp_Params.put("vnp_Amount",
                    String.valueOf((int) (amount * PaymentTypeValue.DEPOSIT_VALUE) * 100L));
            case FINAL_PAYMENT -> {
                paymentRepository.findByBookingType(paymentRequest.getBookingId(), PaymentType.DEPOSIT)
                        .orElseThrow(() -> new ErrorException(PaymentErrorMessage.DEPOSIT_NOT_PAID));
                vnp_Params.put("vnp_Amount",
                        String.valueOf((int) (amount * PaymentTypeValue.FINAL_PAYMENT_VALUE) * 100L));
            }
        }

        vnp_Params.put("vnp_ReturnUrl", VNPayConstant.VNP_RETURN_URL);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
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
            int amount = Integer.parseInt(request.getParameter("vnp_Amount")) / 100;
            CreatePaymentDTO paymentDTO = objectMapper.readValue(paymentInfor, CreatePaymentDTO.class);
            Booking booking = bookingRepository.findById(paymentDTO.getBookingId())
                    .orElseThrow(() -> new ErrorException(BookingErrorMessage.BOOKING_NOT_FOUND));
            Payment payment = Payment.builder()
                    .tradingCode(Integer.parseInt(vnpTransactionNo))
                    .description(String.format(PaymentSuccessMessage.PAYMENT_DESCRIPTION, booking.getCouple().getId(),
                            amount, booking.getId()))
                    .paymentMethod(PaymentMethod.VNPAY)
                    .amount(amount)
                    .paymentType(paymentDTO.getPaymentType())
                    .dateCreated(new Date(System.currentTimeMillis()))
                    .paymentStatus(PaymentStatus.COMPLETED)
                    .couple(booking.getCouple())
                    .booking(booking)
                    .build();
            if (paymentDTO.getPaymentType().equals(PaymentType.FINAL_PAYMENT)) {
                completeMoneyForServiceSupplier(booking, booking.getQuotation().getServiceSupplier().getId());
            }
            paymentRepository.save(payment);
            response.sendRedirect("https://www.youtube.com/");
        }
    }

    private void completeMoneyForServiceSupplier(Booking booking, String serviceSuplierId){
        int totalPrice = booking.getBookingDetails().stream().mapToInt(BookingDetail::getPrice).sum();
        double amount = totalPrice * (1 - BookingConstant.BOOKING_COMMISSION_VALUE);
        Wallet wallet = walletRepository.findByServiceSupplierId(serviceSuplierId)
                .orElseThrow(() -> new ErrorException(WalletErrorMessage.NOT_FOUND));
        wallet.setBalance(wallet.getBalance() + (int)amount);
        Transaction transaction = Transaction.builder()
                .dateCreated(new Date(System.currentTimeMillis()))
                .amount((int)amount)
                .description(String.format(TransactionSuccessMessage.TRANSACTION_COMPLETED_DESCRIPTION, booking.getId(), amount))
                .transactionType(TransactionType.PLUS)
                .wallet(wallet)
                .build();
        transactionRepository.save(transaction);
    }
}
