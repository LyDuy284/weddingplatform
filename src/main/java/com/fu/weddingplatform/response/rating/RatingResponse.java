package com.fu.weddingplatform.response.rating;

import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingResponse {
    private String id;
    private String dateCreated;
    private ServiceSupplierResponse serviceSupplierResponse;
    private int rating;
    private String comment;
}
