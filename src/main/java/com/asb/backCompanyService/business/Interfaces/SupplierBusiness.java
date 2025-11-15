package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.PurchaseSupplierRequestDTO;
import com.asb.backCompanyService.dto.request.SupplierCreateDTO;
import com.asb.backCompanyService.dto.request.SupplierProductDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.PurchaseSupplierResponseDTO;
import com.asb.backCompanyService.dto.responde.SupplierDtoResponse;
import com.asb.backCompanyService.dto.responde.SupplierProductDtoResponse;
import com.asb.backCompanyService.model.Supplier;
import com.asb.backCompanyService.model.SupplierProduct;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SupplierBusiness {

    Supplier createSupplier(SupplierCreateDTO requestDTO);

    SupplierProduct addProductToSupplier(Long supplierId, SupplierProductDTO addDTO);

    Page<SupplierDtoResponse> getAll( int page, int size, String orders, String sortBy);

    Page<SupplierDtoResponse> searchCustom(Map<String, String> customQuery);

    Page<SupplierProductDtoResponse> getAllProductsBySupplier(Long supplierId, int page, int size, String orders, String sortBy);

    Page<SupplierProductDtoResponse> searchProductsBySupplier(Long supplierId, Map<String, String> customQuery);

    GenericResponse deleteSupplierLogical(Long supplierId);

    GenericResponse deleteSupplierProductLogical(Long supplierProductId);

     Supplier updateSupplier(Long supplierId, SupplierCreateDTO updateDTO);

    List<Supplier> getAllNoPage();

    BigDecimal getPurchasePrice(Long supplierId, Long productId);

    GenericResponse createPurchase(PurchaseSupplierRequestDTO purchase);

    Page<PurchaseSupplierResponseDTO> getAllPurshaseSupplier(int page, int size, String orders, String sortBy);

    Page<PurchaseSupplierResponseDTO> searchPPurchaseSupplier(Map<String, String> customQuery);

}
