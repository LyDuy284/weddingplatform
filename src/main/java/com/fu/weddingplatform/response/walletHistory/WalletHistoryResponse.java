package com.fu.weddingplatform.response.walletHistory;

import com.fu.weddingplatform.response.wallet.WalletResponse;
import lombok.*;


@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class WalletHistoryResponse {
     String id;
     int amount;
     String type;
     String createDate;
     String description;
     String walletId;
}
