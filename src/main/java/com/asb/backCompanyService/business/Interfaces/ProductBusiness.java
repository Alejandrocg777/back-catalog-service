package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.ProductRequestDTO;
import com.asb.backCompanyService.dto.request.SellerRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.ProductResponseDTO;
import com.asb.backCompanyService.model.Product;
import com.asb.backCompanyService.model.Seller;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductBusiness {

    ProductRequestDTO save(ProductRequestDTO requestDTO);
    GenericResponse update(Long id, ProductRequestDTO requestDTO);
    Boolean delete(Long id);
    Page<ProductResponseDTO> getAll(int page, int size, String orders, String sortBy);
    ProductResponseDTO get(Long id);
    List<Product> getAllNoPage();
}
