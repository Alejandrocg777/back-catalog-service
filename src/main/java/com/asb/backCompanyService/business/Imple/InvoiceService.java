package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IInvoiceBusiness;
import com.asb.backCompanyService.dto.SellProducts;
import com.asb.backCompanyService.dto.request.DeleteSellProductRequstDto;
import com.asb.backCompanyService.dto.request.DiscountRequestDTO;
import com.asb.backCompanyService.dto.request.InvoiceDto;
import com.asb.backCompanyService.dto.request.TaxConfigurationRequestDTO;
import com.asb.backCompanyService.dto.responde.*;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.*;
import com.asb.backCompanyService.repository.InvoiceRepository;
import com.asb.backCompanyService.repository.NumerationRepository;
import com.asb.backCompanyService.repository.SellProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InvoiceService implements IInvoiceBusiness {

    private final InvoiceRepository repository;
    private final NumerationRepository numerationRepository;
    private final SellProductRepository sellProductRepository;
    private final ProductService productService;
    private final DiscountService discountService;
    private final TaxService taxService;


    @Override
    @Transactional
    public PreliminarInvoice save(InvoiceDto invoiceDto) {
        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber(createInvoiceNumber(numerationRepository.getPrefix(invoiceDto.getNumerationPrefixId()), getCurrentNumber() + 1));

        invoice.setCashRegisterId(invoiceDto.getCashRegisterId());

        invoice.setConsecutive(getCurrentNumber() + 1);

        invoice.setNumerationPrefixId(invoiceDto.getNumerationPrefixId());

        invoice.setStatus("ACTIVE");

        Invoice savedInvoice = repository.save(invoice);

        return new PreliminarInvoice(savedInvoice.getId(), savedInvoice.getCashRegisterId(), savedInvoice.getInvoiceNumber());
    }

    @Override
    public InvoiceDto saveTotal(InvoiceDto invoiceDto) {


        if (!repository.existsById(invoiceDto.getId())) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "factura no existe");
        Invoice invoice = repository.findById(invoiceDto.getId()).get();

        invoice.setCustomerId(invoiceDto.getCustomerId());
        invoice.setSellerId(invoiceDto.getSellerId());
        Double totalInvoice = sellProductRepository.totalSum(invoice.getId());
        invoice.setTotal(totalInvoice);
        repository.save(invoice);

        InvoiceDto savedInvoiceDto = new InvoiceDto();
        BeanUtils.copyProperties(invoice, savedInvoiceDto);

        return savedInvoiceDto;
    }


    private Long getCurrentNumber(){
        Long response = repository.getMaxConsecutive();
        if (response == null) {
            return 0L;
        }
        return response;
    }

    @Override
    public GenericResponse saveProducts(SellProducts product) {

        SellProduct sellProduct = new SellProduct();
        sellProduct.setInvoiceId(product.getInvoiceId());
        sellProduct.setProductId(product.getProductId());
        sellProduct.setQuantity(product.getQuantity());
        Double priceWithDiscount =  productService.getDiscount(product.getProductId(), product.getQuantity());
        Double priceWithTax = productService.getTax(product.getProductId(),priceWithDiscount);
        sellProduct.setTotal(priceWithTax);
        sellProduct.setValue(priceWithDiscount);
        sellProduct.setStatus("ACTIVE");
        sellProductRepository.save(sellProduct);

        return new GenericResponse("Producto guardado exitosamente", HttpStatus.OK.value());
    }

    @Transactional
    @Override
    public void deleteProduct(DeleteSellProductRequstDto delete) {
        sellProductRepository.deleteSellProductByInvoiceIdAndProductId(delete.getInvoiceId(), delete.getProductId());
    }

    @Override
    public SellProductResponseDTO getAllProducts() {

        return null;
    }

    private void setSell(List<SellProducts> products, Long invoiceId){

        products.forEach( sellProduct1 -> {
            SellProduct sellProduct = new SellProduct();
            sellProduct.setInvoiceId(invoiceId);
            sellProduct.setProductId(sellProduct1.getProductId());
            sellProduct.setQuantity(sellProduct1.getQuantity());
            Double priceWithDiscount =  productService.getDiscount(sellProduct1.getProductId(), sellProduct1.getQuantity());
            Double priceWithTax = productService.getTax(sellProduct1.getProductId(),priceWithDiscount);
            sellProduct.setTotal(priceWithTax);
            sellProduct.setValue(priceWithDiscount);
            sellProduct.setStatus("ACTIVE");
            sellProductRepository.save(sellProduct);
        });

    }

    private String createInvoiceNumber(String prefix, Long number){
        LocalDate date = LocalDate.now();
        int year = date.getYear();
        return prefix + "-" + year + "-" + number;
    }

    @Override
    public InvoiceDto update(Long id, InvoiceDto invoiceDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public InvoiceResponseDto get(Long id) {
        return null;
    }

    @Override
    public boolean setStatus(Long id, String status) {
        return false;
    }

    @Override
    public Page<Invoice> searchInvoice(Map<String, String> customQuery) {
        return null;
    }


    public List<Invoice> getAllInvoice() {
        return repository.findByStatus("ACTIVE");
    }
}
