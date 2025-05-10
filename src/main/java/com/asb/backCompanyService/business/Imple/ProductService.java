package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.ProductBusiness;
import com.asb.backCompanyService.dto.request.ProductRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.ProductResponseDTO;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Product;
import com.asb.backCompanyService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ProductService implements ProductBusiness {

    private final ProductRepository productRepository;
    private final DiscountService discountService;
    private final TaxService taxService;

    @Override
    public ProductRequestDTO save(ProductRequestDTO request) {
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setPrice(request.getPrice());
        product.setStatus(request.getStatus());
        Product newProduct = productRepository.save(product);

        ProductRequestDTO response = new ProductRequestDTO();
        BeanUtils.copyProperties(newProduct, response);
        return response;
    }


    public Double getDiscount(Long productId, Long quantity){
        if (!productRepository.existsById(productId)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "producto no existe");

        Product product = productRepository.findById(productId).get();
        return  discountService.calculatePriceLessDiscount(product.getDiscountId(),product.getPrice(),quantity);


    }

    public  Double getTax(Long productId, Double value){
        if (!productRepository.existsById(productId)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "producto no existe");
        Product product = productRepository.findById(productId).get();
        return taxService.calculatePricePlusTax(product.getTaxConfigurationId(),value);


    }

    @Override
    public GenericResponse update(Long id, ProductRequestDTO request) {
        if (!productRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "producto no existe");

        Optional<Product> productOptional = productRepository.findById(id);

        Product product = productOptional.get();
        product.setProductName(request.getProductName());
        product.setPrice(request.getPrice());
        product.setStatus(request.getStatus());
        productRepository.save(product);

        return new GenericResponse("product actualizado con exito", 200);
    }

    @Override
    public Boolean delete(Long id) {
        if (productRepository.existsById(id)) {
            Product product = productRepository.findById(id).get();
            product.setStatus("INACTIVE");
            productRepository.save(product);
            return true;
        } else {
            throw new RuntimeException("el producto no fue encontrada por el id " + id);
        }
    }

    @Override
    public Page<ProductResponseDTO> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return productRepository.getStatus(pagingSort);
    }

    @Override
    public ProductResponseDTO get(Long id) {
        if (!productRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "seller no existe");

        Optional<Product> productOptional = productRepository.findById(id);

        ProductResponseDTO response = new ProductResponseDTO();
        response.setId(productOptional.get().getId());
        response.setProductName(productOptional.get().getProductName());
        response.setDiscountId(productOptional.get().getDiscountId());
        response.setTaxConfigurationId(productOptional.get().getTaxConfigurationId());
        response.setPrice(productOptional.get().getPrice());
        response.setStatus(productOptional.get().getStatus());
        return response;
    }

    @Override
    public List<Product> getAllNoPage() {
        try {
            return (List<Product>) productRepository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener el seller");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar el seller", e);
        }
    }
}
