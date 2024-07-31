package com.fu.weddingplatform.request.account;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fu.weddingplatform.constant.validation.ValidationMessage;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSupplierDTO {
  private String supplierId;
  private String name;
  private String image;
  @NotNull(message = ValidationMessage.PHONE_NUMBER_NOT_NULL)
  @ValidPhoneNumber
  private String phoneNumber;
  @NotNull(message = ValidationMessage.PHONE_NUMBER_NOT_NULL)
  @ValidPhoneNumber
  private String contactNumber;
  @Email(message = ValidationMessage.EMAIL_INVALID_MESSAGE)
  private String contactEmail;
  private String province;
  private String district;
  private String ward;
  private String apartmentNumber;
}
