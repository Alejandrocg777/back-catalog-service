package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.TaxBusiness;
import com.asb.backCompanyService.dto.request.TaxConfigurationRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.TaxConfiguration;
import com.asb.backCompanyService.repository.TaxConfigurationRepository;
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
public class TaxService implements TaxBusiness {

    private final TaxConfigurationRepository taxConfigurationRepository;

    @Override
    public TaxConfigurationRequestDTO saveTaxConfiguration(TaxConfigurationRequestDTO request) {

        TaxConfiguration taxConfiguration = new TaxConfiguration();
        taxConfiguration.setId(request.getId());
        taxConfiguration.setTaxCode(request.getTaxCode());
        taxConfiguration.setTax(request.getTax());
        taxConfiguration.setStatus(request.getStatus());
        taxConfiguration.setType(request.getType());
        taxConfiguration.setConcept(request.getConcept());
        TaxConfiguration newTax = taxConfigurationRepository.save(taxConfiguration);

        TaxConfigurationRequestDTO response = new TaxConfigurationRequestDTO();
        BeanUtils.copyProperties(newTax, response);
        return response;
    }

    public Double calculatePricePlusTax(Long id, Double value) {

        if (!taxConfigurationRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "tax no existe");

        Double finalPrice = 0.0;

        TaxConfiguration tax = taxConfigurationRepository.findById(id).get();

        if(tax.getType().equals("Porcentaje")){
            finalPrice = value + (value * tax.getTax()/100);
        }

        if(tax.getType().equals("Valor")){
            finalPrice = value + tax.getTax();
        }
        return finalPrice;
    }

    @Override
    public GenericResponse update(Long taxId, TaxConfigurationRequestDTO requestDTO) {

        if (!taxConfigurationRepository.existsById(taxId)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "tax no existe");

        Optional<TaxConfiguration> taxOptional = taxConfigurationRepository.findById(taxId);

        TaxConfiguration taxConfiguration = taxOptional.get();
        taxConfiguration.setTaxCode(requestDTO.getTaxCode());
        taxConfiguration.setTax(requestDTO.getTax());
        taxConfiguration.setStatus(requestDTO.getStatus());
        taxConfiguration.setType(requestDTO.getType());
        taxConfiguration.setConcept(requestDTO.getConcept());
        taxConfigurationRepository.save(taxConfiguration);

        return new GenericResponse("Tax actualizado con exito", 200);
    }

    @Override
    public Boolean delete(Long id) {
        if (taxConfigurationRepository.existsById(id)) {
            TaxConfiguration taxConfiguration = taxConfigurationRepository.findById(id).get();
            taxConfiguration.setStatus("INACTIVE");
            taxConfigurationRepository.save(taxConfiguration);
            return true;
        } else {
            throw new RuntimeException("La ciudad no fue encontrada por el id " + id);
        }

    }

    @Override
    public Page<TaxConfiguration> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return taxConfigurationRepository.getStatus(pagingSort);
    }

    @Override
    public TaxConfigurationRequestDTO get(Long id) {

        if (!taxConfigurationRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "tax no existe");

        Optional<TaxConfiguration> taxOptional = taxConfigurationRepository.findById(id);

        TaxConfigurationRequestDTO response = new TaxConfigurationRequestDTO();
        response.setTaxCode(taxOptional.get().getTaxCode());
        response.setTax(taxOptional.get().getTax());
        response.setStatus(taxOptional.get().getStatus());
        response.setConcept(taxOptional.get().getConcept());
        response.setType(taxOptional.get().getType());
        return response;
    }

    @Override
    public List<TaxConfiguration> getAllTax() {
        try {
            return (List<TaxConfiguration>) taxConfigurationRepository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener la ciudad");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar la ciudad", e);
        }
    }


}
