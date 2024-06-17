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
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(generator = "category-id")
    @GenericGenerator(name = "category-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.CategoryIdGenerate")
    private String id;
    private String categoryName;
    private String status;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Include
    @ToString.Include
    @JsonIgnore
    private Collection<Service> services;
}
