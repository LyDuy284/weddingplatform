package com.fu.weddingplatform.entity;

import javax.persistence.Column;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bookng_detail_history")
public class BookingDetailHistory {
    @Id
    @GeneratedValue(generator = "booking-detail-history-id")
    @GenericGenerator(name = "booking-detail-history-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.BookingDetailHistoryIdGenerator")
    private String id;
    @Column(columnDefinition = "text")
    private String description;
    private String createdAt;
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "booking_detail_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private BookingDetail bookingDetail;
}
