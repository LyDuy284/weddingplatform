package com.fu.weddingplatform.entity;

import java.sql.Date;
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
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(generator = "promotion-id")
    @GenericGenerator(name = "promotion-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.PromotionIdGenerate")
    private String id;
    @Column(columnDefinition = "text")
    private String promotionDetails;
    private int percent;
    private Date startDate;
    private Date endDate;
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "service_supplier_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private ServiceSupplier serviceSupplier;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Services> services;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<QuoteDetail> quoteDetails;

}
