package com.fu.weddingplatform.entity;

import java.sql.Date;

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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transaction")
public class Transaction {

  @Id
  @GeneratedValue(generator = "transaction-id")
  @GenericGenerator(name = "transaction-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.TransactionIdGenerator")
  private String id;
  private Date dateCreated;
  @Column(columnDefinition = "text")
  private String description;
  private String name;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "payment_id")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Payment payment;
}
