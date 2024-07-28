package com.fu.weddingplatform.response.feedback;

import com.fu.weddingplatform.response.couple.CoupleResponse;
import com.fu.weddingplatform.response.serviceSupplier.ServiceSupplierResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponse {
    String id;
    String description;
    String status;
    CoupleResponse couple;
    ServiceSupplierResponse serviceSupplier;
}
