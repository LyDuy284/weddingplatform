package com.fu.weddingplatform.constant.email;

import com.fu.weddingplatform.request.email.MailVerifyAccountDTO;

public class VerifyAccountMail {
  public static final String content(MailVerifyAccountDTO content) {
    StringBuilder emailBody = new StringBuilder();

    emailBody.append("<html><body>");
    emailBody.append("<div style=\"display: flex; align-items: center; flex-direction: column; margin-top: 1rem;\">");
    emailBody.append("<div style=\"width: 52rem; background-color: #FFDAC0; padding: 2rem; border-radius: 0.5rem;\">");
    emailBody.append("<p>Thân gửi <b>" + content.getName() + "</b>,</p>");
    emailBody.append("<p><b>The Day</b> xin cảm ơn bạn đã tin tưởng và sử dụng nền tang của chúng tôi.</b>.</p>");
    emailBody.append("<div style=\"font-size: 1rem;\">");
    emailBody.append("<p style=\"margin: 0;\">Vui lòng xác thực tài khoản của bạn qua đường link:");
    emailBody
        .append("<a href=\"" + content.getLink() + "\" style=\"color: blue;\"><i>Xác thực tài khoản</i></a></p></div>");
    emailBody.append(
        "<p style=\"margin: 0.5rem 0;\">Mọi thắc mắc vui lòng liên hệ Email hoặc trực tiếp thông qua số điện thoại 0979477952(Mr.Duy).</p>");
    emailBody.append("<div><p><i>Trân trọng,</i></p>");
    emailBody.append("<div style=\"border-bottom: 2px dashed; margin-bottom: 1rem;\"></div>");
    emailBody.append("<div>The Day Wedding Platform</div>");
    emailBody.append(
        "<div>Địa chỉ: Vinhomes Grand Park, 18 Nguyễn Xiển, phường Long Thạnh Mỹ, Thành phố Thủ Đức, Thành Phố Hồ ChíMinh</div>");
    emailBody.append("<div>Mobile: 0979477952 - Mr.Duy</div>");
    emailBody.append("<div>Email: lyhieuduy9190@gmail.com</div></div></div></div></body></html>");

    return emailBody.toString();
  }
}