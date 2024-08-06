package com.fu.weddingplatform.entity;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fu.weddingplatform.enums.PaymentMethod;
import com.fu.weddingplatform.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(generator = "payment-id")
    @GenericGenerator(name = "payment-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.PaymentIdGenerator")
    private String id;
    private String dateCreated;
    private int amount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Transaction> transactions;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "invoice_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Invoice invoice;
}
