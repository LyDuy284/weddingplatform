package com.fu.weddingplatform.entity;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private String transactionType;
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "invoice_detail_id")
    @EqualsAndHashCode.Include
    @ToString.Include
//    @OneToOne
//    @JoinColumn(name = "invoice_detail_id", unique = true)
    private InvoiceDetail invoiceDetail;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "payment_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Payment payment;
}
