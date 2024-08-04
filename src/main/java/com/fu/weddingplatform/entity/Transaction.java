package com.fu.weddingplatform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fu.weddingplatform.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private String dateCreated;
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
