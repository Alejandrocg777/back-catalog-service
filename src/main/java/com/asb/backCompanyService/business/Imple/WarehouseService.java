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
    public Warehouse save(WarehouseDto warehouseDto) {

            log.info("Guardando Warehouse: {}", warehouseDto);
            Warehouse warehouse = new Warehouse();
            warehouse.setWarehouseName(warehouseDto.getWarehouseName());
            warehouse.setAddress(warehouseDto.getAddress());
            warehouse.setDescription(warehouseDto.getDescription());
            warehouse.setStatus("ACTIVE");

            return repository.save(warehouse);
    }

    @Override
    @Transactional
    public GenericResponse update(Long id, WarehouseDto warehouseDto) {

        GenericResponse response = new GenericResponse();
        log.info("Iniciando actualización para Warehouse con ID: {} y WarehouseDto: {}", id, warehouseDto);

        Optional<Warehouse> optionalWarehouse = repository.findById(id);
        if (!optionalWarehouse.isPresent()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La bodega no existe");
        }

        Warehouse warehouse = optionalWarehouse.get();

        if (warehouseDto.getWarehouseName() != null) {
            warehouse.setWarehouseName(warehouseDto.getWarehouseName());
        }

        if (warehouseDto.getAddress() != null) {
            warehouse.setAddress(warehouseDto.getAddress());
        }

        if (warehouseDto.getDescription() != null) {
            warehouse.setDescription(warehouseDto.getDescription());
        }

        repository.save(warehouse);

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Bodega actualizada correctamente");

        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            Warehouse warehouse = repository.findById(id).orElseThrow(() ->
                    new RuntimeException("La bodega no fue encontrada por el ID " + id));
            warehouse.setStatus("INCATIVE");
            repository.save(warehouse);
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
        return repository.getActive(pagingSort);
    }

    @Override
    public Page<Warehouse> searchWarehouses(Map<String, String> customQuery) {
            String orders = "ASC";
            String sortBy = "id";
            int page = 0;
            int size = 10;
            String id = null;
            String warehouseName = null;
            String description = null;
            String address = null;
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

            if (customQuery.containsKey("id")) {
                id = "%" + customQuery.get("id") + "%";
            }

            if (customQuery.containsKey("warehouseName")) {
                warehouseName = "%" + customQuery.get("warehouseName").toUpperCase() + "%";
            }

            if (customQuery.containsKey("description")) {
                description = "%" + customQuery.get("description").toUpperCase() + "%";
            }

            if (customQuery.containsKey("address")) {
                address = "%" + customQuery.get("address").toUpperCase() + "%";
            }

            if (customQuery.containsKey("status")) {
                status = "%" + customQuery.get("status").toUpperCase() + "%";
            }

            Sort.Direction direction = Sort.Direction.fromString(orders);
            Sort sort = Sort.by(direction, sortBy);

            Pageable pagingSort = PageRequest.of(page, size, sort);

            log.info("id: " + id);
            log.info("warehouseName: " + warehouseName);
            log.info("description: " + description);
            log.info("address: " + address);
            log.info("status: " + status);
            log.info("Page: " + page);
            log.info("Size: " + size);
            log.info("Orders: " + orders);
            log.info("SortBy: " + sortBy);

            Page<Warehouse> searchResult = repository.searchWarehouses(id, warehouseName, description, address, status, pagingSort);
            log.info("Search results: " + searchResult.getContent());
            return searchResult;
        }

    @Override
    public List<Warehouse> getAllWarehouses() {
        try {
            return repository.findByStatus("ACTIVE");
        } catch (Exception e) {
            log.error("Error al obtener las bodegas");
            throw new RuntimeException("No se pueden recuperar las bodegas", e);
        }
    }

}
