package br.dev.robsonbs.livraria.infrastructure.repositories.spec;

import br.dev.robsonbs.livraria.domain.models.Book;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class BookSpecs {
  private BookSpecs(){}
  
  public static Specification<Book> byAuthor(String author) {
    return new BookByAuthorSpec(author);
  }
  
  public static Specification<Book> fromThisYear() {
    return new BookFromYearSpec();
  }
  
  public static Specification<Book> lessThan30BRL() {
    return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("value"),
                                                               BigDecimal.valueOf(30));
  }
}
