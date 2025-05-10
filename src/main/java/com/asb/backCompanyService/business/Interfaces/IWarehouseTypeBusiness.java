package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.WarehouseTypeDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.WarehouseType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IWarehouseTypeBusiness {
    WarehouseTypeDto save(WarehouseTypeDto warehouseTypeDto);

    GenericResponse update(Long id, WarehouseTypeDto warehouseTypeDto);

    boolean delete(Long id);

    WarehouseTypeDto get(Long id);

    Page<WarehouseType> getAll(int page, int size, String orders, String sortBy);

    Page<WarehouseType> searchWarehouseTypes(Map<String, String> customQuery);

    List<WarehouseType> getAllWarehouseTypes();
}
