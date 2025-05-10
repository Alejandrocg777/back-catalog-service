package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IPaymentMethodBusiness;
import com.asb.backCompanyService.dto.request.PaymentMethodDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.CurrencyType;
import com.asb.backCompanyService.model.PaymentMethod;
import com.asb.backCompanyService.repository.PaymentMethodRepository;
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
public class PaymentMethodService implements IPaymentMethodBusiness {

    private final PaymentMethodRepository repository;

    @Override
    @Transactional
    public PaymentMethodDto save(PaymentMethodDto paymentMethodDto) {
        try {
            log.info("Guardando PaymentMethod: {}", paymentMethodDto);
            boolean objectExists = false;
            if (paymentMethodDto.getId() != null) {
                objectExists = repository.existsById(paymentMethodDto.getId());
            }

            if (!objectExists) {
                PaymentMethod paymentMethodRepo = new PaymentMethod();
                paymentMethodRepo.setDescription(paymentMethodDto.getDescription());
                paymentMethodRepo.setStatus("ACTIVE");

                PaymentMethod newObject = repository.save(paymentMethodRepo);

                PaymentMethodDto objectDtoVo = new PaymentMethodDto();
                BeanUtils.copyProperties(newObject, objectDtoVo);
                return objectDtoVo;
            } else {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El método de pago ya existe");
            }
        } catch (Exception e) {
            log.error("Error al guardar PaymentMethod", e);
            throw new RuntimeException("Error al guardar el método de pago", e);
        }
    }

    @Override
    @Transactional
    public GenericResponse update(Long id, PaymentMethodDto paymentMethodDto) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("Iniciando método de actualización para PaymentMethod con ID: {} y PaymentMethodDto: {}", id, paymentMethodDto);

            Optional<PaymentMethod> optionalPaymentMethod = repository.findById(id);
            if (!optionalPaymentMethod.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El método de pago no existe");
            }

            PaymentMethod paymentMethod = optionalPaymentMethod.get();
            BeanUtils.copyProperties(paymentMethodDto, paymentMethod);
            paymentMethod.setDescription(paymentMethodDto.getDescription());
            repository.save(paymentMethod);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Método de pago actualizado");
        } catch (Exception e) {
            log.error("Error al actualizar el método de pago: {}", e.getMessage());
            throw new RuntimeException("Error", e);
        }
        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            PaymentMethod paymentMethod = repository.findById(id).get();
            paymentMethod.setStatus("INACTIVE");
            repository.save(paymentMethod);
            return true;
        } else {
            throw new RuntimeException("El método de pago no fue encontrado por el id " + id);
        }
    }

    @Override
    public PaymentMethodDto get(Long id) {
        Optional<PaymentMethod> paymentMethodOptional = repository.findById(id);
        PaymentMethodDto paymentMethodDto = null;
        if (paymentMethodOptional.isPresent()) {
            paymentMethodDto = new PaymentMethodDto();
            BeanUtils.copyProperties(paymentMethodOptional.get(), paymentMethodDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "El método de pago no existe");
        }
        return paymentMethodDto;
    }

    @Override
    public Page<PaymentMethod> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return repository.getStatus(pagingSort);
    }

    @Override
    public Page<PaymentMethod> searchPaymentMethod(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "payment_method_id";
        int page = 0;
        int size = 6;
        String id = null;
        String description = null;
        String status = null;

        if (customQuery.containsKey("id")) {
            id = "%" + customQuery.get("id") + "%";
        }
        if (customQuery.containsKey("description")) {
            description = "%" + customQuery.get("description") + "%";
        }
        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status").toUpperCase() + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        log.info("ID: " + id);
        log.info("Description: " + description);
        log.info("Status: " + status);
        log.info("Page: " + page);
        log.info("Size: " + size);
        log.info("Orders: " + orders);
        log.info("SortBy: " + sortBy);

        Page<PaymentMethod> searchResult = repository.searchPaymentMethod(id, description, status, pagingSort);
        log.info("Search results: " + searchResult.getContent());
        return searchResult;
    }

    @Override
    public List<PaymentMethod> getAllPaymentMethod() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener los métodos de pago");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se pueden recuperar los métodos de pago", e);
        }
    }
}
