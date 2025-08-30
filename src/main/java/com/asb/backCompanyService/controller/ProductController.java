package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.ProductBusiness;
import com.asb.backCompanyService.dto.request.ProductRequestDTO;
import com.asb.backCompanyService.dto.request.SellerRequestDTO;
import com.asb.backCompanyService.dto.request.updateProductQuantityDTO;
import com.asb.backCompanyService.dto.responde.CategoryResponseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.ProductResponseDTO;
import com.asb.backCompanyService.model.Product;
import com.asb.backCompanyService.model.Seller;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/product")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class ProductController {

    private final ProductBusiness productBusiness;
    private final Cloudinary cloudinary;

    @PostMapping("/create")
    public ResponseEntity<ProductRequestDTO> save(@RequestParam("productName") String productName,
                                                  @RequestParam("price") Double price,
                                                  @RequestParam("description") String description,
                                                  @RequestParam("categoryId") Long categoryId,
                                                  @RequestParam("image")MultipartFile image,
                                                  @RequestParam("purchasePrice") Double purchasePrice,
                                                  @RequestParam("quantity") Long quantity) throws IOException {

        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setProductName(productName);
        productRequestDTO.setPrice(price);
        productRequestDTO.setPurchasePrice(purchasePrice);
        productRequestDTO.setQuantity(quantity);
        productRequestDTO.setDescription(description);
        productRequestDTO.setCategoryId(categoryId);
        if(image != null) {
            try {
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                String imageUrl = (String) uploadResult.get("url");
                productRequestDTO.setImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen", e);
            }
        }else{
            productRequestDTO.setImage("");
        }
        ProductRequestDTO savedSeller = productBusiness.save(productRequestDTO);
        return ResponseEntity.ok(savedSeller);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") Long id,
                                                  @RequestParam(value = "productName", required = false) String productName,
                                                  @RequestParam(value = "price", required = false) Double price,
                                                  @RequestParam(value = "description",required = false) String description,
                                                  @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                  @RequestParam(value = "quantity", required = false) Long quantity,
                                                  @RequestParam(value = "image",required = false)MultipartFile image,
                                                  @RequestParam("purchasePrice") Double purchasePrice) {
        log.info("Iniciando actualización para City con ID: {}", id);
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setProductName(productName);
        productRequestDTO.setPrice(price);
        productRequestDTO.setDescription(description);
        productRequestDTO.setCategoryId(categoryId);
        productRequestDTO.setQuantity(quantity);
        productRequestDTO.setPurchasePrice(purchasePrice);
        if(image != null) {
            try {
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                String imageUrl = (String) uploadResult.get("url");
                productRequestDTO.setImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen", e);
            }
        }
        GenericResponse response = productBusiness.update(id, productRequestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        productBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size,
                                                         @RequestParam(defaultValue = "ASC") String orders,
                                                         @RequestParam(defaultValue = "id") String sortBy) {
        Page<ProductResponseDTO> products = productBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductResponseDTO> get(@PathVariable("id") Long id) {
        ProductResponseDTO requestDTO = productBusiness.get(id);
        return ResponseEntity.ok(requestDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDTO>> search(@RequestParam Map<String, String> customQuery) {
        Page<ProductResponseDTO> products = productBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/no-page/getAllProduct")
    public ResponseEntity<List<Product>> getAllNopage() {
        log.info("Iniciando endpoint para obtener todas las taxes");
        List<Product> response = productBusiness.getAllNoPage();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/add-quantity")
    public ResponseEntity<GenericResponse>addQuantity(@RequestBody updateProductQuantityDTO quantity){
        log.info("servicio para sumar cantidad a los productos");
        return new ResponseEntity<>(productBusiness.addQuantity(quantity), HttpStatus.OK);
    }


    @PutMapping("/subtract-quantity")
    public ResponseEntity<GenericResponse>subtractQuantity(@RequestBody updateProductQuantityDTO quantity){
        log.info("servicio para restar cantidad a los productos");
        return new ResponseEntity<>(productBusiness.subtractQuantity(quantity), HttpStatus.OK);
    }

}
