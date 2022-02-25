package br.dev.robsonbs.livraria.domain.repositories;

import br.dev.robsonbs.livraria.domain.models.Book;

import java.util.List;

public interface BookRepositoryQueries {
  
  List<Book> findBookByYears(Integer startYear, Integer finalYear);
  List<Book> findBooksByAuthorAndYear(String author,
                                      Integer startYear, Integer finalYear);
}
