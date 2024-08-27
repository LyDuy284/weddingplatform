package com.fu.weddingplatform.request.Auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fu.weddingplatform.constant.validation.ValidationMessage;
import com.fu.weddingplatform.constant.validation.ValidationSize;
import com.fu.weddingplatform.custom.customAnnotation.ValidPhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterCoupleDTO {

    @Email(message = ValidationMessage.EMAIL_INVALID_MESSAGE)
    private String email;
    @Size(min = ValidationSize.PASSWORD_MIN, max = ValidationSize.PASSWORD_MAX, message = ValidationMessage.PASSWORD_INVALID_MESSAGE)
    private String password;
    private String name;
    @NotNull(message = ValidationMessage.PHONE_NUMBER_NOT_NULL)
    @ValidPhoneNumber
    private String phoneNumber;
    private String address;
    private String partnerName1;
    private String partnerName2;

}
