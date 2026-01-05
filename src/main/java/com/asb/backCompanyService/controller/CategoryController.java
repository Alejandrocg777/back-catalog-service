package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.ICategoryBusiness;
import com.asb.backCompanyService.dto.request.CategoryDto;
import com.asb.backCompanyService.dto.responde.CategoryResponseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Category;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/category")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class CategoryController {

    private final ICategoryBusiness iCategoryBusiness;
    private final Cloudinary cloudinary;

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> save(
            @RequestParam("nameCategory") String nameCategory,
            @RequestParam("soldOutValue") Double soldOutValue,
            @RequestParam("fewUnits") Double fewUnits,
            @RequestPart("image") MultipartFile image) {

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setNameCategory(nameCategory);
        categoryDto.setSoldOutValue(soldOutValue);
        categoryDto.setFewUnits(fewUnits);

        if(image != null) {
            try {
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                String imageUrl = (String) uploadResult.get("url");
                categoryDto.setImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen", e);
            }
        }else{
            categoryDto.setImage("");
        }
        CategoryDto savedCategory = iCategoryBusiness.save(categoryDto);
        return ResponseEntity.ok(savedCategory);
    }


    @GetMapping
    public ResponseEntity<Page<CategoryResponseDto>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "6") Integer size,
                                                            @RequestParam(defaultValue = "ASC") String orders,
                                                            @RequestParam(defaultValue = "id") String sortBy) {
        Page<CategoryResponseDto> categories = iCategoryBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CategoryResponseDto>> search(@RequestParam Map<String, String> customQuery) {
        Page<CategoryResponseDto> categories = iCategoryBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long categoryId,
                                                  @RequestParam("nameCategory") String nameCategory,
                                                  @RequestParam("soldOutValue") Double soldOutValue,
                                                  @RequestParam("fewUnits") Double fewUnits,
                                                  @RequestParam(required = false)  MultipartFile image) {
        log.info("Iniciando actualización para Category con ID: {}", categoryId);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setNameCategory(nameCategory);
        categoryDto.setSoldOutValue(soldOutValue);
        categoryDto.setFewUnits(fewUnits);

        if(image != null) {
            try {
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                String imageUrl = (String) uploadResult.get("url");
                categoryDto.setImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen", e);
            }
        }
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
