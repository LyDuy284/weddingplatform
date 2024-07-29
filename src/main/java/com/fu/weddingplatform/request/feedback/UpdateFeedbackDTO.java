package com.fu.weddingplatform.request.feedback;

import com.fu.weddingplatform.constant.validation.ValidationMessage;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateFeedbackDTO {
    @NotEmpty(message = "ID " + ValidationMessage.NOT_EMPTY)
    String id;
    String description;
    @NotEmpty(message = "CoupleId ID " + ValidationMessage.NOT_EMPTY)
    String coupleId;
    @NotEmpty(message = "Supplier ID " + ValidationMessage.NOT_EMPTY)
    String serviceSupplierId;
}
