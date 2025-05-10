package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IRequestDetailBusiness;
import com.asb.backCompanyService.dto.request.RequestDetailDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.RequestDetails;
import com.asb.backCompanyService.repository.RequestDetailsRepository;
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
public class RequestDetailService implements IRequestDetailBusiness {

    private final RequestDetailsRepository repository;

    @Override
    @Transactional
    public RequestDetailDto save(RequestDetailDto requestDetailDto) {
        try {
            log.info("Guardando RequestDetail: {}", requestDetailDto);
            RequestDetails requestDetail = new RequestDetails();
            requestDetail.setItemCode(requestDetailDto.getItemCode());
            requestDetail.setDescription(requestDetailDto.getDescription());
            requestDetail.setDestination(requestDetailDto.getDestination());
            requestDetail.setItemType(requestDetailDto.getItemType());
            requestDetail.setRequestedQuantity(requestDetailDto.getRequestedQuantity());
            requestDetail.setObservations(requestDetailDto.getObservations());
            requestDetail.setRequestId(requestDetailDto.getRequestId());
            requestDetail.setStatus("ACTIVE"); // Establecer como activo

            RequestDetails newRequestDetail = repository.save(requestDetail);
            RequestDetailDto savedRequestDetailDto = new RequestDetailDto();
            BeanUtils.copyProperties(newRequestDetail, savedRequestDetailDto);
            return savedRequestDetailDto;

        } catch (Exception e) {
            log.error("Error al guardar el detalle de la solicitud", e);
            throw new RuntimeException("Error al guardar el detalle de la solicitud", e);
        }
    }

    @Override
    @Transactional
    public GenericResponse update(Long id, RequestDetailDto requestDetailDto) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("Iniciando actualización para el detalle de la solicitud con ID: {} y RequestDetailDto: {}", id, requestDetailDto);

            if (!repository.existsById(id)) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El detalle de la solicitud no existe");
            }

            RequestDetails requestDetail = repository.findById(id).orElseThrow(() ->
                    new CustomErrorException(HttpStatus.BAD_REQUEST, "El detalle de la solicitud no existe"));

            // Actualiza los campos
            requestDetail.setItemCode(requestDetailDto.getItemCode());
            requestDetail.setDescription(requestDetailDto.getDescription());
            requestDetail.setDestination(requestDetailDto.getDestination());
            requestDetail.setItemType(requestDetailDto.getItemType());
            requestDetail.setRequestedQuantity(requestDetailDto.getRequestedQuantity());
            requestDetail.setObservations(requestDetailDto.getObservations());

            repository.save(requestDetail);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Detalle de solicitud actualizado correctamente");

        } catch (Exception e) {
            log.error("Error al actualizar el detalle de la solicitud", e);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Error al actualizar el detalle de la solicitud");
            return response;
        }
        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            RequestDetails requestDetail = repository.findById(id).orElseThrow(() ->
                    new RuntimeException("El detalle de la solicitud no fue encontrado por el ID " + id));
            requestDetail.setStatus("INACTIVE"); // Cambiar a inactivo
            repository.save(requestDetail);
            return true;
        } else {
            throw new RuntimeException("El detalle de la solicitud no fue encontrado por el ID " + id);
        }
    }

    @Override
    public RequestDetailDto get(Long id) {
        Optional<RequestDetails> requestDetailOptional = repository.findById(id);
        RequestDetailDto requestDetailDto = null;
        if (requestDetailOptional.isPresent()) {
            requestDetailDto = new RequestDetailDto();
            BeanUtils.copyProperties(requestDetailOptional.get(), requestDetailDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El detalle de la solicitud no existe");
        }
        return requestDetailDto;
    }

    @Override
    public Page<RequestDetails> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return repository.findAll(pagingSort);
    }

    @Override
    public Page<RequestDetails> searchRequestDetails(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String itemCode = null;
        String description = null;
        String destination = null;
        String itemType = null;
        String requestedQuantity = null;
        String observations = null;
        String status = null;

        if (customQuery.containsKey("id")) {
            id = "%" + customQuery.get("id") + "%";
        }
        if (customQuery.containsKey("itemCode")) {
            itemCode = "%" + customQuery.get("itemCode") + "%";
        }
        if (customQuery.containsKey("description")) {
            description = "%" + customQuery.get("description") + "%";
        }
        if (customQuery.containsKey("destination")) {
            destination = "%" + customQuery.get("destination") + "%";
        }
        if (customQuery.containsKey("itemType")) {
            itemType = "%" + customQuery.get("itemType") + "%";
        }
        if (customQuery.containsKey("requestedQuantity")) {
            requestedQuantity = customQuery.get("requestedQuantity");
        }
        if (customQuery.containsKey("observations")) {
            observations = "%" + customQuery.get("observations") + "%";
        }
        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status").toUpperCase() + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        return repository.searchRequestDetails(id, itemCode, description, destination, itemType, requestedQuantity, observations, status, pagingSort);
    }

    @Override
    public List<RequestDetails> getAllRequestDetails() {
        return repository.findAll();
    }
}
