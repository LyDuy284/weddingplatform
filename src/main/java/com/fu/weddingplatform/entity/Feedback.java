package com.fu.weddingplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "feedback")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(generator = "feedback-id")
    @GenericGenerator(name = "feedback-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.FeedbackIdGenerate")
    private String id;

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
    @JoinColumn(name = "staffId")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Staff staff;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "serviceSupplierId")
    @EqualsAndHashCode.Include
    @ToString.Include
    private ServiceSupplier serviceSupplier;
}
