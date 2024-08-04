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
@Table(name = "area")
public class Area {
  @Id
  @GeneratedValue(generator = "area-id")
  @GenericGenerator(name = "area-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.AreaIdGenerator")
  private String id;

  private String province;
  private String district;
  private String ward;
  private String apartmentNumber;
  private String status;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "supplier_id")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Supplier supplier;
}
