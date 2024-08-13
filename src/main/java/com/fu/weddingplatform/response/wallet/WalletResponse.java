package com.fu.weddingplatform.response.wallet;

import com.fu.weddingplatform.response.Account.AccountResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletResponse {
    String id;
    int balance;
    AccountResponse account;
}
