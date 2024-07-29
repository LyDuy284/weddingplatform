package com.fu.weddingplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(generator = "booking-id")
    @GenericGenerator(name = "booking-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.BookingIdGenerate")
    private String id;
    private Date bookingDate;
    private Date completedDate;
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
    @JoinColumn(name = "quotation_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Quotation quotation;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<BookingDetail> bookingDetails;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Payment> payments;
}
