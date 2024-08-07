package com.fu.weddingplatform.entity;

import javax.persistence.Entity;
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
@Table(name = "transaction_summary")
public class TransactionSummary {
    @Id
    @GeneratedValue(generator = "transaction-summary-id")
    @GenericGenerator(name = "transaction-summary-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.TransactionSummaryIdEnerator")
    private String id;
    private String dateCreated;
    private String dateModified;
    private int totalAmount;
    private int platformFee;
    private int supplierAmount;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "booking_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Booking booking;
}
