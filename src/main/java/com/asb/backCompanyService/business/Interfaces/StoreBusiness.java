package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.StoreDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Store;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface StoreBusiness {
    StoreDto save(StoreDto requestDto);

    StoreDto get(long id);

    Page<Store> getAll(int page, int size, String orders, String sortBy);

    Page<Store> search(Map<String, String> customQuery);

    GenericResponse update(long id, StoreDto requestDto);

    GenericResponse delete(long id);

    List<Store> getAllWithoutPagination();

}
