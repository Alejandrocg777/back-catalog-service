package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.CategoryDto;
import com.asb.backCompanyService.dto.responde.CategoryResponseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ICategoryBusiness {
    CategoryDto save(CategoryDto categoryDto);

    GenericResponse update(long id, CategoryDto categoryDto);

    boolean delete(Long id);

    CategoryDto get(long id);

    Page<CategoryResponseDto> getAll(int page, int size, String orders, String sortBy);

    Page<CategoryResponseDto> searchCustom(Map<String, String> customQuery);


    List<Category> getAllCategories();
}
