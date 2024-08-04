package com.fu.weddingplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "service_supplier")
public class ServiceSupplier {
    @Id
    @GeneratedValue(generator = "service-supplier-id")
    @GenericGenerator(name = "service-supplier-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.ServiceSupplierIdGenerator")
    private String id;
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String images;
    private String type;
    private String createAt;
    private int price;
    private String status;

    @OneToMany(mappedBy = "serviceSupplier", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<ComboServices> comboServices;

    @OneToMany(mappedBy = "serviceSupplier", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<PromotionServiceSupplier> promotionServiceSuppliers;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "service_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Services service;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "supplier_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Supplier supplier;

}
