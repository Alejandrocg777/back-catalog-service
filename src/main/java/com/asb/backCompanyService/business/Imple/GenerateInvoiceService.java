package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IGenerateInvoiceBusiness;
import com.asb.backCompanyService.dto.request.GenerateInvoiceDto;
import com.asb.backCompanyService.dto.request.InvoiceDetailDTO;
import com.asb.backCompanyService.dto.request.InvoiceRequestDTO;
import com.asb.backCompanyService.dto.responde.GenerateInvoiceResponseDto;
import com.asb.backCompanyService.dto.responde.InvoiceResponseDto;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.exception.GenericException;
import com.asb.backCompanyService.model.Bill;
import com.asb.backCompanyService.model.BillDetails;
import com.asb.backCompanyService.model.GenerateInvoice;
import com.asb.backCompanyService.repository.BillDetailsRepopsitory;
import com.asb.backCompanyService.repository.BillRepository;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class GenerateInvoiceService implements IGenerateInvoiceBusiness {

    private final GenerateInvoiceRepository generateInvoiceRepository;
    private final BillRepository billRepository;
    private final BillDetailsRepopsitory billDetailsRepopsitory;
    private final NumerationService numerationService;

    @Override
    @Transactional
    public InvoiceRequestDTO save(InvoiceRequestDTO dto) {
        Bill factura = new Bill();
        factura.setCustomerId(dto.getCustomerId());
        factura.setInvoiceDate(dto.getInvoiceDate());
        factura.setDueDate(dto.getDueDate());
        factura.setPaymentTypeId(dto.getPaymentTypeId());
        factura.setPaymentMethodId(dto.getPaymentMethodId());
        factura.setDeliveryType(dto.getDeliveryType());
        factura.setDeliveryCost(dto.getDeliveryCost());
        factura.setObservations(dto.getObservations());
        factura.setSubtotal(dto.getSubtotal());
        factura.setTotalDiscount(dto.getTotalDiscount());
        factura.setTotal(dto.getTotal());
        factura.setUserId(dto.getUserId());
        factura.setInitialPayment(dto.getInitialPayment());
        factura.setRemainingBalance(dto.getRemainingBalance());
        factura.setCashReceived(dto.getCashReceived());
        factura.setChangeGiven(dto.getChangeGiven());

        String invoiceNumber = numerationService.generateInvoiceNumber(dto.getUserId());
        factura.setInvoiceNumber(invoiceNumber);

        Bill newBill = billRepository.save(factura);

        for (InvoiceDetailDTO detailDto : dto.getInvoiceDetails()) {
            BillDetails detalle = new BillDetails();
            detalle.setFacturaId(newBill.getId());  // Asignar el ID de la factura
            detalle.setProductId(detailDto.getProductId());
            detalle.setQuantity(detailDto.getQuantity());
            detalle.setUnitPrice(detailDto.getUnitPrice());
            detalle.setDiscountPercent(detailDto.getDiscountPercent());
            detalle.setDiscountFixed(detailDto.getDiscountFixed());
            detalle.setTotalDiscount(detailDto.getTotalDiscount());
            detalle.setSubtotal(detailDto.getSubtotal());
            detalle.setTotal(detailDto.getTotal());

            // Guardar cada detalle (asumiendo que tienes un BillDetailsRepository)
            billDetailsRepopsitory.save(detalle);
        }
        return dto;
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
       Bill generateInvoice = billRepository.findById(id).get();

       generateInvoice.setStatus("INCATIVE");

        billRepository.save(generateInvoice);
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
    public Page<InvoiceResponseDto> getAll(int page, int size, String orders, String sortBy) {
        if (page < 0) {
            throw new IllegalArgumentException("El índice de página no debe ser menor que cero");
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Bill> result = billRepository.findAll(pageable);
        return result.map(generateInvoice -> {
            InvoiceResponseDto dto = new InvoiceResponseDto();  // Cambiado a InvoiceResponseDto
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
