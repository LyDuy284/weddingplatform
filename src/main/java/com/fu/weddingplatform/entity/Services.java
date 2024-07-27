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
@Table(name = "service")
public class Services {

    @Id
    @GeneratedValue(generator = "service-id")
    @GenericGenerator(name = "service-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.ServiceIdGenerate")
    private String id;
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String images;
    private int price;
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "service_supplier_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private ServiceSupplier serviceSupplier;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "category_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Category category;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<ComboService> comboServices;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<BookingDetail> bookingDetails;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Rating> ratings;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Quotation> quotations;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<PromotionServiceEntity> promotionServices;
}
