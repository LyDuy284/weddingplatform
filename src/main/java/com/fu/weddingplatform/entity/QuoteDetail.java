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
@Table(name = "quote_detail")
public class QuoteDetail {

    @Id
    @GeneratedValue(generator = "quote-detail-id")
    @GenericGenerator(name = "quote-detail-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.QuoteDetailIdGenerate")
    private String id;
    private float total;
    private float deposit;
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "quotation_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Quotation quotation;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "promotion_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Promotion promotion;

}
