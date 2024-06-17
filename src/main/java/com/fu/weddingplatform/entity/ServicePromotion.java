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
@Table(name = "service_promotion")
public class ServicePromotion {

    @Id
    @GeneratedValue(generator = "service-promotion-id")
    @GenericGenerator(name = "service-promotion-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.ServicePromotionIdGenerate")
    private String id;
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "service_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Service service;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "promotion_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Promotion promotion;


}
