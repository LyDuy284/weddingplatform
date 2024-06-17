package com.fu.weddingplatform.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "blog_post")
public class BlogPost {

    @Id
    @GeneratedValue(generator = "blog-id")
    @GenericGenerator(name = "blog-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.BlogPostIdGenerator")
    private String id;
    private String title;
    private String content;
    private String dateCreated;
    private String status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "service_supplier_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private ServiceSupplier serviceSupplier;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "staff_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Staff staff;
}
