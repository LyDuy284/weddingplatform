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
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(generator = "staff-id")
    @GenericGenerator(name = "staff-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.StaffIdGenerate")
    private String id;
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "accountId")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Account account;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<BlogPost> blogPosts;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Feedback> feedbacks;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<ComboService> comboServices;
}
