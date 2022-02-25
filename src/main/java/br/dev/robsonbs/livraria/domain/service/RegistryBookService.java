package br.dev.robsonbs.livraria.domain.service;

import br.dev.robsonbs.livraria.domain.exception.EntityInUseException;
import br.dev.robsonbs.livraria.domain.exception.EntityNotFoundException;
import br.dev.robsonbs.livraria.domain.models.Book;
import br.dev.robsonbs.livraria.domain.models.Category;
import br.dev.robsonbs.livraria.domain.repositories.BookRepository;
import br.dev.robsonbs.livraria.domain.repositories.CategoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class RegistryBookService {
  
  private final BookRepository bookRepository;
  private final CategoryRepository categoryRepository;
  
  public RegistryBookService(BookRepository bookRepository, CategoryRepository categoryRepository) {
    this.bookRepository = bookRepository;
    this.categoryRepository = categoryRepository;
  }
  
  public Book save(Book book) {
    Long categoryId = book.getCategory().getId();
    Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new EntityNotFoundException(
            String.format("Não existe cadastro de categoria com código %d", categoryId)));
    book.setCategory(category);
    
    return bookRepository.save(book);
  }
  
  public void delete(Long bookId) {
    try {
      bookRepository.deleteById(bookId);
    } catch (EmptyResultDataAccessException e) {
      throw new EntityNotFoundException(
          String.format("Não existe um cadastro de livro com o código %d", bookId));
    } catch (DataIntegrityViolationException exception) {
      throw new EntityInUseException(
          String.format("Livro de código %d não pode ser removida, pois está em uso", bookId));
    }
  }
}
