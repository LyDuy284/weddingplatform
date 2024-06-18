package com.fu.weddingplatform.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "service_combo")
public class ServiceCombo {

    @Id
    @GeneratedValue(generator = "service-combo-id")
    @GenericGenerator(name = "service-combo-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.ServiceComboIdGenerate")
    private String id;
    private String status;

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
    @JoinColumn(name = "package_combo_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private PackageCombo packageCombo;
}
