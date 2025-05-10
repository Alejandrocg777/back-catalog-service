package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IInventoryBusiness;
import com.asb.backCompanyService.dto.request.InventoryDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Inventory;
import com.asb.backCompanyService.repository.InventoryRepository;
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
public class InventoryService implements IInventoryBusiness {

    private final InventoryRepository repository;

    @Override
    @Transactional
    public InventoryDto save(InventoryDto inventoryDto) {
        try {
            System.out.println("inventoryDto.toString() " + inventoryDto.toString());
            Boolean objectExists = false;
            if (inventoryDto.getId() != null) {
                objectExists = repository.existsById(inventoryDto.getId());
            }
            InventoryDto objectDtoVo = new InventoryDto();
            if (!objectExists) {
                Inventory inventoryRepo = new Inventory();
                inventoryRepo.setNombreEquipo(inventoryDto.getNombreEquipo());
                inventoryRepo.setNumeroSerie(inventoryDto.getNumeroSerie());
                inventoryRepo.setNumeroInventario(inventoryDto.getNumeroInventario());
                inventoryRepo.setComentario(inventoryDto.getComentario());
                inventoryRepo.setStatus("ACTIVE");
                inventoryRepo.setIdUsuario(inventoryDto.getIdUsuario());
                inventoryRepo.setIdFabricante(inventoryDto.getIdFabricante());
                inventoryRepo.setIdEstadosDispositivo(inventoryDto.getIdEstadosDispositivo());

                Inventory newObject = repository.save(inventoryRepo);

                BeanUtils.copyProperties(newObject, objectDtoVo);
                return objectDtoVo;
            } else {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El inventario ya existe");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }
    }


    @Override
    @Transactional
    public GenericResponse update(long id, InventoryDto inventoryDto) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("Iniciando método de actualización para Inventory con ID: {} y InventoryDto: {}", id, inventoryDto);

            Boolean objectExists = repository.existsById(id);
            if (!objectExists) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El inventario no existe");
            }

            Inventory inventory = new Inventory();
            BeanUtils.copyProperties(inventoryDto, inventory);
            inventory.setId(id);
            repository.save(inventory);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Inventario actualizado");
        } catch (Exception e) {
            log.error("Se produjo un error al actualizar el inventario: {}", e.getMessage());
            throw new RuntimeException("Error", e);
        }
        return response;
    }


    @Override
    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            Inventory inventory = repository.findById(id).get();
            inventory.setStatus("INACTIVE");
            repository.save(inventory);
            return true;
        } else {
            throw new RuntimeException("El inventario no fue encontrado por el id " + id);
        }
    }





    @Override
    public InventoryDto get(long id) {
        Optional<Inventory> inventoryOptional = repository.findById(id);
        InventoryDto inventoryDto = null;
        if (inventoryOptional.isPresent()) {
            inventoryDto = new InventoryDto();
            BeanUtils.copyProperties(inventoryOptional.get(), inventoryDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El inventario no existe");
        }
        return inventoryDto;
    }

    @Override
    public Page<Inventory> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return repository.getActiveInventory(pagingSort);
    }

    @Override
    public Page<Inventory> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String nombreEquipo = null;
        String numeroSerie = null;
        String numeroInventario = null;
        String comentario = null;
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

        if (customQuery.containsKey("nombreEquipo")) {
            nombreEquipo = "%" + customQuery.get("nombreEquipo") + "%";
        }

        if (customQuery.containsKey("numeroSerie")) {
            numeroSerie = "%" + customQuery.get("numeroSerie") + "%";
        }

        if (customQuery.containsKey("numeroInventario")) {
            numeroInventario = "%" + customQuery.get("numeroInventario") + "%";
        }

        if (customQuery.containsKey("comentario")) {
            comentario = "%" + customQuery.get("comentario") + "%";
        }

        if (customQuery.containsKey("estadoDispositivo")) {
            status = "%" + customQuery.get("estadoDispositivo") + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        log.info("id: " + id);
        log.info("nombreEquipo: " + nombreEquipo);
        log.info("numeroSerie: " + numeroSerie);
        log.info("numeroInventario: " + numeroInventario);
        log.info("comentario: " + comentario);
        log.info("estadoDispositivo: " + status);
        log.info("page: " + page);
        log.info("size: " + size);
        log.info("orders: " + orders);
        log.info("sortBy: " + sortBy);

        Page<Inventory> searchResult = repository.searchInventory(id, nombreEquipo, numeroSerie, numeroInventario, comentario, status, pagingSort);
        log.info("No se encontró el resultado de la búsqueda", searchResult.getContent());
        return searchResult;
    }

    @Override
    public List<Inventory> getAllInventories() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener el inventario");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar el inventario", e);
        }
    }
}
