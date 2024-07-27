package com.fu.weddingplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fu.weddingplatform.enums.TransactionType;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(generator = "transaction-id")
    @GenericGenerator(name = "transaction-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.TransactionIdGenerator")
    private String id;
    private Date dateCreated;
    private int amount;
    @Column(columnDefinition = "text")
    private String description;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "wallet_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Wallet wallet;
}
