package com.fu.weddingplatform.constant.email;

import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.request.email.RejectMailDTO;

public class RejectBookingDetail {
        public static final String content(RejectMailDTO content) {
                StringBuilder emailBody = new StringBuilder();

                emailBody.append("<html><body>");
                emailBody.append(
                                "<div style=\"display: flex; align-items: center; flex-direction: column; margin-top: 1rem; height: max-content;\">");
                emailBody.append(
                                "<div style=\"width: 72rem;  background-color: #FFDAC0; padding: 2rem; border-radius: 0.5rem;\">");
                emailBody.append("<p>Thân gửi <b>" + content.getCoupleName() + "</b>,</p>");
                emailBody.append(
                                "<p><b>The Day</b> xin thông báo bạn có dịch vụ đã bị từ chối bởi <b>"
                                                + content.getSupplierName()
                                                + "</b>.</p>");
                emailBody.append(
                                "<div style=\"border: 2px black solid; width: 80%; padding: 1.8rem; margin: auto;  border-radius: 0.6rem;\">");
                emailBody.append("<h3 style=\"margin: 0; text-align: center;\">The Day Wedding Platform</h3>");
                emailBody.append("<p>Mã Đơn Hàng: <b>" + content.getBookingDetail().getId() + "</b></p>");
                emailBody.append("<p>Nhà cung cấp: <b>" + content.getSupplierName() + "</b></p>");
                emailBody.append("<p>Ngày tạo: <b><i>" + content.getBookingDetail().getCreateAt() + "</i></b></p>");
                emailBody.append("<p>Chi tiết:</p><div>");
                emailBody.append(
                                "<div style=\" width: 90%; display: flex; justify-content: space-between; border-bottom: 2px dashed; padding-bottom: 0.6rem; margin-bottom: 0.5rem;\">");
                emailBody.append(
                                "<span style=\"width: 75%; text-align:center;\"><b>Tên dịch vụ</b></span>");
                emailBody.append("<span style=\"width: 8%; text-align: center;\"><b>Số lượng</b></span>");
                emailBody.append(
                                "<span style=\"width: 17%; text-align: center;\"><b>Giá</b></span></div>");
                emailBody.append(
                                "<div style=\" width: 90%; display: flex; justify-content: space-between; border-bottom: 2px dashed; padding-bottom: 0.6rem;\">");
                emailBody.append(
                                "<span style=\"width: 75%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;\"><b>"
                                                + content.getBookingDetail().getServiceSupplier().getName()
                                                + "</b></span>");
                emailBody.append("<span style=\"width: 8%; text-align: center;\"><b>"
                                + content.getBookingDetail().getQuantity()
                                + "</b></span>");
                emailBody.append(
                                "<span style=\"width: 17%; text-align: right;\">"
                                                + content.getBookingDetail().getPrice()
                                                + "</span></div>");
                emailBody.append(" <div style=\"margin-left: 1rem; padding-top: 1rem;\">");
                emailBody.append(
                                "<span>- Ngày hoàn thành:</span><i style=\"padding-left: 2rem; font-size: 1rem; font-size:14px \"><b>"
                                                + content.getBookingDetail().getCompletedDate() + "</b></i>");
                emailBody.append("</div><div style=\"margin-left: 1rem; display:flex;\"><span>- Ghi chú:</span>");
                emailBody.append("<span style=\"padding-left: 0.5rem; display: block; max-width: 80%\"><i>"
                                + content.getBookingDetail().getNote() + "</i></span>");
                emailBody.append("</div><br><div style=\"width: 90%;\"><span><b>Lý do:</b></span>");
                emailBody.append("<span style=\"width: 100%;\">" + content.getReason() + "</span></div></div></div>");
                if (content.getListCurrentBookingDetails().size() > 0) {
                        emailBody.append("<h4>Đơn hàng hiện tại:</h4>");
                        emailBody.append(
                                        "<div style=\"border: 2px black solid; width: 80%; padding: 1.8rem; margin: auto;  border-radius: 0.6rem;\">");
                        emailBody.append("<h3 style=\"margin: 0; text-align: center;\">The Day Wedding Platform</h3>");
                        emailBody.append("<p>Mã Đơn Hàng: <b>" + content.getBookingDetail().getBooking().getId()
                                        + "</b></p>");
                        emailBody.append(
                                        "<p>Ngày tạo: <b><i>" + content.getBookingDetail().getBooking().getCreatedAt()
                                                        + "</i></b></p>");
                        emailBody.append("<p>Chi tiết:</p><div><ul>");
                        for (BookingDetail bookingDetail : content.getListCurrentBookingDetails()) {
                                emailBody.append("<li style=\"padding-top: 1rem;\">");
                                emailBody.append(
                                                "<div style=\" width: 90%; display: flex; justify-content: space-between; border-bottom: 2px dashed; padding-bottom: 0.6rem;\">");
                                emailBody.append(
                                                "<span style=\"width: 75%; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;\"><b>"
                                                                + bookingDetail.getServiceSupplier().getName()
                                                                + "</b></span>");
                                emailBody
                                                .append("<span style=\"width: 8%; text-align: center;\"><b>"
                                                                + bookingDetail.getQuantity()
                                                                + "</b></span>");
                                emailBody.append(
                                                "<span style=\"width: 17%; text-align: right;\">"
                                                                + bookingDetail.getPrice() + "</span></div>");
                                emailBody
                                                .append("<div style=\"margin-left: 1rem; padding-top: 1rem;\"><span>- Ngày hoàn thành:</span>");
                                emailBody.append("<i style=\"padding-left: 2rem; font-size: 1rem; font-size:14px \"><b>"
                                                + bookingDetail.getCompletedDate() + "</b></i></div>");
                                emailBody.append(
                                                "<div style=\"margin-left: 1rem; display:flex;\"><span>- Ghi chú:</span> ");
                                emailBody.append(
                                                "<span style=\"padding-left: 0.5rem; display: block; max-width: 80%\"><i>"
                                                                + bookingDetail.getNote() + "</i></span></div></li>");
                        }
                        emailBody.append("</ul></div><br>");
                        emailBody.append(
                                        "<div><span style=\"display: inline-block; width: 8rem;\">Tổng tiền:</span><b><i>"
                                                        + content.getTotalPrice() + "</i></b></div>");
                        emailBody.append(
                                        "<div><span style=\"display: inline-block; width: 8rem;\">Đã thanh toán:</span><b><i>"
                                                        + content.getPaidPrice() + " </i></b></div>");
                        emailBody.append(
                                        "<div><span style=\"display: inline-block; width: 8rem;\">Còn lại:</span><b><i>"
                                                        + content.getRemaining()
                                                        + "</i></b></div></div>");
                }
                emailBody.append("<p>Kiểm tra trạng thái đơn hàng tại: link</p>");
                emailBody.append(
                                "<p>Mọi thắc mắc vui lòng gửi Email hoặc liên hệ trực tiếp thông qua số điện thoại 0979477952 (Mr.Duy).</p>");
                emailBody.append("<div><p><i>Trân trọng,</i></p>");
                emailBody.append("<div style=\"border-bottom: 2px dashed; margin-bottom: 1rem;\"></div>");
                emailBody.append("<div>The Day Wedding Platform </div>");
                emailBody.append(
                                "<div>Địa chỉ: Vinhomes Grand Park, 18 Nguyễn Xiển, phường Long Thạnh Mỹ, Thành phố Thủ Đức, Thành Phố Hồ Chí Minh</div>");
                emailBody.append("<div>Mobile: 0979477952 - Mr.Duy</div>");
                emailBody.append(" <div>Email: lyhieuduy9190@gmail.com</div>");
                emailBody.append("</div></div></div></body></html>");
                return emailBody.toString();
        }
}
