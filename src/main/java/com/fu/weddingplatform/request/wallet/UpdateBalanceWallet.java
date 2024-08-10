package com.fu.weddingplatform.request.wallet;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBalanceWallet {
    int accountId;
    String type;
    int amount;
    String description;
}
