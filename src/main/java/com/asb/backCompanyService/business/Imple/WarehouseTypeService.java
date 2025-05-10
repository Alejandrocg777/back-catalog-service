package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IWarehouseTypeBusiness;
import com.asb.backCompanyService.dto.request.WarehouseTypeDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.WarehouseType;
import com.asb.backCompanyService.repository.WarehouseTypeRepository;
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
public class WarehouseTypeService implements IWarehouseTypeBusiness {

    private final WarehouseTypeRepository repository;

    @Override
    @Transactional
    public WarehouseTypeDto save(WarehouseTypeDto warehouseTypeDto) {
        try {
            log.info("Guardando WarehouseType: {}", warehouseTypeDto);
            WarehouseType warehouseType = new WarehouseType();
            BeanUtils.copyProperties(warehouseTypeDto, warehouseType);

            WarehouseType newWarehouseType = repository.save(warehouseType);
            WarehouseTypeDto savedWarehouseTypeDto = new WarehouseTypeDto();
            BeanUtils.copyProperties(newWarehouseType, savedWarehouseTypeDto);
            return savedWarehouseTypeDto;

        } catch (Exception e) {
            log.error("Error al guardar WarehouseType", e);
            throw new RuntimeException("Error al guardar el tipo de almacén", e);
        }
    }

    @Override
    @Transactional
    public GenericResponse update(Long id, WarehouseTypeDto warehouseTypeDto) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("Iniciando actualización para WarehouseType con ID: {} y WarehouseTypeDto: {}", id, warehouseTypeDto);

            Optional<WarehouseType> optionalWarehouseType = repository.findById(id);
            if (!optionalWarehouseType.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El tipo de almacén no existe");
            }

            WarehouseType warehouseType = optionalWarehouseType.get();
            BeanUtils.copyProperties(warehouseTypeDto, warehouseType);

            repository.save(warehouseType);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Tipo de almacén actualizado correctamente");
        } catch (Exception e) {
            log.error("Error al actualizar el tipo de almacén: {}", e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error al actualizar el tipo de almacén");
            return response;
        }
        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            // Obtener el tipo de almacén por su ID
            WarehouseType warehouseType = repository.findById(id).get();

            // Cambiar el estado a "INACTIVE" en lugar de eliminar
            warehouseType.setStatus("INACTIVE");

            // Guardar el cambio en la base de datos
            repository.save(warehouseType);

            return true;
        } else {
            throw new RuntimeException("El tipo de almacén no fue encontrado por el id " + id);
        }
    }
    @Override
    public WarehouseTypeDto get(Long id) {
        Optional<WarehouseType> warehouseTypeOptional = repository.findById(id);
        WarehouseTypeDto warehouseTypeDto = null;
        if (warehouseTypeOptional.isPresent()) {
            warehouseTypeDto = new WarehouseTypeDto();
            BeanUtils.copyProperties(warehouseTypeOptional.get(), warehouseTypeDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El tipo de almacén no existe");
        }
        return warehouseTypeDto;
    }

    @Override
    public Page<WarehouseType> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return repository.findAll(pagingSort);
    }

    @Override
    public Page<WarehouseType> searchWarehouseTypes(Map<String, String> customQuery) {
        String id = "%" + customQuery.getOrDefault("id", "") + "%";
        String description = "%" + customQuery.getOrDefault("description", "") + "%";
        String status = "%" + customQuery.getOrDefault("status", "") + "%";

        Sort.Direction direction = Sort.Direction.fromString("ASC");
        Sort sort = Sort.by(direction, "id");
        Pageable pagingSort = PageRequest.of(0, 5, sort);

        return repository.searchWarehouseTypes(id, description, status, pagingSort);
    }

    @Override
    public List<WarehouseType> getAllWarehouseTypes() {
        try {
            return repository.findAll();  // Devuelve todos los registros sin paginación
        } catch (Exception e) {
            log.error("Error al obtener los tipos de almacenes", e);
            throw new RuntimeException("No se pueden recuperar los tipos de almacenes", e);
        }
    }
}
