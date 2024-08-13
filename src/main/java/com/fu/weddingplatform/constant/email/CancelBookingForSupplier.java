package com.fu.weddingplatform.constant.email;

import com.fu.weddingplatform.request.email.CancelBookingMailForSupplierDTO;
import com.fu.weddingplatform.utils.Utils;

public class CancelBookingForSupplier {
    public static final String content(CancelBookingMailForSupplierDTO content) {
        StringBuilder emailBody = new StringBuilder();

        emailBody.append("<html><body>");
        emailBody.append(
                "<div style=\"display: flex; align-items: center; flex-direction: column; margin-top: 1rem;\">");
        emailBody.append(
                "<div style=\"width: 72rem; background-color: #FFDAC0; padding: 2rem; border-radius: 0.5rem;\">");
        emailBody.append("<p>Thân gửi <b>" + content.getSupplierName()
                + "</b>,</p><p><b>The Day</b> xin thông báo đơn hàng của bạn đã bị hủy bởi <b>"
                + content.getCoupleName()
                + "</b>.</p>");
        emailBody.append(
                "<div style=\"border: 2px solid black; padding: 1.8rem; margin: auto; border-radius: 0.6rem; width: 80%;\">");
        emailBody.append("<h3 style=\"margin: 0; text-align: center;\">The Day Wedding Platform</h3>");
        emailBody
                .append("<p>Mã Đơn Hàng: <b style=\"padding-left: 1rem;\">" + content.getBookingDetail().getId()
                        + "</b></p>");
        emailBody.append("<p>Khách hàng: <b style=\"padding-left: 1rem;\">" + content.getCoupleName() + "</b></p>");
        emailBody.append("<p>SĐT: <b style=\"padding-left: 1rem;\">" + content.getPhoneNumber() + "</b></p>");
        emailBody.append("<p>Ngày tạo: <b style=\"padding-left: 1rem;\"><i>" + content.getBookingDetail().getCreateAt()
                + "</i></b></p>");
        emailBody.append("<p>Chi tiết:</p>");
        emailBody.append(
                "<div style=\"width: 90%; display: flex; justify-content: space-between; border-bottom: 2px dashed; margin: auto; padding-bottom: 0.6rem; margin-bottom: 0.5rem; \">");
        emailBody.append("<span style=\"width: 65%; text-align: center;\"><b>Tên dịch vụ</b></span>");
        emailBody.append("<span style=\"width: 10%; text-align: center;\"><b>Số lượng</b></span>");
        emailBody.append("<span style=\"width: 25%; text-align: center;\">Giá</span></div>");
        emailBody.append(
                "<div style=\"width: 90%; display: flex; justify-content: space-between; margin: auto;  border-bottom: 2px dashed; padding-bottom: 0.6rem;\">");
        emailBody.append(
                "<span style=\"width: 65%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;\"><b>"
                        + content.getBookingDetail().getServiceSupplier().getName() + "</b></span>");
        emailBody
                .append("<span style=\"width: 10%; text-align: center;\"><b>" + content.getBookingDetail().getQuantity()
                        + "</b></span>");
        emailBody.append(
                "<span style=\"width: 25%; text-align: center;\">"
                        + Utils.formatAmountToVND(content.getBookingDetail().getPrice()) + "</span></div>");
        emailBody.append("<div style=\"padding-top: 1rem;\">");
        emailBody.append("<span>- Ngày hoàn thành:</span><i style=\"padding-left: 2rem; font-size: 14px;\"><b>"
                + content.getBookingDetail().getCompletedDate() + "</b></i></div>");
        emailBody.append("<div style=\"display: flex;\"><span>- Ghi chú:</span>");
        emailBody.append("<span style=\"padding-left: 0.5rem; max-width: 80%; overflow-wrap: break-word;\"><i>"
                + content.getBookingDetail().getNote() + "</i></span></div><br>");
        emailBody.append("<div style=\"width: 100%;\"><span><b>Lý do:</b></span>");
        emailBody.append("<span style=\"width: 100%;\">" + content.getReason() + "</span> </div> </div>");
        emailBody
                .append("<p style=\"margin: 0.5rem 0; margin-top: 1.5rem;\">Kiểm tra đơn hàng tại: <a href=\"#\">link</a></p>");
        emailBody.append(
                "<i style=\"font-size: 1rem;\"><b>Số tiền đặt cọc sẽ được hệ thống gửi về ví của bạn (nếu có).</b></i>");
        emailBody.append(
                "<p style=\"margin: 0.5rem 0;\">Mọi thắc mắc vui lòng liên hệ Email hoặc trực tiếp thông qua số điện thoại 0979477952 (Mr.Duy).</p>");
        emailBody.append("<div><p><i>Trân trọng,</i></p>");
        emailBody.append("<div style=\"border-bottom: 2px dashed; margin-bottom: 1rem;\"></div>");
        emailBody.append("<div>The Day Wedding Platform</div>");
        emailBody.append(
                "<div>Địa chỉ: Vinhomes Grand Park, 18 Nguyễn Xiển, phường Long Thạnh Mỹ, Thành phố Thủ Đức, Thành Phố Hồ Chí Minh</div>");
        emailBody.append("<div>Mobile: 0979477952 - Mr.Duy</div>");
        emailBody.append("<div>Email: lyhieuduy9190@gmail.com</div> </div></div></div></body></html>");
        return emailBody.toString();
    }
}
