package com.fu.weddingplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="service_supplier")
public class ServiceSupplier {
    @Id
    @GeneratedValue(generator = "service-supplier-id")
    @GenericGenerator(name = "service-supplier-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.ServiceSupplierIdGenerator")
    private String id;

    private String supplierName;
    private String supplierAddress;
    private String contactPersonName;
    private String contactPhone;
    private String contactEmail;
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "accountId")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Account account;

    @OneToMany(mappedBy = "serviceSupplier", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<BlogPost> blogPosts;

    @OneToMany(mappedBy = "serviceSupplier", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Feedback> feedbacks;
}
