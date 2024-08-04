package com.fu.weddingplatform.request.rating;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.fu.weddingplatform.constant.validation.ValidationMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRatingDTO {
    @Min(value = 0, message = "Value must be greater than 0")
    @Max(value = 5, message = "Value must be smaller than 5")
    int ratingValue;
    String description;
    @NotEmpty(message = "CoupleId ID " + ValidationMessage.NOT_EMPTY)
    String coupleId;
    @NotEmpty(message = "Booking Detail ID " + ValidationMessage.NOT_EMPTY)
    String bookingDetailId;
}
