package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.ICategoryBusiness;
import com.asb.backCompanyService.dto.request.CategoryDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Category;
import com.asb.backCompanyService.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class CategoryService implements ICategoryBusiness {

    private final CategoryRepository repository;

    @Override
    @Transactional
    public CategoryDto save(CategoryDto categoryDto) {
        try {

            Category category = new Category();
            category.setCategoryType(categoryDto.getCategoryType());
            category.setDescription(categoryDto.getDescription());
            category.setStatus("ACTIVE");

            Category newCategory = repository.save(category);

            CategoryDto savedCategoryDto = new CategoryDto();
            BeanUtils.copyProperties(newCategory, savedCategoryDto);
            return savedCategoryDto;
        } catch (Exception e) {
            log.error("Error al guardar la categoría", e);
            throw new RuntimeException("Error al guardar la categoría", e);
        }
    }

    @Override
    @Transactional
    public GenericResponse update(long id, CategoryDto categoryDto) {
        GenericResponse response = new GenericResponse();
        try {
            // Buscar la categoría existente
            Optional<Category> optionalCategory = repository.findById(id);
            if (!optionalCategory.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La categoría no existe");
            }

            // Actualizar la categoría
            Category category = optionalCategory.get();
            if (categoryDto.getCategoryType() != null) category.setCategoryType(categoryDto.getCategoryType());
            if (categoryDto.getDescription() != null) category.setDescription(categoryDto.getDescription());
            if (categoryDto.getStatus() != null) category.setStatus(categoryDto.getStatus());

            repository.save(category);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Categoría actualizada correctamente");
        } catch (Exception e) {
            log.error("Error al actualizar la categoría", e);
            throw new RuntimeException("Error al actualizar la categoría", e);
        }
        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        try {
            // Verificar existencia de la categoría
            Optional<Category> categoryOptional = repository.findById(id);
            if (!categoryOptional.isPresent()) {
                throw new RuntimeException("La categoría no fue encontrada por el id " + id);
            }

            // Cambiar estado a "INACTIVE" en lugar de eliminación física
            Category category = categoryOptional.get();
            category.setStatus("INACTIVE");
            repository.save(category);
            return true;
        } catch (Exception e) {
            log.error("Error al eliminar la categoría", e);
            throw new RuntimeException("Error al eliminar la categoría", e);
        }
    }

    @Override
    public CategoryDto get(long id) {
        try {
            Optional<Category> categoryOptional = repository.findById(id);
            if (!categoryOptional.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La categoría no existe");
            }

            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(categoryOptional.get(), categoryDto);
            return categoryDto;
        } catch (Exception e) {
            log.error("Error al obtener la categoría", e);
            throw new RuntimeException("Error al obtener la categoría", e);
        }
    }

    @Override
    public Page<Category> getAll(int page, int size, String orders, String sortBy) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(orders);
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));
            return repository.getActiveCategories(pagingSort);
        } catch (Exception e) {
            log.error("Error al obtener todas las categorías", e);
            throw new RuntimeException("Error al obtener todas las categorías", e);
        }
    }

    @Override
    public Page<Category> searchCustom(Map<String, String> customQuery) {
        try {
            // Establecer valores predeterminados y buscar filtros
            String orders = customQuery.getOrDefault("orders", "ASC");
            String sortBy = customQuery.getOrDefault("sortBy", "category_id");
            int page = Integer.parseInt(customQuery.getOrDefault("page", "0"));
            int size = Integer.parseInt(customQuery.getOrDefault("size", "10"));
            String id = customQuery.containsKey("id") ? "%" + customQuery.get("id") + "%" : null;
            String categoryType = customQuery.containsKey("categoryType") ? "%" + customQuery.get("categoryType") + "%" : null;
            String name = customQuery.containsKey("name") ? "%" + customQuery.get("name") + "%" : null;
            String description = customQuery.containsKey("description") ? "%" + customQuery.get("description") + "%" : null;
            String status = customQuery.containsKey("status") ? "%" + customQuery.get("status") + "%" : null;

            Pageable pagingSort = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(orders), sortBy));
            return repository.searchCategory(id, categoryType, name, description, status, pagingSort);
        } catch (Exception e) {
            log.error("Error en la búsqueda personalizada de categorías", e);
            throw new RuntimeException("Error en la búsqueda personalizada de categorías", e);
        }
    }

    @Override
    public List<Category> getAllCategories(Map<String, String> customQuery) {
        try {
            String description = customQuery.getOrDefault("description", "");
            String categoryType = customQuery.get("categoryType");
            Pageable pagingSort = PageRequest.of(
                    Integer.parseInt(customQuery.getOrDefault("offset", "0")),
                    Integer.parseInt(customQuery.getOrDefault("limit", "10"))
            );

            Page<Category> entityPage = repository.findByCategoryTypeAndDescriptionContainingIgnoreCase(categoryType, description, pagingSort);

            return entityPage.getContent();
        } catch (Exception e) {
            log.error("Error al obtener categorías con filtros", e);
            throw new RuntimeException("Error al obtener categorías con filtros", e);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener todas las categorías", e);
            throw new RuntimeException("No se pueden recuperar las categorías", e);
        }
    }
}
