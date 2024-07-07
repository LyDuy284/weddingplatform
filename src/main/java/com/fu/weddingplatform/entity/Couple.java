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
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Couple {
    @Id
    @GeneratedValue(generator = "couple-id")
    @GenericGenerator(name = "couple-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.CoupleIdGenerator")
    private String id;

    private String partnerName1;
    private String partnerName2;
    private String status;
    private Date weddingDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "accountId")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Account account;

    @OneToMany(mappedBy = "couple", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Feedback> feedbacks;

    @OneToMany(mappedBy = "couple", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Booking> bookings;

    @OneToMany(mappedBy = "couple", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Quotation> quotations;

    @OneToMany(mappedBy = "couple", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Rating> ratings;

    @OneToMany(mappedBy = "couple", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Payment> payments;

    @OneToMany(mappedBy = "couple", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Comment> comments;

}
