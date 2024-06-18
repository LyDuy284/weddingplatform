package com.fu.weddingplatform.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

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
    private float price;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "promotion_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Promotion promotion;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<ServiceCombo> serviceCombos;
}
