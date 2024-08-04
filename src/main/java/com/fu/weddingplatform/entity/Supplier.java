package com.fu.weddingplatform.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(generator = "supplier-id")
    @GenericGenerator(name = "supplier-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.SupplierIdGenerator")
    private String id;
    private String image;
    private String supplierName;
    private String contactPersonName;
    private String contactPhone;
    private String contactEmail;
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "accountId")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Account account;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<ServiceSupplier> serviceSuppliers;

    @OneToOne(mappedBy = "supplier", cascade = CascadeType.ALL)
    private Wallet wallet;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Promotion> promotions;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Area> areas;
}
