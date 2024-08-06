package com.fu.weddingplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "wallet_history")
public class WalletHistory {
    @Id
    @GeneratedValue(generator = "wallet-history-id")
    @GenericGenerator(name = "wallet-history-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.WalletHistoryIdGenerator")
    private String id;
    private int amount;
    private String type;
    private String createDate;
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "wallet_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Wallet wallet;
}
