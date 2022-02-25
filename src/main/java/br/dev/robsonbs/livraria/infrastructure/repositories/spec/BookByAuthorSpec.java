package br.dev.robsonbs.livraria.infrastructure.repositories.spec;

import br.dev.robsonbs.livraria.domain.models.Book;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class BookByAuthorSpec implements Specification<Book> {
  
  private String author;
  
  @Override
  public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    return builder.like(root.get("author"), "%" + author + "%");
  }
}
