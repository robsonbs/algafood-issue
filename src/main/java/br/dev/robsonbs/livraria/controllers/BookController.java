package br.dev.robsonbs.livraria.controllers;

import br.dev.robsonbs.livraria.domain.exception.EntityInUseException;
import br.dev.robsonbs.livraria.domain.exception.EntityNotFoundException;
import br.dev.robsonbs.livraria.domain.models.Book;
import br.dev.robsonbs.livraria.domain.repositories.BookRepository;
import br.dev.robsonbs.livraria.domain.service.RegistryBookService;
import br.dev.robsonbs.livraria.infrastructure.repositories.spec.BookByAuthorSpec;
import br.dev.robsonbs.livraria.infrastructure.repositories.spec.BookFromYearSpec;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static br.dev.robsonbs.livraria.infrastructure.repositories.spec.BookSpecs.*;

@RestController
@RequestMapping("/livros")
public class BookController {
  
  @Autowired
  private BookRepository bookRepository;
  @Autowired
  private RegistryBookService registryBook;
  
  @GetMapping
  public List<Book> list() {
    
    return bookRepository.findAll();
  }
  
  @GetMapping("/{bookId}")
  public ResponseEntity<Book> show(@PathVariable("bookId") Long id) {
    
    try {
      Book book = bookRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException(
              String.format("Não existe um cadastro de livro com o código %d", id)));
      
      return ResponseEntity.ok(book);
    } catch (EntityNotFoundException e) {
      
      return ResponseEntity.notFound().build();
    }
  }
  
  @PostMapping
  public ResponseEntity<Object> create(@RequestBody Book book) {
    
    try {
      book = registryBook.save(book);
      
      return ResponseEntity.status(HttpStatus.CREATED).body(book);
    } catch (EntityNotFoundException e) {
      
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }
  }
  
  @PutMapping("/{bookId}")
  public ResponseEntity<Object> update(@PathVariable("bookId") Long id, @RequestBody Book book) {
    
    try {
      Book foundBook = bookRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException(
              String.format("Não existe um cadastro de livro com o código %d", id)));
      
      if (foundBook != null) {
        BeanUtils.copyProperties(book, foundBook, "id");
        return ResponseEntity.ok(registryBook.save(foundBook));
      }
      
      return ResponseEntity.notFound().build();
    } catch (EntityNotFoundException e) {
      
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }
  }
  
  @PatchMapping("/{bookId}")
  public ResponseEntity<Object> partialUpdate(@PathVariable("bookId") Long id,
                                              @RequestBody Map<String, Object> values) {
    
    Book actualBook = bookRepository.findById(id).orElse(null);
    
    if (actualBook == null) {
      
      return ResponseEntity.notFound().build();
    }
    merge(values, actualBook);
    
    return update(id, actualBook);
  }
  
  private void merge(Map<String, Object> valuesSource, Book targetBook) {
    
    ObjectMapper objectMapper = new ObjectMapper();
    Book bookSource = objectMapper.convertValue(valuesSource, Book.class);
    
    valuesSource.forEach((key, value) -> {
      Field field = ReflectionUtils.findField(Book.class, key);
      field.setAccessible(true);
      Object newValue = ReflectionUtils.getField(field, bookSource);
      
      ReflectionUtils.setField(field, targetBook, newValue);
    });
  }
  
  @DeleteMapping("/{bookId}")
  public ResponseEntity<Object> remover(@PathVariable("bookId") Long id) {
    
    try {
      registryBook.delete(id);
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      
      return ResponseEntity.notFound().build();
    } catch (EntityInUseException e) {
      
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }
  
  @GetMapping("/por-ano")
  public List<Book> searchByYear(Integer startYear, Integer finalYear) {
    return bookRepository.findBookByYears(startYear, finalYear);
  }
  
  @GetMapping("/por-autor-neste-ano")
  public List<Book> searchByAuthorFromThisYear(String author) {
    
    Specification<Book> byAuthor = new BookByAuthorSpec(author);
    Specification<Book> fromThisYear = new BookFromYearSpec();
    
    return bookRepository.findAll(byAuthor.and(fromThisYear));
  }
  
  @GetMapping("/por-autor-neste-ano-v2")
  public List<Book> searchByAuthorFromThisYearV2(String author) {
    
    return bookRepository.findAll(byAuthor(author).and(fromThisYear()));
  }
  
  @GetMapping("/por-autor-neste-ano-baratos")
  public List<Book> searchByAuthorFromThisYearAndCheap(String author) {
    
    return bookRepository.findAll(byAuthor(author).and(fromThisYear()).and(lessThan30BRL()));
  }
}
