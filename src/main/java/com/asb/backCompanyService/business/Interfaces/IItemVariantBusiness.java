package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.ItemVariantDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.ItemVariant;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IItemVariantBusiness {
    ItemVariantDto save(ItemVariantDto itemVariantDto);

    GenericResponse update(long id, ItemVariantDto itemVariantDto);

    boolean delete(Long id);

    ItemVariantDto get(long id);

    Page<ItemVariant> getAll(int page, int size, String orders, String sortBy);

    Page<ItemVariant> searchCustom(Map<String, String> customQuery);

    List<ItemVariant> getAllItemVariants(Map<String, String> customQuery);

    List<ItemVariant> getAllItemVariants();
}
