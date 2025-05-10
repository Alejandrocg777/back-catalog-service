package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IRequestBusiness;
import com.asb.backCompanyService.dto.request.RequestDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Requests;
import com.asb.backCompanyService.repository.RequestsRepository;
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
public class RequestService implements IRequestBusiness {

    private final RequestsRepository repository;

    @Override
    @Transactional
    public RequestDto save(RequestDto requestDto) {
        try {
            log.info("Guardando Request: {}", requestDto);
            Requests request = new Requests();
            request.setRequesterName(requestDto.getRequesterName());
            request.setRequestDate(requestDto.getRequestDate());
            request.setArea(requestDto.getArea());
            request.setDepartment(requestDto.getDepartment());
            request.setRequestNumber(requestDto.getRequestNumber());
            request.setStatus("ACTIVE"); // Establecer como activo

            Requests newRequest = repository.save(request);
            RequestDto savedRequestDto = new RequestDto();
            BeanUtils.copyProperties(newRequest, savedRequestDto);
            return savedRequestDto;

        } catch (Exception e) {
            log.error("Error al guardar Request", e);
            throw new RuntimeException("Error al guardar la solicitud", e);
        }
    }

    @Override
    @Transactional
    public GenericResponse update(Long id, RequestDto requestDto) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("Iniciando actualización para la solicitud con ID: {} y RequestDto: {}", id, requestDto);

            Optional<Requests> optionalRequest = repository.findById(id);
            if (!optionalRequest.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La solicitud no existe");
            }

            Requests request = optionalRequest.get();
            BeanUtils.copyProperties(requestDto, request);
            repository.save(request);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Solicitud actualizada correctamente");
        } catch (Exception e) {
            log.error("Error al actualizar la solicitud: {}", e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error al actualizar la solicitud");
            return response;
        }
        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            Requests request = repository.findById(id).orElseThrow(() ->
                    new RuntimeException("La solicitud no fue encontrada por el ID " + id));
            request.setStatus("INACTIVE"); // Cambiar a inactivo
            repository.save(request);
            return true;
        } else {
            throw new RuntimeException("La solicitud no fue encontrada por el ID " + id);
        }
    }

    @Override
    public RequestDto get(Long id) {
        Optional<Requests> requestOptional = repository.findById(id);
        RequestDto requestDto = null;
        if (requestOptional.isPresent()) {
            requestDto = new RequestDto();
            BeanUtils.copyProperties(requestOptional.get(), requestDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La solicitud no existe");
        }
        return requestDto;
    }

    @Override
    public Page<Requests> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return repository.findAll(pagingSort);
    }

    @Override
    public Page<Requests> searchRequests(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String requesterName = null;
        String area = null;
        String department = null;
        String requestNumber = null;
        String status = null;

        if (customQuery.containsKey("id")) {
            id = "%" + customQuery.get("id") + "%";
        }
        if (customQuery.containsKey("requesterName")) {
            requesterName = "%" + customQuery.get("requesterName") + "%";
        }
        if (customQuery.containsKey("area")) {
            area = "%" + customQuery.get("area") + "%";
        }
        if (customQuery.containsKey("department")) {
            department = "%" + customQuery.get("department") + "%";
        }
        if (customQuery.containsKey("requestNumber")) {
            requestNumber = "%" + customQuery.get("requestNumber") + "%";
        }
        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status") + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        return repository.searchRequests(id, requesterName, area, department, requestNumber, status, pagingSort);
    }

    @Override
    public List<Requests> getAllRequests() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener las solicitudes");
            throw new RuntimeException("No se pueden recuperar las solicitudes", e);
        }
    }
}
