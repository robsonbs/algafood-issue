package br.dev.robsonbs.livraria.domain.service;

import br.dev.robsonbs.livraria.domain.exception.EntityInUseException;
import br.dev.robsonbs.livraria.domain.models.Category;
import br.dev.robsonbs.livraria.domain.repositories.CategoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class RegistryCategoryService {
  
  private final CategoryRepository categoryRepository;
  
  public RegistryCategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }
  
  public Category save(Category category) {
    return categoryRepository.save(category);
  }
  
  public void remove(Long categoryId) {
    try {
      categoryRepository.deleteById(categoryId);
    } catch (EmptyResultDataAccessException e) {
      throw new EntityNotFoundException(
          String.format("Não existe um cadastro de categoria com código %d", categoryId));
    } catch (DataIntegrityViolationException e) {
      throw new EntityInUseException(
          String.format("Categoria de código %d não pode ser removida, pois está em uso",
                        categoryId));
    }
  }
}
