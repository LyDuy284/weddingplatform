package com.fu.weddingplatform.request.serviceSupplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateServiceSupplier {
    private String id;
    private String name;
    private String description;
    private String images;
    private String type;
    private int price;
    private String promotionId;
}
