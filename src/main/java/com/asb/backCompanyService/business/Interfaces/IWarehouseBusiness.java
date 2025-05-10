package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.WarehouseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Warehouse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IWarehouseBusiness {
    WarehouseDto save(WarehouseDto warehouseDto);

    GenericResponse update(Long id, WarehouseDto warehouseDto);

    boolean delete(Long id);

    WarehouseDto get(Long id);

    Page<Warehouse> getAll(int page, int size, String orders, String sortBy);

    Page<Warehouse> searchWarehouses(Map<String, String> customQuery);

    List<Warehouse> getAllWarehouses();
}
