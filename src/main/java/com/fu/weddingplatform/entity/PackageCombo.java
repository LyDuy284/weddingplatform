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
@Table(name = "package_combo")
public class PackageCombo {

    @Id
    @GeneratedValue(generator = "package-combo-id")
    @GenericGenerator(name = "package-combo-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.PackageComboIdGenerate")
    private String id;
    private String packageName;
    @Column(columnDefinition = "text")
    private String description;
    private float price;
    private String status;

    @OneToMany(mappedBy = "packageCombo", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<ServiceCombo> serviceCombos;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "service_supplier_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private ServiceSupplier serviceSupplier;



}
