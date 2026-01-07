package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IRateNeighborhoodBusiness;
import com.asb.backCompanyService.dto.request.GeneralRateNeighborhoodDto;
import com.asb.backCompanyService.dto.request.RateNeighborhoodDto;
import com.asb.backCompanyService.dto.responde.RateNeighborhoodDtoResponse;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.RateNeighborhood;
import com.asb.backCompanyService.repository.RateNeighborhoodRepository;
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
public class RateNeighborhoodService implements IRateNeighborhoodBusiness {

    private final RateNeighborhoodRepository repository;

        @Override
        @Transactional
        public RateNeighborhoodDto save(RateNeighborhoodDto RateNeighborhoodDto) {
            try {
                System.out.println("RateNeighborhoodDto.toString() " + RateNeighborhoodDto.toString());
                Boolean objectExists = false;
                if (RateNeighborhoodDto.getId() != null) {
                    objectExists = repository.existsById(RateNeighborhoodDto.getId());
                }
                RateNeighborhoodDto objectDtoVo = new RateNeighborhoodDto();
                if (!objectExists) {
                    RateNeighborhood RateNeighborhoodRepo = new RateNeighborhood();
                    RateNeighborhoodRepo.setCityId(RateNeighborhoodDto.getCityId());
                    RateNeighborhoodRepo.setDepartmentId(RateNeighborhoodDto.getDepartmentId());
                    RateNeighborhoodRepo.setNeighborhood(RateNeighborhoodDto.getNeighborhood());
                    RateNeighborhoodRepo.setRate(RateNeighborhoodDto.getRate());
                    RateNeighborhoodRepo.setStatus("ACTIVE");

                    RateNeighborhood newObject = repository.save(RateNeighborhoodRepo);

                    BeanUtils.copyProperties(newObject, objectDtoVo);
                    return objectDtoVo;
                } else {
                    if (objectExists) {
                        throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La tarifa por barrio ya existe");
                    } else {
                        throw new RuntimeException("Error");
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error al guardar la tarifa por barrio", e);
            }
        }



    @Override
    @Transactional
    public GeneralRateNeighborhoodDto generateRates(GeneralRateNeighborhoodDto dto) {
        if (dto.getCityId() == null) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El ID de ciudad es obligatorio");
        }

        if (dto.getTypeOperation() == null || dto.getTypeOperation().isEmpty()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El tipo de operación es obligatorio");
        }

        if (dto.getRate() == null || dto.getRate() <= 0) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La tarifa debe ser mayor a 0");
        }

        List<RateNeighborhood> rates = repository.findByCityId(dto.getCityId());

        if (rates.isEmpty()) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST,
                    "No se encontraron tarifas para la ciudad especificada");
        }

        int updatedCount = 0;

        for (RateNeighborhood rate : rates) {
            Double newRate = rate.getRate();

            switch (dto.getTypeOperation().toUpperCase()) {
                case "ADD":
                    newRate = rate.getRate() + dto.getRate();
                    break;

                case "SUBTRACT":
                    newRate = rate.getRate() - dto.getRate();
                    if (newRate < 0) {
                        newRate = 0.0;
                    }
                    break;
                default:
                    throw new CustomErrorException(HttpStatus.BAD_REQUEST,
                            "Tipo de operación inválido. Use: ADD, SUBTRACT.");
            }

            rate.setRate(newRate);

            if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
                rate.setStatus(dto.getStatus());
            }

            repository.save(rate);
            updatedCount++;
        }

        GeneralRateNeighborhoodDto response = new GeneralRateNeighborhoodDto();
        response.setCityId(dto.getCityId());
        response.setDepartmentId(dto.getDepartmentId());
        response.setTypeOperation(dto.getTypeOperation());
        response.setRate(dto.getRate());
        response.setStatus(dto.getStatus());

        return response;
    }

        @Override
        @Transactional
        public GenericResponse update(Long id, RateNeighborhoodDto RateNeighborhoodDto) {
            GenericResponse response = new GenericResponse();
            try {
                log.info("Iniciando método de actualización para RateNeighborhood con ID: {} y RateNeighborhoodDto: {}", id, RateNeighborhoodDto);

                Optional<RateNeighborhood> optionalRateNeighborhood = repository.findById(id);
                if (!optionalRateNeighborhood.isPresent()) {
                    throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La ciudad no existe");
                }

                RateNeighborhood RateNeighborhood = optionalRateNeighborhood.get();
                BeanUtils.copyProperties(RateNeighborhoodDto, RateNeighborhood);
                RateNeighborhood.setCityId(RateNeighborhoodDto.getCityId());
                RateNeighborhood.setDepartmentId(RateNeighborhoodDto.getDepartmentId());
                RateNeighborhood.setNeighborhood(RateNeighborhoodDto.getNeighborhood());
                RateNeighborhood.setRate(RateNeighborhoodDto.getRate());
                repository.save(RateNeighborhood);

                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Ciudad actualizada");
            } catch (Exception e) {
                log.error("Error al actualizar la ciudad: {}", e.getMessage());
                throw new RuntimeException("Error", e);
            }
            return response;
        }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            RateNeighborhood RateNeighborhood = repository.findById(id).get();
            RateNeighborhood.setStatus("INACTIVE");
            repository.save(RateNeighborhood);
            return true;
        } else {
            throw new RuntimeException("La ciudad no fue encontrada por el id " + id);
        }
    }

    @Override
    public RateNeighborhoodDto get(Long id) {
        Optional<RateNeighborhood> RateNeighborhoodOptional = repository.findById(id);
        RateNeighborhoodDto RateNeighborhoodDto = null;
        if (RateNeighborhoodOptional.isPresent()) {
            RateNeighborhoodDto = new RateNeighborhoodDto();
            BeanUtils.copyProperties(RateNeighborhoodOptional.get(), RateNeighborhoodDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La ciudad no existe");
        }
        return RateNeighborhoodDto;
    }

    @Override
    public Page<RateNeighborhoodDtoResponse> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return repository.getStatus(pagingSort);
    }


    @Override
    public Page<RateNeighborhoodDtoResponse>searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String cityName = null;
        String departmentName = null;
        String neighborhood = null;
        String rate = null;
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
        if (customQuery.containsKey("cityName")) {
            cityName = "%" + customQuery.get("cityName") + "%";
        }
        if (customQuery.containsKey("departmentName")) {
            departmentName = "%" + customQuery.get("departmentName") + "%";
        }
        if (customQuery.containsKey("neighborhood")) {
            neighborhood = "%" + customQuery.get("neighborhood").toUpperCase() + "%";
        }
        if (customQuery.containsKey("rate")) {
            rate = "%" + customQuery.get("rate").toUpperCase() + "%";
        }
        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status").toUpperCase() + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);

        log.info("ID: " + id);
        log.info("cityName : " + cityName);
        log.info("department Name: " + departmentName);
        log.info("neighborhood Name: " + neighborhood);
        log.info("rate : " + rate);
        log.info("Status: " + status);
        log.info("Page: " + page);
        log.info("Size: " + size);
        log.info("Orders: " + orders);
        log.info("SortBy: " + sortBy);

        Page<RateNeighborhoodDtoResponse> searchResult = repository.searchRateNeighborhood(id, cityName, departmentName, neighborhood,rate, status, pagingSort);
        log.info("Search results: " + searchResult.getContent());
        return searchResult;
    }

    @Override
    public List<RateNeighborhood> getAllRateNeighborhood() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener la ciudad");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar la ciudad", e);
        }
    }
}
