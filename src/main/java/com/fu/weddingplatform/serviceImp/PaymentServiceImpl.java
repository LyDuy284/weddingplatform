package com.fu.weddingplatform.serviceImp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fu.weddingplatform.constant.booking.BookingStatus;
import com.fu.weddingplatform.constant.payment.PaymentErrorMessage;
import com.fu.weddingplatform.constant.payment.PaymentStatus;
import com.fu.weddingplatform.constant.payment.PaymentSuccessMessage;
import com.fu.weddingplatform.constant.payment.PaymentTypeValue;
import com.fu.weddingplatform.constant.payment.vnpay.VNPayConstant;
import com.fu.weddingplatform.constant.quotation.QuotationErrorMessage;
import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.Payment;
import com.fu.weddingplatform.entity.Quotation;
import com.fu.weddingplatform.enums.PaymentType;
import com.fu.weddingplatform.exception.ErrorException;
import com.fu.weddingplatform.repository.BookingRepository;
import com.fu.weddingplatform.repository.CoupleRepository;
import com.fu.weddingplatform.repository.PaymentRepository;
import com.fu.weddingplatform.repository.QuotationRepository;
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
import java.time.LocalDate;
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

    @Override
    public String requestPaymentVNP(HttpServletRequest req, HttpServletResponse resp, CreatePaymentDTO paymentRequest) throws JsonProcessingException {
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
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
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

    private Map<String, String> setVNPParams(HttpServletRequest req, CreatePaymentDTO paymentRequest) throws JsonProcessingException {
        Quotation quotation = quotationRepository.findById(paymentRequest.getQuotationId())
                .orElseThrow(() -> new ErrorException(QuotationErrorMessage.QUOTATION_NOT_FOUND));
        if(!quotation.getBooking().getStatus().equals(BookingStatus.CONFIRM)){
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
        //set order infor
        CreatePaymentDTO paymentDTO = CreatePaymentDTO.builder()
                .quotationId(quotation.getId())
                .paymentType(paymentRequest.getPaymentType())
                .build();
        vnp_Params.put("vnp_OrderInfo", objectMapper.writeValueAsString(paymentDTO));

        switch (paymentRequest.getPaymentType()) {
            case DEPOSIT ->
                    vnp_Params.put("vnp_Amount", String.valueOf((int) (quotation.getPrice() * PaymentTypeValue.DEPOSIT_VALUE) * 100L));
            case FINAL_PAYMENT ->
                    vnp_Params.put("vnp_Amount", String.valueOf((int) (quotation.getPrice() * PaymentTypeValue.FINAL_PAYMENT_VALUE) * 100L));
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
            response.sendRedirect(String.format(VNPayConstant.VNP_RETURN_CLIENT_URL, false, request.getParameter("vnp_Amount")));
        } else {
            String paymentInfor = request.getParameter("vnp_OrderInfo");
            String transactionId = request.getParameter("vnp_TransactionNo");
            int amount = Integer.parseInt(request.getParameter("vnp_Amount")) / 100;
            CreatePaymentDTO paymentDTO = objectMapper.readValue(paymentInfor, CreatePaymentDTO.class);
            Quotation quotation = quotationRepository.findById(paymentDTO.getQuotationId())
                    .orElseThrow(() -> new ErrorException(QuotationErrorMessage.QUOTATION_NOT_FOUND));
            Payment payment = Payment.builder()
                    .transactionId(Integer.parseInt(transactionId))
                    .description(String.format(PaymentSuccessMessage.PAYMENT_DESCRIPTION, quotation.getCouple().getId(), amount, quotation.getService().getId()))
                    .amount(amount)
                    .paymentType(paymentDTO.getPaymentType())
                    .dateCreated(Date.valueOf(String.valueOf(LocalDateTime.now())))
                    .paymentStatus(PaymentStatus.COMPLETED)
                    .couple(quotation.getCouple())
                    .quotation(quotation)
                    .build();
            try{
                if(paymentDTO.getPaymentType().equals(PaymentType.FINAL_PAYMENT)){
                    Booking booking = quotation.getBooking();
                    booking.setStatus(BookingStatus.DONE);
                    booking.setCompletedDate(Date.valueOf(String.valueOf(LocalDateTime.now())));
                    bookingRepository.save(booking);
                }
                paymentRepository.save(payment);
                response.sendRedirect("https://www.youtube.com/");
            }catch (ErrorException ex){
                response.sendRedirect("https://www.youtube.com/");
            }
        }
    }

}
