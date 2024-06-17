package com.fu.weddingplatform.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

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
    private Collection<ServicePromotion> servicePromotions;
}
