package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IWarehouseBusiness;
import com.asb.backCompanyService.dto.request.WarehouseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Warehouse;
import com.asb.backCompanyService.repository.WarehouseRepository;
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
public class WarehouseService implements IWarehouseBusiness {

    private final WarehouseRepository repository;

    @Override
    @Transactional
    public WarehouseDto save(WarehouseDto warehouseDto) {
        try {
            log.info("Guardando Warehouse: {}", warehouseDto);
            Warehouse warehouse = new Warehouse();
            BeanUtils.copyProperties(warehouseDto, warehouse);

            // Guardar los nuevos campos: owner y description
            warehouse.setOwner(warehouseDto.getOwner());
            warehouse.setDescription(warehouseDto.getDescription());

            Warehouse newWarehouse = repository.save(warehouse);
            WarehouseDto savedWarehouseDto = new WarehouseDto();
            BeanUtils.copyProperties(newWarehouse, savedWarehouseDto);
            return savedWarehouseDto;

        } catch (Exception e) {
            log.error("Error al guardar Warehouse", e);
            throw new RuntimeException("Error al guardar la bodega", e);
        }
    }

    @Override
    @Transactional
    public GenericResponse update(Long id, WarehouseDto warehouseDto) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("Iniciando actualización para Warehouse con ID: {} y WarehouseDto: {}", id, warehouseDto);

            Optional<Warehouse> optionalWarehouse = repository.findById(id);
            if (!optionalWarehouse.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La bodega no existe");
            }

            Warehouse warehouse = optionalWarehouse.get();
            BeanUtils.copyProperties(warehouseDto, warehouse);

            // Actualizar los nuevos campos: owner y description
            warehouse.setOwner(warehouseDto.getOwner());
            warehouse.setDescription(warehouseDto.getDescription());

            repository.save(warehouse);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Bodega actualizada correctamente");
        } catch (Exception e) {
            log.error("Error al actualizar la bodega: {}", e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error al actualizar la bodega");
            return response;
        }
        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            Warehouse warehouse = repository.findById(id).orElseThrow(() ->
                    new RuntimeException("La bodega no fue encontrada por el ID " + id));
            repository.delete(warehouse);
            return true;
        } else {
            throw new RuntimeException("La bodega no fue encontrada por el ID " + id);
        }
    }

    @Override
    public WarehouseDto get(Long id) {
        Optional<Warehouse> warehouseOptional = repository.findById(id);
        WarehouseDto warehouseDto = null;
        if (warehouseOptional.isPresent()) {
            warehouseDto = new WarehouseDto();
            BeanUtils.copyProperties(warehouseOptional.get(), warehouseDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La bodega no existe");
        }
        return warehouseDto;
    }

    @Override
    public Page<Warehouse> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return repository.findAll(pagingSort);
    }

    @Override
    public Page<Warehouse> searchWarehouses(Map<String, String> customQuery) {
        String id = "%" + customQuery.getOrDefault("id", "") + "%";
        String warehouseName = "%" + customQuery.getOrDefault("warehouseName", "") + "%";
        String email = "%" + customQuery.getOrDefault("email", "") + "%";
        String address = "%" + customQuery.getOrDefault("address", "") + "%";
        String description = "%" + customQuery.getOrDefault("description", "") + "%";
        String owner = "%" + customQuery.getOrDefault("owner", "") + "%";

        Sort.Direction direction = Sort.Direction.fromString("ASC");
        Sort sort = Sort.by(direction, "id");
        Pageable pagingSort = PageRequest.of(0, 5, sort);

        return repository.searchWarehouses(id, warehouseName, email, address, description, owner, pagingSort);
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener las bodegas");
            throw new RuntimeException("No se pueden recuperar las bodegas", e);
        }
    }
}
