package com.fu.weddingplatform.constant.email;

import com.fu.weddingplatform.request.email.MailApproveForCoupleDTO;
import com.fu.weddingplatform.utils.Utils;

public class ApprovedMailForCouple {
    public static final String content(MailApproveForCoupleDTO content) {
        StringBuilder emailBody = new StringBuilder();

        emailBody.append("<html><body>");
        emailBody.append(
                "<div style=\"display: flex; align-items: center; flex-direction: column; margin-top: 1rem;\">");
        emailBody.append(
                "<div style=\"width: 72rem; background-color: #FFDAC0; padding: 2rem; border-radius: 0.5rem;\">");
        emailBody.append("<p>Thân gửi <b>" + content.getCouple().getAccount().getName() + "</b>,</p>");
        emailBody
                .append("<p><b>The Day</b> xin thông báo dịch vụ <b>" + content.getBookingDetail().getId()
                        + "</b> đã được xác nhận bởi <b>"
                        + content.getBookingDetail().getServiceSupplier().getSupplier().getSupplierName()
                        + "</b>.</p>");
        emailBody.append(
                "<div style=\"border: 2px solid black; padding: 1.8rem; margin: auto; border-radius: 0.6rem; width: 80%;\">");
        emailBody.append("<h3 style=\"margin: 0; text-align: center;\">The Day Wedding Platform</h3>");
        emailBody
                .append("<p>Mã Đơn Hàng: <b style=\"padding-left: 1rem;\">" + content.getBookingDetail().getId()
                        + "</b></p>");
        emailBody.append("<p>Ngày tạo: <b style=\"padding-left: 1rem;\"><i>" + content.getBookingDetail().getCreateAt()
                + "</i></b></p>");

        emailBody.append("<p>Chi tiết:</p>");
        emailBody.append(
                "<div style=\"width: 90%; display: flex; justify-content: space-between; margin: auto; margin-bottom: 0.5rem; \">");
        emailBody.append("<span style=\"width: 65%; text-align: center;\"><b>Tên dịch vụ</b></span>");
        emailBody.append("<span style=\"width: 10%; text-align: center;\"><b>Số lượng</b></span>");
        emailBody.append("<span style=\"width: 25%; text-align: center;\"><b>Giá</b></span></div>");
        emailBody.append("<div><ul style=\"list-style-type: none; padding-left: 0;\">");
        emailBody.append("<li style=\"margin-top: 1rem;\">");
        emailBody.append(
                "<div style=\"width: 90%; display: flex; justify-content: space-between; border-bottom: 2px dashed; margin: 0.8rem auto; padding: 0.6rem 0rem;\">");
        emailBody.append(
                "<span style=\"width: 65%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;\"><b>"
                        + content.getBookingDetail().getServiceSupplier().getName() + "</b></span>");

        emailBody
                .append("<span style=\"width: 10%; text-align: center;\"><b>" + content.getBookingDetail().getQuantity()
                        + "</b></span>");

        emailBody.append("<span style=\"width: 25%; text-align: center;\"><b>"
                + Utils.formatAmountToVND(content.getBookingDetail().getPrice()) + "</b></span></div>");

        emailBody.append("<div style=\"padding-left: 4rem; margin-bottom: 2rem;\"><div>");
        emailBody.append(
                "<span>- Ngày hoàn thành:</span> <i style=\"padding-left: 1rem; font-size: 1rem; font-size: 14px;\"><b>"
                        + content.getBookingDetail().getCompletedDate() + "</b></i>");

        emailBody.append("</div><div style=\"display:flex;\"><span>- Ghi chú:</span>");
        emailBody.append("<span style=\"padding-left: 1rem; display: block; max-width: 80%\"><i>"
                + content.getBookingDetail().getNote() + "</i></span>");
        emailBody.append("</div></div></li></ul></div>");
        emailBody.append("<div><span style=\"display: inline-block; width: 8rem;\">Tổng tiền:</span><b><i>"
                + Utils.formatAmountToVND(content.getBookingDetail().getPrice()) + "</i></b></div>");
        emailBody.append("<div><span style=\"display: inline-block; width: 8rem;\">Đã thanh toán:</span><b><i>"
                + Utils.formatAmountToVND(0) + "</i></b></div>");
        emailBody.append("<div><span style=\"display: inline-block; width: 8rem;\">Còn lại:</span><b><i>"
                + Utils.formatAmountToVND(content.getBookingDetail().getPrice()) + "</i></b></div>");
        emailBody.append(
                "<p style=\"margin: 0.5rem 0; margin-top: 1.5rem;\">Kiểm tra trạng thái đơn hàng tại: <a href=\"#\">link</a></p>");

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
