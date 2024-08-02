package com.fu.weddingplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "payment_booking_service")
public class PaymentBookingService {
    @Id
    @GeneratedValue(generator = "payment-booking-service-id")
    @GenericGenerator(name = "payment-booking-service-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.PaymentBookingServiceIdGenerate")
    private String id;
    private String status;
    private String createAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "payment_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Payment payment;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "booking_detail_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private BookingDetail bookingDetail;
}
