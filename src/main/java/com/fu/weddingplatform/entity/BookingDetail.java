package com.fu.weddingplatform.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    private int quantity;
    @Column(columnDefinition = "text")
    private String note;
    private String status;
    private String createAt;
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
    @JoinColumn(name = "service_supplier_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private ServiceSupplier serviceSupplier;

    @OneToMany(mappedBy = "bookingDetail", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<BookingDetailHistory> bookingDetailHistories;

    @OneToMany(mappedBy = "bookingDetail", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Rating> ratings;

    @OneToMany(mappedBy = "bookingDetail", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<InvoiceDetail> invoiceDetails;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "promotion_serivce_supplier_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private PromotionServiceSupplier promotionServiceSupplier;

}
