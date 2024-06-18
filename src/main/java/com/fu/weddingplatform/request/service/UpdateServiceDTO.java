package com.fu.weddingplatform.request.service;

import lombok.*;

import javax.validation.constraints.Min;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServiceDTO {
    private String id;
    private String categoryId;
    private String name;
    private String description;
    @Min(value = 0, message = "Value must be greater than 0")
    private float price;
}
