package com.fu.weddingplatform.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "comment")
public class Comment {
  @Id
  @GeneratedValue(generator = "comment-id")
  @GenericGenerator(name = "comment-id", strategy = "com.fu.weddingplatform.custom.customGenerateId.CommentIdGenerator")
  private String id;
  private String createdAt;
  @Column(columnDefinition = "text")
  private String content;
  private String status;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "blog_post_id")
  @EqualsAndHashCode.Include
  @ToString.Include
  private BlogPost blogPost;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "couple_id")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Couple couple;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "parent_id")
  @EqualsAndHashCode.Include
  @ToString.Include
  private Comment parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  @EqualsAndHashCode.Include
  @ToString.Include
  @JsonIgnore
  private Collection<Comment> replyComments;

}
