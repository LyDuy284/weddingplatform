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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "booking_history")
public class BookingHistory {
  @Id
  @GeneratedValue(generator = "booking-history-id")
  @GenericGenerator(name = "booking-history-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.BookingHistoryIdGenerator")
  private String id;
  private String createdAt;
  private String status;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "booking_id")
  @EqualsAndHashCode.Include
  @ToString.Include
  private BookingDetail bookingDetail;
}
