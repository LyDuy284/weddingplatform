package com.fu.weddingplatform.request.rating;

import com.fu.weddingplatform.constant.validation.ValidationMessage;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRatingDTO {
    @Min(value = 0, message = "Value must be greater than 0")
    @Max(value = 5, message = "Value must be smaller than 5")
    int ratingValue;
    Date dateCreated;
    String description;
    @NotEmpty(message = "CoupleId ID " + ValidationMessage.NOT_EMPTY)
    String coupleId;
    @NotEmpty(message = "Service ID " + ValidationMessage.NOT_EMPTY)
    String serviceId;
}
