package br.dev.robsonbs.livraria.infrastructure.repositories;

import br.dev.robsonbs.livraria.domain.models.Book;
import br.dev.robsonbs.livraria.domain.repositories.BookRepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepositoryQueries {
  
  @PersistenceContext
  private EntityManager manager;
  
  @Override
  public List<Book> findBookByYears(Integer startYear, Integer finalYear) {
    
    StringBuilder jpql = new StringBuilder();
    jpql.append("from Book where 0 = 0 ");
    HashMap<String, Object> params = new HashMap<>();
    
    if (startYear != null) {
      jpql.append("and year >=:startYear ");
      params.put("startYear", startYear);
    }
    
    if (finalYear != null) {
      jpql.append("and year <=:finalYear ");
      params.put("finalYear", finalYear);
    }
    
    TypedQuery<Book> query = manager.createQuery(jpql.toString(), Book.class);
    
    params.forEach(query::setParameter);
    
    return query.getResultList();
  }
  
  @Override
  public List<Book> findBooksByAuthorAndYear(String author, Integer startYear, Integer finalYear) {
    CriteriaBuilder builder = manager.getCriteriaBuilder();
    CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
    Root<Book> root = criteria.from(Book.class);
    
    List<Predicate> predicates = new ArrayList<>(1);
    
    if (StringUtils.hasText(author)) {
      predicates.add(builder.like(root.get("author"), "%" + author + "%"));
    }
    if (startYear != null) {
      predicates.add(builder.greaterThanOrEqualTo(root.get("year"), startYear));
    }
    if (finalYear != null) {
      predicates.add(builder.lessThanOrEqualTo(root.get("year"), finalYear));
    }
    
    criteria.where(predicates.toArray(new Predicate[0]));
    
    return manager.createQuery(criteria).getResultList();
  }
}
