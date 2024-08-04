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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "invoice_detail")
public class InvoiceDetail {
  @Id
  @GeneratedValue(generator = "invoice-detail-id")
  @GenericGenerator(name = "invoice-detail-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.InvoiceDetailIdGenerator")
  private String id;
  private String status;
  private int price;
  private String createAt;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "booking_detail_id")
  @EqualsAndHashCode.Include
  @ToString.Include
  private BookingDetail bookingDetail;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "invoice_id")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Invoice invoice;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "promotion_serivce_supplier_id")
  @EqualsAndHashCode.Include
  @ToString.Include
  private PromotionServiceSupplier promotionServiceSupplier;

}
