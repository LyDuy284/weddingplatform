package com.fu.weddingplatform.response.couple;

import java.sql.Date;

import com.fu.weddingplatform.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoupleResponse {
    String id;
    String partnerName1;
    String partnerName2;
    String status;
    Date weddingDate;
    Account account;
}
