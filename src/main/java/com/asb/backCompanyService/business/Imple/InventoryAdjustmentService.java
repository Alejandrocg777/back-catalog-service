package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IInventoryAdjustmentBusiness;
import com.asb.backCompanyService.dto.request.InventoryAdjustmentDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.InventoryAdjustment;
import com.asb.backCompanyService.repository.InventoryAdjustmentRepository;
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
public class InventoryAdjustmentService implements IInventoryAdjustmentBusiness {

    private final InventoryAdjustmentRepository repository;

    @Override
    @Transactional
    public InventoryAdjustmentDto save(InventoryAdjustmentDto inventoryAdjustmentDto) {
        try {
            InventoryAdjustment inventoryAdjustment = new InventoryAdjustment();
            inventoryAdjustment.setWarehouseId(inventoryAdjustmentDto.getWarehouseId());
            inventoryAdjustment.setTotalAjustado(inventoryAdjustmentDto.getTotalAjustado());
            inventoryAdjustment.setObservaciones(inventoryAdjustmentDto.getObservaciones());

            InventoryAdjustment newInventoryAdjustment = repository.save(inventoryAdjustment);

            InventoryAdjustmentDto savedInventoryAdjustmentDto = new InventoryAdjustmentDto();
            BeanUtils.copyProperties(newInventoryAdjustment, savedInventoryAdjustmentDto);
            return savedInventoryAdjustmentDto;
        } catch (Exception e) {
            log.error("Error al guardar el ajuste de inventario", e);
            throw new RuntimeException("Error al guardar el ajuste de inventario", e);
        }
    }

    @Override
    @Transactional
    public GenericResponse update(long id, InventoryAdjustmentDto inventoryAdjustmentDto) {
        GenericResponse response = new GenericResponse();
        try {
            // Buscar el ajuste de inventario existente
            Optional<InventoryAdjustment> optionalInventoryAdjustment = repository.findById(id);
            if (!optionalInventoryAdjustment.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El ajuste de inventario no existe");
            }

            // Actualizar el ajuste de inventario
            InventoryAdjustment inventoryAdjustment = optionalInventoryAdjustment.get();
            if (inventoryAdjustmentDto.getWarehouseId() != null) inventoryAdjustment.setWarehouseId(inventoryAdjustmentDto.getWarehouseId());
            if (inventoryAdjustmentDto.getTotalAjustado() != null) inventoryAdjustment.setTotalAjustado(inventoryAdjustmentDto.getTotalAjustado());
            if (inventoryAdjustmentDto.getObservaciones() != null) inventoryAdjustment.setObservaciones(inventoryAdjustmentDto.getObservaciones());

            repository.save(inventoryAdjustment);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Ajuste de inventario actualizado correctamente");
        } catch (Exception e) {
            log.error("Error al actualizar el ajuste de inventario", e);
            throw new RuntimeException("Error al actualizar el ajuste de inventario", e);
        }
        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        try {
            // Verificar existencia del ajuste de inventario
            Optional<InventoryAdjustment> inventoryAdjustmentOptional = repository.findById(id);
            if (!inventoryAdjustmentOptional.isPresent()) {
                throw new RuntimeException("El ajuste de inventario no fue encontrado por el id " + id);
            }

            // Aquí podrías eliminar o cambiar su estado dependiendo de la lógica
            repository.deleteById(id); // Elimina físicamente, o usa una lógica de "soft delete" cambiando el estado si es necesario
            return true;
        } catch (Exception e) {
            log.error("Error al eliminar el ajuste de inventario", e);
            throw new RuntimeException("Error al eliminar el ajuste de inventario", e);
        }
    }

    @Override
    public InventoryAdjustmentDto get(long id) {
        try {
            Optional<InventoryAdjustment> inventoryAdjustmentOptional = repository.findById(id);
            if (!inventoryAdjustmentOptional.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El ajuste de inventario no existe");
            }

            InventoryAdjustmentDto inventoryAdjustmentDto = new InventoryAdjustmentDto();
            BeanUtils.copyProperties(inventoryAdjustmentOptional.get(), inventoryAdjustmentDto);
            return inventoryAdjustmentDto;
        } catch (Exception e) {
            log.error("Error al obtener el ajuste de inventario", e);
            throw new RuntimeException("Error al obtener el ajuste de inventario", e);
        }
    }

    @Override
    public Page<InventoryAdjustment> getAll(int page, int size, String orders, String sortBy) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(orders);
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));
            return repository.findAll(pagingSort);
        } catch (Exception e) {
            log.error("Error al obtener todos los ajustes de inventario", e);
            throw new RuntimeException("Error al obtener todos los ajustes de inventario", e);
        }
    }

    @Override
    public Page<InventoryAdjustment> searchCustom(Map<String, String> customQuery) {
        try {
            // Establecer valores predeterminados y buscar filtros
            String orders = customQuery.getOrDefault("orders", "ASC");
            String sortBy = customQuery.getOrDefault("sortBy", "id");
            int page = Integer.parseInt(customQuery.getOrDefault("page", "0"));
            int size = Integer.parseInt(customQuery.getOrDefault("size", "10"));
            String id = customQuery.containsKey("id") ? "%" + customQuery.get("id") + "%" : null;
            String warehouseId = customQuery.containsKey("warehouseId") ? "%" + customQuery.get("warehouseId") + "%" : null;
            String observaciones = customQuery.containsKey("observaciones") ? "%" + customQuery.get("observaciones") + "%" : null;
            String totalAjustado = customQuery.containsKey("totalAjustado") ? "%" + customQuery.get("totalAjustado") + "%" : null;

            Pageable pagingSort = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(orders), sortBy));
            return repository.searchInventoryAdjustment(id, warehouseId, observaciones, totalAjustado, pagingSort);
        } catch (Exception e) {
            log.error("Error en la búsqueda personalizada de ajustes de inventario", e);
            throw new RuntimeException("Error en la búsqueda personalizada de ajustes de inventario", e);
        }
    }

    @Override
    public List<InventoryAdjustment> getAllInventoryAdjustments(Map<String, String> customQuery) {
        try {
            String observaciones = customQuery.getOrDefault("observaciones", "");
            Long warehouseId = Long.valueOf(customQuery.getOrDefault("warehouseId", "0"));
            Pageable pagingSort = PageRequest.of(
                    Integer.parseInt(customQuery.getOrDefault("offset", "0")),
                    Integer.parseInt(customQuery.getOrDefault("limit", "10"))
            );

            Page<InventoryAdjustment> entityPage = repository.findByWarehouseIdAndObservacionesContainingIgnoreCase(warehouseId, observaciones, pagingSort);

            return entityPage.getContent();
        } catch (Exception e) {
            log.error("Error al obtener ajustes de inventario con filtros", e);
            throw new RuntimeException("Error al obtener ajustes de inventario con filtros", e);
        }
    }

    @Override
    public List<InventoryAdjustment> getAllInventoryAdjustments() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener todos los ajustes de inventario", e);
            throw new RuntimeException("No se pueden recuperar los ajustes de inventario", e);
        }
    }
}
