package com.fu.weddingplatform.constant.payment.vnpay;

public final class VNPayConstant {
    public final static String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?";
    public final static String VNP_RETURN_URL = "http://localhost:8080/payment/call-back";
    public final static String VNP_RETURN_URL_SERVER = "https://thedaywedding-hkaybdgafndhecbn.southeastasia-01.azurewebsites.net/payment/call-back";
    public final static String VNP_REDIRECT_CLIENT = "https://the-day-six.vercel.app/booking-history";
    public final static String VNP_RETURN_CLIENT_URL = "http://localhost:5173/payment?isSuccess=%s&amount=%s";
    public final static String VNP_TMN_CODE = "PODT5GPF";
    public final static String VNP_HASH_SECRET = "ERCN2HESH2DZAP4AAC7HY5Q10B5WH448";
    public final static String VNP_VERSION = "2.1.0";
    public final static String VNP_COMMAND_PAY = "pay";
    public final static String VNP_ORDER_TYPE = "topup";
}
