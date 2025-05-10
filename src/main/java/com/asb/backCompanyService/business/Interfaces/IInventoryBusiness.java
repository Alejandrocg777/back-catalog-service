package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.InventoryDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Inventory;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IInventoryBusiness {

    InventoryDto save(InventoryDto inventoryDto);

    GenericResponse update(long id, InventoryDto user);

    boolean delete(Long id);

    InventoryDto get(long id);

    Page<Inventory> getAll(int page, int size, String orders, String sortBy);

    Page<Inventory> searchCustom(Map<String, String> customQuery);

    List<Inventory> getAllInventories();
}
