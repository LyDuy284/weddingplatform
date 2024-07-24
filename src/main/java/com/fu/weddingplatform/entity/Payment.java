package com.fu.weddingplatform.entity;

import java.sql.Date;

import javax.persistence.*;

import com.fu.weddingplatform.enums.PaymentType;
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
  private int transactionId;
  private Date dateCreated;
  private int amount;
  @Column(columnDefinition = "text")
  private String description;
  @Enumerated(EnumType.STRING)
  private PaymentType paymentType;
  private String paymentStatus;
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "couple_id")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Couple couple;
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "quotation_id")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Quotation quotation;

//  @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
//  @EqualsAndHashCode.Include
//  @ToString.Include
//  @JsonIgnore
//  private Collection<Transaction> transaction;
}