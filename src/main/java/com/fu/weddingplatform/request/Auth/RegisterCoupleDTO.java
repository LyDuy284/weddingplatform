package com.fu.weddingplatform.request.Auth;

import com.fu.weddingplatform.constant.validation.ValidationMessage;
import com.fu.weddingplatform.constant.validation.ValidationSize;
import com.fu.weddingplatform.custom.customAnnotation.ValidPhoneNumber;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterCoupleDTO {

    @Email(message = ValidationMessage.EMAIL_INVALID_MESSAGE)
    private String email;
    @Size(min = ValidationSize.PASSWORD_MIN, max = ValidationSize.PASSWORD_MAX,
            message = ValidationMessage.PASSWORD_INVALID_MESSAGE)
    private String password;
    private String name;
    @NotNull(message = ValidationMessage.PHONE_NUMBER_NOT_NULL)
    @ValidPhoneNumber
    private String phoneNumber;
    private String address;
    private String partnerName1;
    private String partnerName2;
    private Date weddingDate;

}
