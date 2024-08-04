package com.fu.weddingplatform.response.rating;

import com.fu.weddingplatform.entity.Services;
import com.fu.weddingplatform.response.couple.CoupleResponse;

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
    String id;
    String dateCreated;
    int ratingQuantityValue;
    int ratingTimeValue;
    int ratingQualityValue;
    String description;
    String status;
    CoupleResponse couple;
    Services service;
}
