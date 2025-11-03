package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Imple.SupplierService;
import com.asb.backCompanyService.business.Interfaces.SupplierBusiness;
import com.asb.backCompanyService.dto.request.SupplierCreateDTO;
import com.asb.backCompanyService.dto.request.SupplierProductDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.SupplierDtoResponse;
import com.asb.backCompanyService.dto.responde.SupplierProductDtoResponse;
import com.asb.backCompanyService.model.Supplier;
import com.asb.backCompanyService.model.SupplierProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/supplier")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class SupplierController {

    private final SupplierBusiness supplierService;


    @PostMapping("/create")
    public ResponseEntity<Supplier> createSupplier(@RequestBody SupplierCreateDTO createDTO) {
        log.info("Iniciando el endpoint para crear proveedor");
        Supplier createdSupplier = supplierService.createSupplier(createDTO);
        return new ResponseEntity<>(createdSupplier, HttpStatus.CREATED);
    }

    @PutMapping("/update/{supplierId}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long supplierId, @RequestBody SupplierCreateDTO updateDTO) {
        log.info("Iniciando el endpoint para actualizar proveedor con ID: {}", supplierId);
        Supplier updatedSupplier = supplierService.updateSupplier(supplierId, updateDTO);
        return new ResponseEntity<>(updatedSupplier, HttpStatus.OK);
    }

    @PostMapping("/addProductsBySupplier/{supplierId}")
    public ResponseEntity<SupplierProduct> addProductToSupplier(@PathVariable Long supplierId,
                                                                @RequestBody SupplierProductDTO addDTO) {
        log.info("Iniciando el endpoint para agregar un producto a proveedor con ID: {}", supplierId);
        SupplierProduct addedProduct = supplierService.addProductToSupplier(supplierId, addDTO);
        return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
    }


    @GetMapping("/get-all")
    public ResponseEntity<Page<SupplierDtoResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(defaultValue = "ASC") String orders,
                                                            @RequestParam(defaultValue = "id") String sortBy) {
        Page<SupplierDtoResponse> suppliers = supplierService.getAll(page, size, orders, sortBy);
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }


    @GetMapping("/get-all/no-page")
    public ResponseEntity<List<Supplier>> getAllNoPage() {
        List<Supplier> suppliers = supplierService.getAllNoPage();
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<SupplierDtoResponse>> search(@RequestParam Map<String, String> customQuery) {
        Page<SupplierDtoResponse> suppliers = supplierService.searchCustom(customQuery);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/getAllProductsBySupplier/{supplierId}")
    public ResponseEntity<Page<SupplierProductDtoResponse>> getAllProductsBySupplier(
            @PathVariable Long supplierId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ASC") String orders,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<SupplierProductDtoResponse> products = supplierService.getAllProductsBySupplier(supplierId, page, size, orders, sortBy);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/search/{supplierId}")
    public ResponseEntity<Page<SupplierProductDtoResponse>> searchProductsBySupplier(@PathVariable Long supplierId, @RequestParam Map<String, String> customQuery) {
        Page<SupplierProductDtoResponse> products = supplierService.searchProductsBySupplier(supplierId, customQuery);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{supplierId}")
    public ResponseEntity<GenericResponse> deleteSupplierLogical(@PathVariable Long supplierId) {
        log.info("Iniciando el endpoint para borrado lógico de proveedor con ID: {}", supplierId);
        return new ResponseEntity<>(supplierService.deleteSupplierLogical(supplierId) , HttpStatus.OK);
    }

    @DeleteMapping("/products/delete/{supplierId}/{productId}")
    public ResponseEntity<GenericResponse> deleteSupplierProductLogical(@PathVariable("supplierId")Long supplierId,
                                                                        @PathVariable("productId")Long productId) {
        log.info("Iniciando el endpoint para borrado lógico de detalle ");
        GenericResponse response = supplierService.deleteSupplierProductLogical(supplierId, productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-purchase-price/{supplierId}/{productId}")
    public ResponseEntity<BigDecimal> getPurchasePrice(@PathVariable("supplierId")Long supplierId,
                                                       @PathVariable("productId")Long productId) {
        log.info("Iniciado el enpoint que nos trae el precio de compra");
        return new ResponseEntity<>(supplierService.getPurchasePrice(supplierId, productId), HttpStatus.OK);
    }
}
