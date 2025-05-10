package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IItemVariantBusiness;
import com.asb.backCompanyService.dto.request.ItemVariantDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.ItemVariant;
import com.asb.backCompanyService.repository.ItemVariantRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ItemVariantService implements IItemVariantBusiness {

    private final ItemVariantRepository repository;

    @Override
    @Transactional
    public ItemVariantDto save(ItemVariantDto itemVariantDto) {
        try {
            // Verifica si el item_variant ya existe por ID
            if (itemVariantDto.getId() != null && repository.existsById(itemVariantDto.getId())) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La variante ya existe");
            }

            // Crear nueva variante
            ItemVariant itemVariant = new ItemVariant();
            itemVariant.setVariantType(itemVariantDto.getVariantType());
            itemVariant.setDescription(itemVariantDto.getDescription());
            itemVariant.setStatus("ACTIVE");

            // Guardar nueva variante
            ItemVariant newItemVariant = repository.save(itemVariant);
            ItemVariantDto savedItemVariantDto = new ItemVariantDto();
            BeanUtils.copyProperties(newItemVariant, savedItemVariantDto);
            return savedItemVariantDto;
        } catch (Exception e) {
            log.error("Error al guardar la variante", e);
            throw new RuntimeException("Error al guardar la variante", e);
        }
    }

    @Override
    @Transactional
    public GenericResponse update(long id, ItemVariantDto itemVariantDto) {
        GenericResponse response = new GenericResponse();
        try {
            // Buscar la variante existente
            Optional<ItemVariant> optionalItemVariant = repository.findById(id);
            if (!optionalItemVariant.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La variante no existe");
            }

            // Actualizar la variante
            ItemVariant itemVariant = optionalItemVariant.get();
            if (itemVariantDto.getVariantType() != null) itemVariant.setVariantType(itemVariantDto.getVariantType());
            if (itemVariantDto.getDescription() != null) itemVariant.setDescription(itemVariantDto.getDescription());
            if (itemVariantDto.getStatus() != null) itemVariant.setStatus(itemVariantDto.getStatus());

            repository.save(itemVariant);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Variante actualizada correctamente");
        } catch (Exception e) {
            log.error("Error al actualizar la variante", e);
            throw new RuntimeException("Error al actualizar la variante", e);
        }
        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        try {
            // Verificar existencia de la variante
            Optional<ItemVariant> itemVariantOptional = repository.findById(id);
            if (!itemVariantOptional.isPresent()) {
                throw new RuntimeException("La variante no fue encontrada por el id " + id);
            }

            // Cambiar estado a "INACTIVE" en lugar de eliminación física
            ItemVariant itemVariant = itemVariantOptional.get();
            itemVariant.setStatus("INACTIVE");
            repository.save(itemVariant);
            return true;
        } catch (Exception e) {
            log.error("Error al eliminar la variante", e);
            throw new RuntimeException("Error al eliminar la variante", e);
        }
    }

    @Override
    public ItemVariantDto get(long id) {
        try {
            Optional<ItemVariant> itemVariantOptional = repository.findById(id);
            if (!itemVariantOptional.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La variante no existe");
            }

            ItemVariantDto itemVariantDto = new ItemVariantDto();
            BeanUtils.copyProperties(itemVariantOptional.get(), itemVariantDto);
            return itemVariantDto;
        } catch (Exception e) {
            log.error("Error al obtener la variante", e);
            throw new RuntimeException("Error al obtener la variante", e);
        }
    }

    @Override
    public Page<ItemVariant> getAll(int page, int size, String orders, String sortBy) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(orders);
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));
            return repository.getActiveItemVariants(pagingSort);
        } catch (Exception e) {
            log.error("Error al obtener todas las variantes", e);
            throw new RuntimeException("Error al obtener todas las variantes", e);
        }
    }

    @Override
    public Page<ItemVariant> searchCustom(Map<String, String> customQuery) {
        try {
            // Establecer valores predeterminados y buscar filtros
            String orders = customQuery.getOrDefault("orders", "ASC");
            String sortBy = customQuery.getOrDefault("sortBy", "variant_id");
            int page = Integer.parseInt(customQuery.getOrDefault("page", "0"));
            int size = Integer.parseInt(customQuery.getOrDefault("size", "10"));
            String id = customQuery.containsKey("id") ? "%" + customQuery.get("id") + "%" : null;
            String variantType = customQuery.containsKey("variantType") ? "%" + customQuery.get("variantType") + "%" : null;
            String name = customQuery.containsKey("name") ? "%" + customQuery.get("name") + "%" : null;
            String description = customQuery.containsKey("description") ? "%" + customQuery.get("description") + "%" : null;
            String status = customQuery.containsKey("status") ? "%" + customQuery.get("status") + "%" : null;

            Pageable pagingSort = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(orders), sortBy));
            return repository.searchItemVariants(id, variantType, name, description, status, pagingSort);
        } catch (Exception e) {
            log.error("Error en la búsqueda personalizada de variantes", e);
            throw new RuntimeException("Error en la búsqueda personalizada de variantes", e);
        }
    }

    @Override
    public List<ItemVariant> getAllItemVariants(Map<String, String> customQuery) {
        try {
            String description = customQuery.getOrDefault("description", "");
            String variantType = customQuery.get("variantType");
            Pageable pagingSort = PageRequest.of(
                    Integer.parseInt(customQuery.getOrDefault("offset", "0")),
                    Integer.parseInt(customQuery.getOrDefault("limit", "10"))
            );

            Page<ItemVariant> entityPage = repository.findByVariantTypeAndDescriptionContainingIgnoreCase(variantType, description, pagingSort);

            return entityPage.getContent();
        } catch (Exception e) {
            log.error("Error al obtener variantes con filtros", e);
            throw new RuntimeException("Error al obtener variantes con filtros", e);
        }
    }

    @Override
    public List<ItemVariant> getAllItemVariants() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener todas las variantes", e);
            throw new RuntimeException("No se pueden recuperar las variantes", e);
        }
    }
}
