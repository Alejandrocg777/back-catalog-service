package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.CashRegisterBusiness;
import com.asb.backCompanyService.dto.request.CashRegisterRequestDTO;
import com.asb.backCompanyService.dto.request.TaxConfigurationRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.CashRegister;
import com.asb.backCompanyService.model.TaxConfiguration;
import com.asb.backCompanyService.repository.CashRegisterRepository;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j

public class CashRegisterService implements CashRegisterBusiness {

    private final CashRegisterRepository cashRegisterRepository;

    @Override
    public CashRegisterRequestDTO save(CashRegisterRequestDTO requestDTO) {

        CashRegister cashRegister = new CashRegister();
        cashRegister.setCashRegisterCode(requestDTO.getCashRegisterCode());
        cashRegister.setStatus(requestDTO.getStatus());
        cashRegister.setDescription(requestDTO.getDescription());
        CashRegister newCashRegister = cashRegisterRepository.save(cashRegister);

        CashRegisterRequestDTO response = new CashRegisterRequestDTO();
        BeanUtils.copyProperties(newCashRegister, response);
        return response;
    }

    @Override
    public GenericResponse update(Long id, CashRegisterRequestDTO requestDTO) {
        if (!cashRegisterRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "tax no existe");

        Optional<CashRegister> cashRegisterOptional = cashRegisterRepository.findById(id);

        CashRegister cashRegister = cashRegisterOptional.get();
        cashRegister.setCashRegisterCode(requestDTO.getCashRegisterCode());
        cashRegister.setDescription(requestDTO.getDescription());
        cashRegister.setStatus(requestDTO.getStatus());
        cashRegisterRepository.save(cashRegister);

        return new GenericResponse("Tax actualizado con exito", 200);
    }

    @Override
    public Boolean delete(Long id) {
        if (cashRegisterRepository.existsById(id)) {
            CashRegister taxConfiguration = cashRegisterRepository.findById(id).get();
            taxConfiguration.setStatus("INACTIVE");
            cashRegisterRepository.save(taxConfiguration);
            return true;
        } else {
            throw new RuntimeException("la caja no fue encontrada por el id " + id);
        }

    }

    @Override
    public Page<CashRegisterRequestDTO> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return cashRegisterRepository.getStatus(pagingSort);
    }

    @Override
    public CashRegisterRequestDTO get(Long id) {
        if (!cashRegisterRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "caja no existe");

        Optional<CashRegister> cashRegisterOptional = cashRegisterRepository.findById(id);

        CashRegisterRequestDTO response = new CashRegisterRequestDTO();
        response.setId(cashRegisterOptional.get().getId());
        response.setCashRegisterCode(cashRegisterOptional.get().getCashRegisterCode());
        response.setStatus(cashRegisterOptional.get().getStatus());
        response.setDescription(cashRegisterOptional.get().getDescription());
        return response;
    }

    @Override
    public List<CashRegister> getAllNoPage() {
        try {
            return (List<CashRegister>) cashRegisterRepository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener la ciudad");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar la ciudad", e);
        }
    }
}
