package br.dev.robsonbs.livraria.infrastructure.repositories.spec;

import br.dev.robsonbs.livraria.domain.models.Book;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BookFromYearSpec implements Specification<Book> {
  
  @Override
  public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query,
                               CriteriaBuilder builder) {
    return builder.equal(root.get("year"),2022);
  }
}
