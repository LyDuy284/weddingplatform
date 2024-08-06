package com.fu.weddingplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(generator = "wallet-id")
    @GenericGenerator(name = "wallet-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.WalletIdGenerator")
    private String id;
    private int balance;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<WalletHistory> walletHistories;

    /*
    @OneToOne
    @JoinColumn(name = "supplier_id", unique = true)
    private Supplier supplier;

    @OneToOne
    @JoinColumn(name = "couple_id", unique = true)
    private Couple couple;*/


}
