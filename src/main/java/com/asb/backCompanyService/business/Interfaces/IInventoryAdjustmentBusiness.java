package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.InventoryAdjustmentDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.InventoryAdjustment;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IInventoryAdjustmentBusiness {

    InventoryAdjustmentDto save(InventoryAdjustmentDto inventoryAdjustmentDto);

    GenericResponse update(long id, InventoryAdjustmentDto inventoryAdjustmentDto);

    boolean delete(Long id);

    InventoryAdjustmentDto get(long id);

    Page<InventoryAdjustment> getAll(int page, int size, String orders, String sortBy);

    Page<InventoryAdjustment> searchCustom(Map<String, String> customQuery);

    List<InventoryAdjustment> getAllInventoryAdjustments(Map<String, String> customQuery);

    List<InventoryAdjustment> getAllInventoryAdjustments();
}
