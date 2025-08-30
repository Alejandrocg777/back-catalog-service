package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.ProductBusiness;
import com.asb.backCompanyService.dto.request.ProductRequestDTO;
import com.asb.backCompanyService.dto.request.updateProductQuantityDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.ProductResponseDTO;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.exception.GenericException;
import com.asb.backCompanyService.model.Category;
import com.asb.backCompanyService.model.Product;
import com.asb.backCompanyService.repository.CategoryRepository;
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
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ProductService implements ProductBusiness {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductRequestDTO save(ProductRequestDTO request) {
        try {
            // Validar que categoryId esté presente
            if (request.getCategoryId() == null) {
                throw new GenericException("El categoryId es requerido para calcular el statusProduct", HttpStatus.BAD_REQUEST);
            }


            // Crear el producto desde DTO
            Product product = new Product();
            product.setProductName(request.getProductName());
            product.setPrice(request.getPrice());
            product.setCategoryId(request.getCategoryId());
            product.setImage(request.getImage());
            product.setDescription(request.getDescription());
            product.setQuantity(0L);
            product.setStatus("ACTIVE");

            // Guardar el producto
            Product newProduct = productRepository.save(product);

            // Mapear de vuelta a DTO
            ProductRequestDTO response = new ProductRequestDTO();
            BeanUtils.copyProperties(newProduct, response);
            return response;

        } catch (Exception e) {
            log.error("Error al guardar el producto", e);
            throw new RuntimeException("Error al guardar el producto", e);
        }
    }

    public String calculateProductStatus(Long categoryId, Long quantity){

        // Buscar la categoría por ID
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GenericException("Categoría no encontrada con ID: " + categoryId, HttpStatus.BAD_REQUEST));

        // Calcular statusProduct basado en quantity y valores de categoría
        Double soldOutValue = category.getSoldOutValue();
        Double fewUnits = category.getFewUnits();

        if (quantity == null || soldOutValue == null || fewUnits == null) {
            throw new GenericException("Valores requeridos (quantity, soldOutValue, fewUnits) no pueden ser nulos", HttpStatus.BAD_REQUEST);
        }

        String statusProduct;
        if (quantity >= 0 && quantity <= soldOutValue) {
            statusProduct = "agotado";
        } else if (quantity > soldOutValue && quantity <= fewUnits) {
            statusProduct = "pocas unidades";
        } else {
            statusProduct = "disponible";
        }

        return statusProduct;
    }


    @Override
    public GenericResponse update(Long id, ProductRequestDTO request) {
        if (!productRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "producto no existe");

        Optional<Product> productOptional = productRepository.findById(id);

        Product product = productOptional.get();
       if (request.getProductName() != null)  product.setProductName(request.getProductName());
       if (request.getPrice() != null) product.setPrice(request.getPrice());
       if (request.getQuantity() != null) {

           product.setQuantity(request.getQuantity());
           product.setProductStatus(calculateProductStatus(request.getCategoryId(), request.getQuantity()));
       }
       if (request.getCategoryId() != null)  product.setCategoryId(request.getCategoryId());
       if (request.getImage() != null)  product.setImage(request.getImage());
       if (request.getDescription() != null)  product.setDescription(request.getDescription());
       if (request.getStatus() != null)  product.setStatus(request.getStatus());

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
        response.setPrice(productOptional.get().getPrice());
        response.setDescription(productOptional.get().getDescription());
        response.setCategoryId(productOptional.get().getCategoryId());
        response.setQuantity(productOptional.get().getQuantity());
        response.setImage(productOptional.get().getImage());
        response.setProductStatus(productOptional.get().getProductStatus());
        response.setStatus(productOptional.get().getStatus());
        return response;
    }

    @Override
    public Page<ProductResponseDTO> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String productName = null;
        String price = null;
        String description = null;
        String categoryName = null;
        String quantity = null;
        String productStatus = null;

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
            id = "%" + customQuery.get("id") + "%";
        }

        if (customQuery.containsKey("productName")) {
            productName = "%" + customQuery.get("productName") + "%";
        }

        if (customQuery.containsKey("price")) {
            price = "%" + customQuery.get("price") + "%";
        }

        if (customQuery.containsKey("description")) {
            description = "%" + customQuery.get("description") + "%";
        }

         if (customQuery.containsKey("categoryName")) {
             categoryName = "%" + customQuery.get("categoryName") + "%";
        }

         if (customQuery.containsKey("quantity")) {
             quantity = "%" + customQuery.get("quantity") + "%";
        }


        if (customQuery.containsKey("productStatus")) {
            productStatus = "%" + customQuery.get("productStatus") + "%";
        }

         Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        Page<ProductResponseDTO> result = productRepository.search(id, quantity, productName, description, categoryName, price, productStatus, pagingSort);
        log.info("Resultados encontrados: {}", result.getContent());
        return result;
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

    @Override
    public GenericResponse addQuantity(updateProductQuantityDTO quantity) {
        if (!productRepository.existsById(quantity.getId())) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "producto no existe");
        Optional<Product> productOptional = productRepository.findById(quantity.getId());
        Long currentAmount = productOptional.get().getQuantity();
        Long newQuantity = currentAmount + quantity.getQuantity();
        productOptional.get().setQuantity(newQuantity);
        productOptional.get().setProductStatus(calculateProductStatus(productOptional.get().getCategoryId(), quantity.getQuantity()));
        productRepository.save(productOptional.get());

        return new GenericResponse("Suma realizada con exito", 200);
    }

    @Override
    public GenericResponse subtractQuantity(updateProductQuantityDTO quantity) {
        if (!productRepository.existsById(quantity.getId())) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "producto no existe");
        Optional<Product> productOptional = productRepository.findById(quantity.getId());
        Long currentAmount = productOptional.get().getQuantity();
        Long newQuantity = currentAmount - quantity.getQuantity();
        productOptional.get().setQuantity(newQuantity);
        productOptional.get().setProductStatus(calculateProductStatus(productOptional.get().getCategoryId(), newQuantity));
        productRepository.save(productOptional.get());

        return new GenericResponse("Resta realizada con exito", 200);
    }
}
