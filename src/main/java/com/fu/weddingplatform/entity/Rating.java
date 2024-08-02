package com.fu.weddingplatform.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "rating")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(generator = "rating-id")
    @GenericGenerator(name = "rating-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.RatingIdGenerate")
    private String id;
    private int ratingQuantityValue;
    private int ratingTimeValue;
    private int ratingQualityValue;
    private String dateCreated;
    @Column(columnDefinition = "text")
    private String description;
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "coupleId")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Couple couple;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "service_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Services service;

}
