package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IGenerateInvoiceBusiness;
import com.asb.backCompanyService.dto.request.GenerateInvoiceDto;
import com.asb.backCompanyService.dto.responde.GenerateInvoiceResponseDto;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.exception.GenericException;
import com.asb.backCompanyService.model.GenerateInvoice;
import com.asb.backCompanyService.repository.GenerateInvoiceRepository;
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

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class GenerateInvoiceService implements IGenerateInvoiceBusiness {

    private final GenerateInvoiceRepository generateInvoiceRepository;

    @Override
    @Transactional
    public GenerateInvoiceDto save(GenerateInvoiceDto generateInvoiceDto) {
        if (generateInvoiceDto.getId() != null && generateInvoiceRepository.existsById(generateInvoiceDto.getId())) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La factura ya existe");
        }

        GenerateInvoice generateInvoice = new GenerateInvoice();
        generateInvoice.setResolutionNumber(generateInvoiceDto.getResolutionNumber());
        generateInvoice.setBillingType(generateInvoiceDto.getBillingType());
        generateInvoice.setAuthorizedEnabled(generateInvoiceDto.getAuthorizedEnabled());
        generateInvoice.setResolutionDate(generateInvoiceDto.getResolutionDate());
        generateInvoice.setCashRegisterName(generateInvoiceDto.getCashRegisterName());

        GenerateInvoice savedGenerateInvoice = generateInvoiceRepository.save(generateInvoice);
        GenerateInvoiceDto savedGenerateInvoiceDto = new GenerateInvoiceDto();
        BeanUtils.copyProperties(savedGenerateInvoice, savedGenerateInvoiceDto);
        return savedGenerateInvoiceDto;
    }

    @Override
    @Transactional
    public GenerateInvoiceDto update(Long id, GenerateInvoiceDto generateInvoiceDto) {
        GenerateInvoice generateInvoice = generateInvoiceRepository.findById(id)
                .orElseThrow(() -> new GenericException("La factura no fue encontrada por el id " + id, HttpStatus.NOT_FOUND));

        generateInvoice.setResolutionNumber(generateInvoiceDto.getResolutionNumber());
        generateInvoice.setBillingType(generateInvoiceDto.getBillingType());
        generateInvoice.setAuthorizedEnabled(generateInvoiceDto.getAuthorizedEnabled());
        generateInvoice.setResolutionDate(generateInvoiceDto.getResolutionDate());
        generateInvoice.setCashRegisterName(generateInvoiceDto.getCashRegisterName());

        GenerateInvoice updatedGenerateInvoice = generateInvoiceRepository.save(generateInvoice);
        GenerateInvoiceDto updatedGenerateInvoiceDto = new GenerateInvoiceDto();
        BeanUtils.copyProperties(updatedGenerateInvoice, updatedGenerateInvoiceDto);
        return updatedGenerateInvoiceDto;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        GenerateInvoice generateInvoice = generateInvoiceRepository.findById(id)
                .orElseThrow(() -> new GenericException("La factura no fue encontrada por el id " + id, HttpStatus.NOT_FOUND));

        generateInvoiceRepository.delete(generateInvoice);
        return true;
    }

    @Override
    public GenerateInvoiceResponseDto get(Long id) {
        GenerateInvoice generateInvoice = generateInvoiceRepository.findById(id)
                .orElseThrow(() -> new GenericException("No existe la factura con id " + id, HttpStatus.NOT_FOUND));

        GenerateInvoiceResponseDto responseDto = new GenerateInvoiceResponseDto();
        BeanUtils.copyProperties(generateInvoice, responseDto);
        return responseDto;
    }

    @Override
    @Transactional
    public boolean setStatus(Long id, String status) {
        GenerateInvoice generateInvoice = generateInvoiceRepository.findById(id)
                .orElseThrow(() -> new GenericException("La factura no fue encontrada por el id " + id, HttpStatus.NOT_FOUND));

        generateInvoice.setAuthorizedEnabled(status);
        generateInvoiceRepository.save(generateInvoice);
        return true;
    }

    @Override
    public Page<GenerateInvoiceResponseDto> getAll(int page, int size, String orders, String sortBy) {
        if (page < 0) {
            throw new IllegalArgumentException("El índice de página no debe ser menor que cero");
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<GenerateInvoice> result = generateInvoiceRepository.findAll(pageable);
        return result.map(generateInvoice -> {
            GenerateInvoiceResponseDto dto = new GenerateInvoiceResponseDto();
            BeanUtils.copyProperties(generateInvoice, dto);
            return dto;
        });
    }

    @Override
    public Page<GenerateInvoice> searchGenerateInvoice(Map<String, String> customQuery) {
        return null;
    }

    @Override
    public List<GenerateInvoice> getAllGenerateInvoices() {
        return generateInvoiceRepository.findAll();
    }
}
