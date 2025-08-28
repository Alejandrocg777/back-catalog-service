package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.ICategoryBusiness;
import com.asb.backCompanyService.dto.request.CategoryDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Category;
import com.asb.backCompanyService.repository.CategoryRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class CategoryService implements ICategoryBusiness {

    private final CategoryRepository repository;
    private final Cloudinary cloudinary;

    @Override
    @Transactional
    public CategoryDto save(CategoryDto categoryDto, MultipartFile image) {
        try {

            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            String imageUrl = (String) uploadResult.get("url");

            Category category = new Category();
            category.setImage(imageUrl);
            category.setNameCategory(categoryDto.getNameCategory());
            category.setSoldOutValue(categoryDto.getSoldOutValue());
            category.setFewUnits(categoryDto.getFewUnits());
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
            Optional<Category> optionalCategory = repository.findById(id);
            if (!optionalCategory.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La categoría no existe");
            }

            Category category = optionalCategory.get();
            if (categoryDto.getNameCategory() != null) category.setNameCategory(categoryDto.getNameCategory());
            if (categoryDto.getSoldOutValue() != null) category.setSoldOutValue(categoryDto.getSoldOutValue());
            if (categoryDto.getFewUnits() != null) category.setFewUnits(categoryDto.getFewUnits());
            if (categoryDto.getStatus() != null) category.setStatus(categoryDto.getStatus());

            Map uploadResult = cloudinary.uploader().upload(categoryDto.getImage().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            String imageUrl = (String) uploadResult.get("url");
            if (imageUrl != null) category.setImage(imageUrl);

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
            Optional<Category> categoryOptional = repository.findById(id);
            if (!categoryOptional.isPresent()) {
                throw new RuntimeException("La categoría no fue encontrada por el id " + id);
            }

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
    public Page<CategoryDto> getAll(int page, int size, String orders, String sortBy) {
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
    public Page<CategoryDto> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String nameCategory = null;
        String soldOutValue = null;
        String fewUnits = null;

        if (customQuery.containsKey("orders")) {
            orders = customQuery.get("orders");
        }

        if (customQuery.containsKey("sortBy")) {
            sortBy = customQuery.get("sortBy");
        }

        if (customQuery.containsKey("page")) {
            page = Integer.parseInt(customQuery.get("page"));
        }

        if (customQuery.containsKey("size")) {
            size = Integer.parseInt(customQuery.get("size"));
        }

        if (customQuery.containsKey("id")) {
            id = "%" + customQuery.get("id") + "%";
        }

        if (customQuery.containsKey("nameCategory")) {
            nameCategory = "%" + customQuery.get("nameCategory") + "%";
        }

        if (customQuery.containsKey("soldOutValue")) {
            soldOutValue = "%" + customQuery.get("soldOutValue") + "%";
        }

        if (customQuery.containsKey("fewUnits")) {
            fewUnits = "%" + customQuery.get("fewUnits") + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        log.info("id: {}", id);
        log.info("nameCategory: {}", nameCategory);
        log.info("soldOutValue: {}", soldOutValue);
        log.info("fewUnits: {}", fewUnits);
        log.info("page: {}", page);
        log.info("size: {}", size);
        log.info("orders: {}", orders);
        log.info("sortBy: {}", sortBy);

        Page<CategoryDto> result = repository.search(id, nameCategory, soldOutValue, fewUnits, pagingSort);
        log.info("Resultados encontrados: {}", result.getContent());
        return result;
    }



    @Override
    public List<Category> getAllCategories() {
        try {
            return repository.findByStatus("ACTIVE");
        } catch (Exception e) {
            log.error("Error al obtener categorías activas", e);
            throw new RuntimeException("No se pueden recuperar las categorías activas", e);
        }
    }

}
