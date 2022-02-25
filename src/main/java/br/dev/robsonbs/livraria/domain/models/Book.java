package br.dev.robsonbs.livraria.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "books")
public class Book {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;
  private String title;
  private BigDecimal value;
  private String author;
  @Column(name = "isbn")
  private String isbn;
  private String resume;
  private String image;
  private Integer year;
  @ManyToOne
  @JsonIgnoreProperties("books")
  private Category category;
}
