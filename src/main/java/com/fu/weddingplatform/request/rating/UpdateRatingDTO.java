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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRatingDTO {
    @NotEmpty(message = "ID " + ValidationMessage.NOT_EMPTY)
    String id;
    @Min(value = 0, message = "Value must be greater than 0")
    @Max(value = 5, message = "Value must be smaller than 5")
    int ratingQuantityValue;
    @Min(value = 0, message = "Value must be greater than 0")
    @Max(value = 5, message = "Value must be smaller than 5")
    int ratingTimeValue;
    @Min(value = 0, message = "Value must be greater than 0")
    @Max(value = 5, message = "Value must be smaller than 5")
    int ratingQualityValue;
    String description;
    @NotEmpty(message = "CoupleId ID " + ValidationMessage.NOT_EMPTY)
    String coupleId;
}
