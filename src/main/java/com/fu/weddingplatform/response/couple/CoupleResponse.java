package com.fu.weddingplatform.response.couple;

import java.sql.Date;

import com.fu.weddingplatform.entity.Account;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CoupleResponse {
    String id;
    String partnerName1;
    String partnerName2;
    String status;
    Date weddingDate;
    Account account;
}
