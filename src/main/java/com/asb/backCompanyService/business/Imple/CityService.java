package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.ICityBusiness;
import com.asb.backCompanyService.dto.request.CityDto;
import com.asb.backCompanyService.dto.responde.BranchDtoResponse;
import com.asb.backCompanyService.dto.responde.CityDtoResponse;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.City;
import com.asb.backCompanyService.repository.CityRepository;
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
public class CityService implements ICityBusiness {

    private final CityRepository repository;

        @Override
        @Transactional
        public CityDto save(CityDto cityDto) {
            try {
                System.out.println("cityDto.toString() " + cityDto.toString());
                Boolean objectExists = false;
                if (cityDto.getId() != null) {
                    objectExists = repository.existsById(cityDto.getId());
                }
                CityDto objectDtoVo = new CityDto();
                if (!objectExists) {
                    City cityRepo = new City();
                    cityRepo.setCityCode(cityDto.getCityCode());
                    cityRepo.setCityName(cityDto.getCityName());
                    cityRepo.setDepartmentId(cityDto.getDepartmentId());
                    cityRepo.setStatus("ACTIVE");

                    City newObject = repository.save(cityRepo);

                    BeanUtils.copyProperties(newObject, objectDtoVo);
                    return objectDtoVo;
                } else {
                    if (objectExists) {
                        throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La ciudad ya existe");
                    } else {
                        throw new RuntimeException("Error");
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error al guardar la ciudad", e);
            }
        }

        @Override
        @Transactional
        public GenericResponse update(Long id, CityDto cityDto) {
            GenericResponse response = new GenericResponse();
            try {
                log.info("Iniciando método de actualización para City con ID: {} y CityDto: {}", id, cityDto);

                Optional<City> optionalCity = repository.findById(id);
                if (!optionalCity.isPresent()) {
                    throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La ciudad no existe");
                }

                City city = optionalCity.get();
                BeanUtils.copyProperties(cityDto, city);
                city.setCityCode(cityDto.getCityCode());
                city.setCityName(cityDto.getCityName());
                city.setDepartmentId(cityDto.getDepartmentId());
                repository.save(city);

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
            City city = repository.findById(id).get();
            city.setStatus("INACTIVE");
            repository.save(city);
            return true;
        } else {
            throw new RuntimeException("La ciudad no fue encontrada por el id " + id);
        }
    }

    @Override
    public CityDto get(Long id) {
        Optional<City> cityOptional = repository.findById(id);
        CityDto cityDto = null;
        if (cityOptional.isPresent()) {
            cityDto = new CityDto();
            BeanUtils.copyProperties(cityOptional.get(), cityDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La ciudad no existe");
        }
        return cityDto;
    }

    @Override
    public Page<CityDtoResponse> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return repository.getStatus(pagingSort);
    }


    @Override
    public Page<CityDtoResponse>searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String cityCode = null;
        String cityName = null;
        String status = null;
        String departmentName = null;

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
        if (customQuery.containsKey("cityCode")) {
            cityCode = "%" + customQuery.get("cityCode") + "%";
        }
        if (customQuery.containsKey("departmentName")) {
            departmentName = "%" + customQuery.get("departmentName") + "%";
        }
        if (customQuery.containsKey("cityName")) {
            cityName = "%" + customQuery.get("cityName").toUpperCase() + "%";
        }
        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status").toUpperCase() + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);

        log.info("ID: " + id);
        log.info("City Code: " + cityCode);
        log.info("City Name: " + cityName);
        log.info("department Name: " + departmentName);
        log.info("Status: " + status);
        log.info("Page: " + page);
        log.info("Size: " + size);
        log.info("Orders: " + orders);
        log.info("SortBy: " + sortBy);

        Page<CityDtoResponse> searchResult = repository.searchCity(id, cityCode, cityName, departmentName, status, pagingSort);
        log.info("Search results: " + searchResult.getContent());
        return searchResult;
    }

    @Override
    public List<City> getAllCity() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener la ciudad");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar la ciudad", e);
        }
    }
}
