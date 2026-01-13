package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.SupplierRateBusiness;
import com.asb.backCompanyService.dto.request.GeneralRatesDTO;
import com.asb.backCompanyService.dto.request.SupplierRateRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.SupplierRateResponseDTO;
import com.asb.backCompanyService.dto.responde.TerminalResponseDTO;
import com.asb.backCompanyService.model.Supplier;
import com.asb.backCompanyService.model.SupplierRate;
import com.asb.backCompanyService.repository.SupplierRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class SupplierRateService implements SupplierRateBusiness {

    private final SupplierRateRepository supplierRateRepository;

    @Override
    public SupplierRate createRate(SupplierRateRequestDTO createDTO) {

        SupplierRate supplierRate = new SupplierRate();

        supplierRate.setSupplierId(createDTO.getSupplierId());
        supplierRate.setRate(createDTO.getPriceRate());
        supplierRate.setStatus("ACTIVE");
        return supplierRateRepository.save(supplierRate);
    }

    @Override
    public Page<SupplierRateResponseDTO> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return supplierRateRepository.getActiveSuppliers(pagingSort);
    }

    @Override
    public GenericResponse deleteRate(Long id) {
        SupplierRate supplierRate = supplierRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor con ID " + id + " no existe"));

        supplierRate.setStatus("INACTIVE");
        supplierRateRepository.save(supplierRate);
        return new GenericResponse("Tarifa eliminada", 200);
    }

    @Override
    public GenericResponse updateRate(Long id, SupplierRateRequestDTO createDTO) {

        SupplierRate supplierRate = supplierRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor con ID " + id + " no existe"));

        supplierRate.setSupplierId(createDTO.getSupplierId());
        supplierRate.setRate(createDTO.getPriceRate());
         supplierRateRepository.save(supplierRate);

        return new GenericResponse("Tarifa actualizada", 200);

    }

    @Override
    public Page<SupplierRateResponseDTO> search(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String supplierName = null;
        String priceRate = null;

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

        if (customQuery.containsKey("supplierName")) {
            supplierName = "%" + customQuery.get("supplierName") + "%";
        }

        if (customQuery.containsKey("priceRate")) {
            priceRate = "%" + customQuery.get("priceRate") + "%";
        }


        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);


        Page<SupplierRateResponseDTO> searchResult = supplierRateRepository.search(
                id,
                supplierName,
                priceRate,
                pagingSort
        );

        log.info("Search results found: " + searchResult.getTotalElements() + " records");
        log.info("Search results: " + searchResult.getContent());

        return searchResult;
    }

    @Override
    public GenericResponse generateRates(GeneralRatesDTO rate) {

        List<SupplierRate> rates = supplierRateRepository.findAll();

        if (rate.getAddRate() != null){
            for (SupplierRate r : rates) {
                r.setRate(r.getRate() + rate.getAddRate());
                supplierRateRepository.save(r);
            }
        }else {
            for (SupplierRate r : rates) {
                r.setRate(r.getRate() - rate.getSubtractRate());
                supplierRateRepository.save(r);
            }
        }
        return new GenericResponse("Tarifas actualizadas", 200);
    }
}
