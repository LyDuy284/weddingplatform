package com.fu.weddingplatform.response.rating;

import com.fu.weddingplatform.entity.Couple;
import com.fu.weddingplatform.entity.Services;
import com.fu.weddingplatform.response.couple.CoupleResponse;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.security.Provider;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingResponse {
    String id;
    Date dateCreated;
    int ratingValue;
    String description;
    String status;
    CoupleResponse couple;
    Services service;
}
