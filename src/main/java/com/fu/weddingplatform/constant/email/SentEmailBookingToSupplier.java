package com.fu.weddingplatform.constant.email;

import com.fu.weddingplatform.request.email.EmailCreateBookingToSupplier;

public class SentEmailBookingToSupplier {
    public static final String content(EmailCreateBookingToSupplier content) {
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("<html><body>");

        emailBody.append(
                "<div style=\"display: flex; align-items: center; flex-direction: column; margin-top: 1rem; \">");
        emailBody.append(
                "<div style=\"width: 72rem;  background-color: #FFDAC0; padding: 2rem; border-radius: 0.5rem;\">");
        emailBody.append(" <p>Thân gửi <b>" + content.getName() + "</b>,</p>");
        emailBody.append("<p><b>The Day</b> xin thông báo bạn có đơn hàng mới cần xác nhận.</p>");
        emailBody.append(
                "<div class=\"bill\" style=\"border: 2px black solid; width: 80%; padding: 1.8rem; margin: auto;  border-radius: 0.6rem;\">");
        emailBody.append("<h3 style=\"margin: 0\">The Day Wedding Platform</h3>");
        emailBody.append("<p>Mã Đơn Hàng: <b>" + content.getBookingDetailId() + "</b></p>");
        emailBody.append("<p>Ngày tạo: <b><i>" + content.getCreateAt() + "</i></b></p>");
        emailBody.append("<p>Khách hàng: <b>" + content.getCustomerName() + "</b></p>");
        emailBody.append("<p>Số điện thoại: <b>" + content.getPhone() + "</b></p>");
        emailBody.append("<p>Chi tiết:</p><div>");
        emailBody.append(
                "<div style=\" width: 90%; display: flex; justify-content: space-between; border-bottom: 2px dashed; padding-bottom: 0.6rem;\">");
        emailBody.append("<span style=\"width: 70%\"><b>" + content.getServiceSupplierName() + "</b></span>");
        emailBody.append("<span style=\"width: 30%\">" + content.getPrice() + "</span></div>");
        emailBody.append("<div style=\"margin-left: 1rem; padding-top: 1rem;\">");
        emailBody
                .append("<span>- Ngày hoàn thành:</span><i style=\"padding-left: 2rem; font-size: 1rem; font-size:14px \"><b>"
                        + content.getCompleteDate() + "</b></i></div>");
        emailBody.append("<div style=\"margin-left: 1rem; display:flex;\"><span>- Ghi chú:</span> ");
        emailBody.append("<span style=\"padding-left: 0.5rem; display: block; max-width: 80%\"><i>" + content.getNote()
                + "</i></span></div></div></div>");
        emailBody.append("<p>Vui lòng truy cập hệ thống để xác nhận đơn hàng: link</p>");
        emailBody.append(
                "<p>Mọi thắc mắc vui lòng gửi Email hoặc liên hệ trực tiếp thông qua số điện thoại 0979477952 (Mr.Duy).</p>");
        emailBody
                .append("<div><p><i>Trân trọng,</i></p><div style=\"border-bottom: 2px dashed; margin-bottom: 1rem;\"></div>");
        emailBody.append("<div>The Day Wedding Platform </div>");
        emailBody.append(
                "<div>Địa chỉ: Vinhomes Grand Park, 18 Nguyễn Xiển, phường Long Thạnh Mỹ, Thành phố Thủ Đức, Thành Phố Hồ Chí Minh</div>");
        emailBody.append(" <div>Mobile: 0979477952 - Mr.Duy</div>");
        emailBody.append("<div>Email: lyhieuduy9190@gmail.com</div></div></div> </div>");
        emailBody.append("</body></html>");

        return emailBody.toString();
    }
}
