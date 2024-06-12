package com.fu.weddingplatform.response.couple;

import com.fu.weddingplatform.entity.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Date;

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
