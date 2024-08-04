package com.fu.weddingplatform.request.serviceSupplier;

import javax.validation.constraints.NotEmpty;

import com.fu.weddingplatform.constant.validation.ValidationMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CreateServiceSupplier {
    @NotEmpty(message = "Supplier ID " + ValidationMessage.NOT_EMPTY)
    private String supplierId;
    @NotEmpty(message = "Service ID " + ValidationMessage.NOT_EMPTY)
    private String serviceId;
    private String name;
    private String description;
    private String images;
    private String type;
    private int price;
    private String promotionId;

}
