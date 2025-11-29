package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.SupplierBusiness;
import com.asb.backCompanyService.dto.request.*;
import com.asb.backCompanyService.dto.responde.*;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.exception.GenericException;
import com.asb.backCompanyService.model.*;
import com.asb.backCompanyService.repository.*;
import jakarta.transaction.Transactional;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class SupplierService implements SupplierBusiness {

    private final SupplierRepository supplierRepository;
    private final SupplierProductRepository supplierProductRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final WarehouseRepository warehpuseRepository;
    private final TransactionsService transactionsService;
    private final TransactionProductRepository transactionProductRepository;
    private final PurchaseSupplierRepository purchaseSupplierRepository;
    private final TransactionRepository transactionRepository;
    private final ProductWareHouseRepository productWareHouseRepository;


    @Override
    @Transactional
    public Supplier createSupplier(SupplierCreateDTO createDTO) {

        Supplier supplier = new Supplier();
        supplier.setName(createDTO.getName());
        supplier.setEmail(createDTO.getEmail());
        supplier.setPhone(createDTO.getPhone());
        supplier.setCategoryId(createDTO.getCategoryId());

        if (!warehpuseRepository.existsById(createDTO.getWarehouseId())){
            throw new GenericException("Bodega no existe" , HttpStatus.BAD_REQUEST);
        }
        supplier.setWarehouseId(createDTO.getWarehouseId());
        supplier.setStatus("ACTIVE");
        supplier.setCreatedAt(LocalDateTime.now());
        supplier.setUpdatedAt(LocalDateTime.now());

        Supplier savedSupplier = supplierRepository.save(supplier);

        if (createDTO.getProducts() != null && !createDTO.getProducts().isEmpty()) {
            for (SupplierProductDTO addDTO : createDTO.getProducts()) {
                if (!productRepository.existsById(addDTO.getProductId())) {
                    throw new GenericException("Producto con ID " + addDTO.getProductId() + " no existe", HttpStatus.BAD_REQUEST);
                }

                SupplierProduct detail = new SupplierProduct();
                detail.setSupplierId(savedSupplier.getId());
                detail.setProductId(addDTO.getProductId());
                detail.setPurchasePrice(addDTO.getPurchasePrice() != null ? addDTO.getPurchasePrice() : BigDecimal.ZERO);
                detail.setStatus("ACTIVE");
                detail.setCreatedAt(LocalDateTime.now());
                detail.setUpdatedAt(LocalDateTime.now());

                supplierProductRepository.save(detail);
            }
        }

        return savedSupplier;
    }



    @Override
    public SupplierProduct addProductToSupplier(Long supplierId, SupplierProductDTO addDTO) {

        if (!supplierRepository.existsById(supplierId)) {
            throw new GenericException("Supplier con ID " + addDTO.getProductId() + " no existe", HttpStatus.BAD_REQUEST);
        }

        if (!productRepository.existsById(addDTO.getProductId())) {
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
        SupplierProduct detail = supplierProductRepository.findById(supplierProductId).get();
        detail.setStatus("INACTIVE");
        detail.setUpdatedAt(LocalDateTime.now());
        supplierProductRepository.save(detail);

        return new GenericResponse("Borrado con exito", 200);
    }

    @Override
    @Transactional
    public Supplier updateSupplier(Long supplierId, SupplierCreateDTO updateDTO) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Proveedor con ID " + supplierId + " no existe"));

        // Actualizar solo si el valor no es null
        if (updateDTO.getName() != null) {
            supplier.setName(updateDTO.getName());
        }
        if (updateDTO.getEmail() != null) {
            supplier.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getPhone() != null) {
            supplier.setPhone(updateDTO.getPhone());
        }
        if (updateDTO.getCategoryId() != null) {
            supplier.setCategoryId(updateDTO.getCategoryId());
        }
        if (updateDTO.getWarehouseId() != null) {
            if (!warehpuseRepository.existsById(updateDTO.getWarehouseId())){
                throw new GenericException("Bodega no existe" , HttpStatus.BAD_REQUEST);
            }
            supplier.setWarehouseId(updateDTO.getWarehouseId());
        }
        // Actualizar timestamp
        supplier.setUpdatedAt(LocalDateTime.now());

        Supplier savedSupplier = supplierRepository.save(supplier);

        // Manejar productos si la lista no es null o vacía
        if (updateDTO.getProducts() != null && !updateDTO.getProducts().isEmpty()) {
            for (SupplierProductDTO addDTO : updateDTO.getProducts()) {
                if (!productRepository.existsById(addDTO.getProductId())) {
                    throw new GenericException("Producto con ID " + addDTO.getProductId() + " no existe", HttpStatus.BAD_REQUEST);
                }

                // Verificar si ya existe un SupplierProduct para este supplier y product
                SupplierProduct existingDetail = supplierProductRepository.findBySupplierIdAndProductId(savedSupplier.getId(), addDTO.getProductId());
                if (existingDetail != null) {
                    // Actualizar si existe
                    existingDetail.setPurchasePrice(addDTO.getPurchasePrice() != null ? addDTO.getPurchasePrice() : BigDecimal.ZERO);
                    existingDetail.setUpdatedAt(LocalDateTime.now());
                    supplierProductRepository.save(existingDetail);
                } else {
                    // Agregar nuevo si no existe
                    SupplierProduct newDetail = new SupplierProduct();
                    newDetail.setSupplierId(savedSupplier.getId());
                    newDetail.setProductId(addDTO.getProductId());
                    newDetail.setPurchasePrice(addDTO.getPurchasePrice() != null ? addDTO.getPurchasePrice() : BigDecimal.ZERO);
                    newDetail.setStatus("ACTIVE");
                    newDetail.setCreatedAt(LocalDateTime.now());
                    newDetail.setUpdatedAt(LocalDateTime.now());
                    supplierProductRepository.save(newDetail);
                }
            }
        }

        return savedSupplier;
    }

    @Override
    public List<Supplier> getAllNoPage() {
        return supplierRepository.getAllSupplier();
    }

    @Override
    public BigDecimal getPurchasePrice(Long supplierId, Long productId) {
        BigDecimal purchasePrice = supplierProductRepository.findPurchasePriceBySupplierIdAndProductId(supplierId, productId);

        if (purchasePrice == null) {
            return null;
        }
        return purchasePrice;
    }

    @Override
    @Transactional
    public GenericResponse createPurchase(PurchaseSupplierRequestDTO purchase) {

        Transaction transaction = transactionsService.insertTransaction(
                "ENTRADA PROVEEDOR",
                purchase.getTransactionTotal(),
                purchase.getUserId(),
                purchase.getDate(),
                purchase.getObservation(),
                "ACTIVE"
        );

        for (PurchaseSupplierProductsDTO productDTO : purchase.getProducts()) {
            // Validar producto
            if (!productRepository.existsById(productDTO.getProductId())) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST,
                        "Producto no existe: " + productDTO.getProductId());
            }

            Product product = productRepository.findById(productDTO.getProductId())
                    .orElseThrow(() -> new CustomErrorException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

            // Actualizar cantidad
            Long newQuantity = product.getQuantity() + productDTO.getQuantity();
            product.setQuantity(newQuantity);
            product.setProductStatus(productService.calculateProductStatus(product.getCategoryId(), newQuantity));
            productRepository.save(product);

            // Insertar en transaction_product
            TransactionProduct tp = new TransactionProduct();
            tp.setTransactionId(transaction.getId());
            tp.setProductId(product.getId());
            tp.setPurchasePrice(productDTO.getPurchasePrice());
            tp.setTotal(productDTO.getTotal());
            tp.setQuantity(productDTO.getQuantity());
            transactionProductRepository.save(tp);


            // mandar los productos a la bodega
            addQuantityProductToSupplier(productDTO.getProductId(), purchase.getSupplierId(), productDTO.getQuantity());

        }

        PurchaseSupplier purchaseSupplier = new PurchaseSupplier();
        purchaseSupplier.setSupplierId(purchase.getSupplierId());
        purchaseSupplier.setStatus("ACTIVE");
        purchaseSupplier.setPurchaseStatus(purchase.getPurchaseStatus());
        purchaseSupplier.setTransactionId(transaction.getId());
        purchaseSupplierRepository.save(purchaseSupplier);

        return new GenericResponse("Venta guardad con exito", 200);
    }

    @Override
    public Page<PurchaseSupplierResponseDTO> getAllPurshaseSupplier(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return purchaseSupplierRepository.findAllPurchaseSuppliers(pagingSort);
    }

    @Override
    public Page<PurchaseSupplierResponseDTO> searchPPurchaseSupplier(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";  // Default sort by id
        int page = 0;
        int size = 10;
        Long id = null;
        Long userId = null;
        Long supplierId = null;
        String supplierName = null;
        LocalDateTime date = null;
        String purchaseStatus = null;
        String observation = null;

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

        if (customQuery.containsKey("id")) {
            id = Long.parseLong(customQuery.get("id"));  // Exacto para Long
        }

        if (customQuery.containsKey("userId")) {
            userId = Long.parseLong(customQuery.get("userId"));
        }

        if (customQuery.containsKey("supplierId")) {
            supplierId = Long.parseLong(customQuery.get("supplierId"));
        }

        if (customQuery.containsKey("supplierName")) {
            supplierName = "%" + customQuery.get("supplierName").toUpperCase() + "%";
        }

        if (customQuery.containsKey("date")) {
            // Parsear string a LocalDateTime (asumiendo formato dd/MM/yyyy; ajusta si necesitas HH:mm)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = LocalDate.parse(customQuery.get("date"), formatter).atStartOfDay();  // O ajusta para hora si necesario
        }

        if (customQuery.containsKey("purchaseStatus")) {
            purchaseStatus = "%" + customQuery.get("purchaseStatus").toUpperCase() + "%";
        }

        if (customQuery.containsKey("observation")) {  // Asumiendo "status" en DTO es observation
            observation = "%" + customQuery.get("observation").toUpperCase() + "%";
        }

        // Validar sortBy contra campos válidos para evitar errores (opcional pero recomendado)
        Set<String> validSortFields = Set.of("id", "userId", "supplierId", "supplierName", "date", "purchaseStatus", "observation");
        if (!validSortFields.contains(sortBy)) {
            sortBy = "id";  // Fallback
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);  // Ajusta alias si necesario (e.g., "supplierName" -> "s.name" internamente en JPA)

        Pageable pagingSort = PageRequest.of(page, size, sort);

        // Logs para depuración (como en el ejemplo)
        log.info("id: " + id);
        log.info("userId: " + userId);
        log.info("supplierId: " + supplierId);
        log.info("supplierName: " + supplierName);
        log.info("date: " + date);
        log.info("purchaseStatus: " + purchaseStatus);
        log.info("observation: " + observation);
        log.info("Page: " + page);
        log.info("Size: " + size);
        log.info("Orders: " + orders);
        log.info("SortBy: " + sortBy);

        Page<PurchaseSupplierResponseDTO> searchResult = purchaseSupplierRepository.searchPurchaseSuppliers(
                id, userId, supplierId, supplierName, date, purchaseStatus, observation, pagingSort
        );

        log.info("Search results: " + searchResult.getContent());
        return searchResult;
    }

    @Override
    public GenericResponse deletePurchase(Long purchase) {

        PurchaseSupplier purchaseSupplier = purchaseSupplierRepository.findById(purchase).get();
        purchaseSupplier.setStatus("INACTIVE");

        Transaction transaction = transactionRepository.findById(purchaseSupplier.getTransactionId()).get();

        transaction.setStatus("INACTIVE");

        List<TransactionProduct> products = transactionProductRepository.getQuantityByTransactionId(purchaseSupplier.getTransactionId());

        for (TransactionProduct product : products) {

            Product product1 = productRepository.findById(product.getProductId()).get();
            Long newQuantity = product1.getQuantity() - product.getQuantity();
            product1.setQuantity(newQuantity);
            productRepository.save(product1);
        }

        purchaseSupplierRepository.save(purchaseSupplier);
        transactionRepository.save(transaction);


        return new GenericResponse("Eliminacion con exito", 200);
    }

    @Override
    public Page<PurchaseProductsSupplier> getAllProductByPurchaseId(int page, int size, String orders, String sortBy, Long purchaseSupplierId) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return purchaseSupplierRepository.findAllPurchaseProducts(pagingSort, purchaseSupplierId);
    }

    @Override
    public Page<AmountOwesSupplierDTO> getAllAmountThatSupplierOwes(int page, int size, String orders, String sortBy, Long supplierId) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return productWareHouseRepository.amountOwesSupplierDTO(supplierId, pagingSort);
    }

    @Override
    public Page<SuppliersWhoMustDTO> getAllSupplierOwes(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return supplierRepository.getAllSupplierWithDebt(pagingSort);
    }

    public void addQuantityProductToSupplier(Long productId, Long supplierId, Long reservedQuantity){


        ProductWarehouse productWarehouse = productWareHouseRepository.findByProductIdAndSupplierId(productId, supplierId);

        if (productWarehouse == null){

            ProductWarehouse productWarehouse1 = new ProductWarehouse();
            productWarehouse1.setProductId(productId);
            productWarehouse1.setSupplierId(supplierId);
            productWarehouse1.setReservedQuantity(reservedQuantity);
            productWareHouseRepository.save(productWarehouse1);

        }else{
            Long quantity = productWarehouse.getReservedQuantity() + reservedQuantity;
            productWarehouse.setReservedQuantity(quantity);
            productWareHouseRepository.save(productWarehouse);
        }





    }


}
