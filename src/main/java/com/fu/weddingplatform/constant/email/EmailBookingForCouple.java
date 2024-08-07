package com.fu.weddingplatform.constant.email;

import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.request.email.EmailBookingForCoupleDTO;
import com.fu.weddingplatform.utils.Utils;

public class EmailBookingForCouple {

        public static final String content(EmailBookingForCoupleDTO emailBookingForCouple) {
                StringBuilder emailBody = new StringBuilder();
                emailBody.append("<html><body>");
                emailBody.append(
                                "<div style=\"display: flex; align-items: center; flex-direction: column; margin-top: 1rem; \">");
                emailBody.append(
                                "  <div style=\"width: fit-content;  background-color: #FFDAC0; padding: 2rem; border-radius: 0.5rem;\">");
                emailBody.append("<p>Thân gửi <b>" + emailBookingForCouple.getName() + "<b>,</p>");
                emailBody.append(
                                "<p>Lời đầu tiên, <b>The Day<b> xin gửi lời cảm ơn đến quý khách vì đã tin tưởng và sử dụng nền tảng cho ngày trọng đại của quý khách.</p>");
                emailBody.append(
                                "<p> Chúng tôi xin thông báo quý khách đã đặt dịch vụ thành công. Vui lòng đợi nhà cung cấp phản hồi.</p>");
                emailBody.append(
                                " <div class=\"bill\" style=\"border: 2px black solid; width: 80%; padding: 1.8rem; margin: auto;  border-radius: 0.6rem;\">");
                emailBody.append("<h3 style=\"margin: 0\">The Day Wedding Platform</h3>");
                emailBody.append("<p>Mã Đơn Hàng: <b>" + emailBookingForCouple.getBookingId() + "</b></p>");
                emailBody.append("<p>Ngày tạo: <b><i>" + emailBookingForCouple.getCreatedAt() + "</i></b></p>");
                emailBody.append("<p>Chi tiết:</p>");
                emailBody.append("<div><ul>");
                for (BookingDetail bookingDetail : emailBookingForCouple.getListBookingDetails()) {
                        emailBody.append("<li style=\"padding-top: 1rem;\">");
                        emailBody.append(
                                        "<div style=\" width: 90%; display: flex; justify-content: space-between; border-bottom: 2px dashed; padding-bottom: 0.6rem;\">");
                        emailBody.append("<span style=\"width: 70%\"><b>"
                                        + bookingDetail.getServiceSupplier().getName() + "</b></span>");
                        emailBody.append("<span style=\\\"width: 30%\\\">"
                                        + Utils.formatAmountToVND(bookingDetail.getPrice()) + "</span>");
                        emailBody.append("</div><div style=\"margin-left: 1rem; padding-top: 1rem;\">");
                        emailBody.append("<span>- Ngày hoàn thành:</span>");
                        emailBody.append("<i style=\"padding-left: 2rem; font-size: 1rem; font-size: 14px;\"><b>"
                                        + bookingDetail.getCompletedDate()
                                        + "</b></i>");
                        emailBody.append("</div><div style=\"margin-left: 1rem; display:flex;\">");
                        emailBody.append(
                                        "<span>- Ghi chú:</span> <span style=\"padding-left: 0.5rem; display: block; max-width: 80%\"><i>"
                                                        + bookingDetail.getNote() + "</i></span>");
                        emailBody.append("</div></li>");
                }
                emailBody.append("</ul></div>");
                emailBody.append(
                                "<div><span style=\"display: inline-block; width: 8rem;\">Tổng tiền:</span><b><i>"
                                                + emailBookingForCouple.getTotalPrice() + " </i></b>VND</div>");
                emailBody.append(
                                "<div><span style=\"display: inline-block; width: 8rem;\">Đã thanh toán:</span><b><i>0 </i></b>VND</div>");
                emailBody.append("<div><span style=\"display: inline-block; width: 8rem;\">Còn lại:</span><b><i>"
                                + emailBookingForCouple.getTotalPrice() + " </i></b>VND</div></div>");
                emailBody.append("<p>Kiểm tra trạng thái đơn hàng tại: link</p>");
                emailBody.append(
                                "<p>Mọi thắc mắc quý khách có thể gửi Email hoặc liên hệ trực tiếp thông qua số điện thoại 0979477952 (Mr.Duy).</p>");
                emailBody.append("<p>The Day chúc quý khách có một trải nghiệm tuyệt vời.</p><br><div>");
                emailBody.append("<p><i>Trân trọng,</i></p>");
                emailBody.append(" <div style=\"border-bottom: 2px dashed;\"></div>");
                emailBody.append("<div>The Day Wedding Platform </div>");
                emailBody.append(
                                "<div>Địa chỉ: Vinhomes Grand Park, 18 Nguyễn Xiển, phường Long Thạnh Mỹ, Thành phố Thủ Đức, Thành Phố Hồ Chí Minh</div>");

                emailBody.append("<div>Mobile: 0979477952 - Mr.Duy</div>");
                emailBody.append("<div>Email: lyhieuduy9190@gmail.com</div>");
                emailBody.append("</div></div></div>");

                emailBody.append("</body></html>");
                return emailBody.toString();
        }

}
