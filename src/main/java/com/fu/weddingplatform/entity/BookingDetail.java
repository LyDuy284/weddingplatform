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

import java.sql.Date;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "booking_detail")
public class BookingDetail {

    @Id
    @GeneratedValue(generator = "booking-detail-id")
    @GenericGenerator(name = "booking-detail-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.BookingDetailIdGenerator")
    private String id;
    private int price;
    @Column(columnDefinition = "text")
    private String note;
    private String status;
    private String completedDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "bookingId")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Booking booking;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "serviceId")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Services service;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "quotation_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Quotation quotation;

    @OneToMany(mappedBy = "bookingDetail", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<PaymentBookingService> paymentBookingServices;
}
