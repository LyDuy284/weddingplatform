package com.fu.weddingplatform.request.service;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.fu.weddingplatform.constant.validation.ValidationMessage;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServiceDTO {
    @NotEmpty(message = "Service ID " + ValidationMessage.NOT_EMPTY)
    private String id;
    private String type;
    private String name;
    private String description;
    @Min(value = 0, message = "Value must be greater than 0")
    private int price;
    private String images;
    @NotEmpty(message = "Promotion ID " + ValidationMessage.NOT_EMPTY)
    private String promotionId;
}
