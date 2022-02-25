package br.dev.robsonbs.livraria.controllers;

import br.dev.robsonbs.livraria.domain.exception.EntityInUseException;
import br.dev.robsonbs.livraria.domain.exception.EntityNotFoundException;
import br.dev.robsonbs.livraria.domain.models.Category;
import br.dev.robsonbs.livraria.domain.service.RegistryCategoryService;
import br.dev.robsonbs.livraria.domain.repositories.CategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
  
  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private RegistryCategoryService registryCategory;
  
  @GetMapping()
  public List<Category> list() {
    return categoryRepository.findAll();
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<Category> show(@PathVariable("id") Long categoryId) {
    try {
      Category category = categoryRepository.findById(categoryId)
          .orElseThrow(() -> new EntityNotFoundException(
              String.format("Não existe um cadastro de categoria com o código %d", categoryId)));
      return ResponseEntity.ok(category);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }
  
  @PostMapping
  public ResponseEntity<Category> create(@RequestBody Category category) {
    return ResponseEntity.ok(registryCategory.save(category));
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<Category> update(@RequestBody Category category,
                                         @PathVariable("id") Long categoryId) {
    Optional<Category> actualCategory = categoryRepository.findById(categoryId);
    
    if (actualCategory.isPresent()) {
      BeanUtils.copyProperties(category, actualCategory.get(), "id");
      
      Category savedCategory = registryCategory.save(actualCategory.get());
      return ResponseEntity.ok(savedCategory);
    }
    return ResponseEntity.notFound().build();
  }
  
  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Object> remove(@PathVariable("categoryId") Long id) {
    try {
      registryCategory.remove(id);
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (EntityInUseException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
  }
}
