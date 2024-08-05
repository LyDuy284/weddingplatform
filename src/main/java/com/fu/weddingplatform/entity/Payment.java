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
import com.fu.weddingplatform.enums.PaymentMethod;
import com.fu.weddingplatform.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    @Column(unique = true)
    private int tradingCode;
    private String dateCreated;
    private int amount;
    @Column(columnDefinition = "text")
    private String description;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String paymentStatus;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "couple_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Couple couple;

    // @JsonIgnore
    // @ManyToOne(fetch = FetchType.LAZY)
    // @Fetch(FetchMode.JOIN)
    // @JoinColumn(name = "booking_detail_id")
    // @EqualsAndHashCode.Include
    // @ToString.Include
    // private BookingDetail bookingDetail;

    // @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    // @EqualsAndHashCode.Include
    // @ToString.Include
    // @JsonIgnore
    // private Collection<PaymentBookingService> paymentBookingServices;
}
