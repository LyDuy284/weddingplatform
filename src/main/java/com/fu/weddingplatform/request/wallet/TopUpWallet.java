package com.fu.weddingplatform.request.wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TopUpWallet {
    String coupleId;
    String staffId;
    int amount;
}
