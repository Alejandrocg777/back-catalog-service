package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.SupplierBusiness;
import com.asb.backCompanyService.dto.request.SupplierCreateDTO;
import com.asb.backCompanyService.dto.request.SupplierProductDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.SupplierDtoResponse;
import com.asb.backCompanyService.dto.responde.SupplierProductDtoResponse;
import com.asb.backCompanyService.exception.GenericException;
import com.asb.backCompanyService.model.Supplier;
import com.asb.backCompanyService.model.SupplierProduct;
import com.asb.backCompanyService.repository.SupplierProductRepository;
import com.asb.backCompanyService.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class SupplierService implements SupplierBusiness {

    private final SupplierRepository supplierRepository;
    private final SupplierProductRepository supplierProductRepository;


    @Override
    public Supplier createSupplier(SupplierCreateDTO createDTO) {

        Supplier supplier = new Supplier();
        supplier.setName(createDTO.getName());
        supplier.setEmail(createDTO.getEmail());
        supplier.setPhone(createDTO.getPhone());
        supplier.setCategoryId(createDTO.getCategoryId());
        supplier.setWarehouseId(createDTO.getWarehouseId());
        supplier.setStatus("ACTIVE");
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());

        return supplierRepository.save(supplier);
    }

    @Override
    public SupplierProduct addProductToSupplier(Long supplierId, SupplierProductDTO addDTO) {

        if (!supplierRepository.existsById(supplierId)) {
            throw new GenericException("Supplier con ID " + addDTO.getProductId() + " no existe", HttpStatus.BAD_REQUEST);
        }

        // Validar que el productId exista (opcional)
        if (!supplierProductRepository.existsById(addDTO.getProductId())) {
            throw new GenericException("Producto con ID " + addDTO.getProductId() + " no existe", HttpStatus.BAD_REQUEST);
        }

        SupplierProduct detail = new SupplierProduct();
        detail.setSupplierId(supplierId);
        detail.setProductId(addDTO.getProductId());
        detail.setPurchasePrice(addDTO.getPurchasePrice() != null ? addDTO.getPurchasePrice() : BigDecimal.ZERO);
        detail.setStatus("ACTIVE");
        detail.setCreatedAt(LocalDateTime.now());
        detail.setUpdatedAt(LocalDateTime.now());

        return supplierProductRepository.save(detail);
    }

    @Override
    public Page<SupplierDtoResponse> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return supplierRepository.getActiveSuppliers(pagingSort);
    }


    @Override
    public Page<SupplierDtoResponse> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 10;
        String supplierId = null;
        String name = null;
        String email = null;
        String phone = null;
        String categoryId = null;
        String warehouseName = null;
        String status = null;

        if (customQuery.containsKey("orders")) {
            orders = customQuery.get("orders");
        }

        if (customQuery.containsKey("sortBy")) {
            sortBy = customQuery.get("sortBy");
        }

        if (customQuery.containsKey("page")) {
            page = Integer.parseInt(customQuery.get("page"));
        }

        if (customQuery.containsKey("size")) {
            size = Integer.parseInt(customQuery.get("size"));
        }

        if (customQuery.containsKey("supplierId")) {
            supplierId = "%" + customQuery.get("supplierId") + "%";
        }

        if (customQuery.containsKey("name")) {
            name = "%" + customQuery.get("name").toUpperCase() + "%";
        }

        if (customQuery.containsKey("email")) {
            email = "%" + customQuery.get("email").toUpperCase() + "%";
        }

        if (customQuery.containsKey("phone")) {
            phone = "%" + customQuery.get("phone") + "%";
        }

        if (customQuery.containsKey("categoryId")) {
            categoryId = "%" + customQuery.get("categoryId") + "%";
        }

        if (customQuery.containsKey("warehouseName")) {
            warehouseName = "%" + customQuery.get("warehouseName").toUpperCase() + "%";
        }

        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status").toUpperCase() + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);

        log.info("supplierId: " + supplierId);
        log.info("name: " + name);
        log.info("email: " + email);
        log.info("phone: " + phone);
        log.info("categoryId: " + categoryId);
        log.info("warehouseName: " + warehouseName);
        log.info("status: " + status);
        log.info("Page: " + page);
        log.info("Size: " + size);
        log.info("Orders: " + orders);
        log.info("SortBy: " + sortBy);

        Page<SupplierDtoResponse> searchResult = supplierRepository.searchSuppliers(supplierId, name, email, phone, categoryId, warehouseName, status, pagingSort);
        log.info("Search results: " + searchResult.getContent());
        return searchResult;
    }

    @Override
    public Page<SupplierProductDtoResponse> getAllProductsBySupplier(Long supplierId, int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return supplierProductRepository.getActiveProductsBySupplier(supplierId, pagingSort);
    }

    @Override
    public Page<SupplierProductDtoResponse> searchProductsBySupplier(Long supplierId, Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 10;
        String supplierProductId = null;
        String productId = null;
        String productName = null;
        String purchasePrice = null;
        String status = null;

        if (customQuery.containsKey("orders")) {
            orders = customQuery.get("orders");
        }

        if (customQuery.containsKey("sortBy")) {
            sortBy = customQuery.get("sortBy");
        }

        if (customQuery.containsKey("page")) {
            page = Integer.parseInt(customQuery.get("page"));
        }

        if (customQuery.containsKey("size")) {
            size = Integer.parseInt(customQuery.get("size"));
        }

        if (customQuery.containsKey("supplierProductId")) {
            supplierProductId = "%" + customQuery.get("supplierProductId") + "%";
        }

        if (customQuery.containsKey("productId")) {
            productId = "%" + customQuery.get("productId") + "%";
        }

        if (customQuery.containsKey("productName")) {
            productName = "%" + customQuery.get("productName").toUpperCase() + "%";
        }

        if (customQuery.containsKey("purchasePrice")) {
            purchasePrice = "%" + customQuery.get("purchasePrice") + "%";
        }

        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status").toUpperCase() + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);

        log.info("supplierProductId: " + supplierProductId);
        log.info("productId: " + productId);
        log.info("productName: " + productName);
        log.info("purchasePrice: " + purchasePrice);
        log.info("status: " + status);
        log.info("Page: " + page);
        log.info("Size: " + size);
        log.info("Orders: " + orders);
        log.info("SortBy: " + sortBy);

        Page<SupplierProductDtoResponse> searchResult = supplierProductRepository.searchSupplierProducts(supplierId, supplierProductId, productId, productName, purchasePrice, status, pagingSort);
        log.info("Search results: " + searchResult.getContent());
        return searchResult;
    }

    @Override
    public GenericResponse deleteSupplierLogical(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Proveedor con ID " + supplierId + " no existe"));

        supplier.setStatus("INACTIVE");
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);

        List<SupplierProduct> details = supplierProductRepository.findBySupplierId(supplierId);
        for (SupplierProduct detail : details) {
            detail.setStatus("INACTIVE");
            detail.setUpdatedAt(LocalDateTime.now());
            supplierProductRepository.save(detail);
        }

        return new GenericResponse("Borrado con exito", 200);
    }

    @Override
    public GenericResponse deleteSupplierProductLogical(Long supplierProductId) {
        SupplierProduct detail = supplierProductRepository.findById(supplierProductId)
                .orElseThrow(() -> new RuntimeException("Detalle con ID " + supplierProductId + " no existe"));

        detail.setStatus("INACTIVE");
        detail.setUpdatedAt(LocalDateTime.now());
        supplierProductRepository.save(detail);

        return new GenericResponse("Borrado con exito", 200);
    }

}
