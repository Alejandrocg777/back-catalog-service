package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.ICategoryBusiness;
import com.asb.backCompanyService.dto.request.CategoryDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.ListGenericResponse;
import com.asb.backCompanyService.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/category")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class CategoryController {

    private final ICategoryBusiness iCategoryBusiness;

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> save(@RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategory = iCategoryBusiness.save(categoryDto);
        return ResponseEntity.ok(savedCategory);
    }


    @GetMapping
    public ResponseEntity<Page<CategoryDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "6") int size,
                                                 @RequestParam(defaultValue = "ASC") String orders,
                                                 @RequestParam(defaultValue = "id") String sortBy) {
        Page<CategoryDto> categories = iCategoryBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CategoryDto>> search(@RequestParam Map<String, String> customQuery) {
        Page<CategoryDto> categories = iCategoryBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long categoryId,
                                                     @RequestBody CategoryDto categoryDto) {
        log.info("Iniciando actualización para Category con ID: {} y DTO: {}", categoryId, categoryDto);
        GenericResponse response = iCategoryBusiness.update(categoryId, categoryDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        iCategoryBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/no-page/getAllCategories")
    public ResponseEntity<List<Category>> getAllCategories() {
        log.info("Iniciando endpoint para obtener todas las categorías");
        List<Category> categories = iCategoryBusiness.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
