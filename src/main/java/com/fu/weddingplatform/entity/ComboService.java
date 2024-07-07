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
@Table(name = "combo_service")
public class ComboService {

    @Id
    @GeneratedValue(generator = "combo-service-id")
    @GenericGenerator(name = "combo-service-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.ComboServiceIdGenerate")
    private String id;
    @Column(columnDefinition = "text")
    private String description;
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
    @JoinColumn(name = "staff_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Staff staff;

}
