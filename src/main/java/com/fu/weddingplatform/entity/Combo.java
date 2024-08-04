package com.fu.weddingplatform.entity;

import java.util.ArrayList;
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
@Table(name = "combo")
public class Combo {
  @Id
  @GeneratedValue(generator = "combo-id")
  @GenericGenerator(name = "combo-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.ComboIdGenerator")
  private String id;
  private String name;
  @Column(columnDefinition = "text")
  private String description;
  private String status;
  private String createAt;
  private String image;

  @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL)
  @EqualsAndHashCode.Include
  @ToString.Include
  @JsonIgnore
  private Collection<ComboServices> comboServices;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "staff_id")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Staff staff;
}
