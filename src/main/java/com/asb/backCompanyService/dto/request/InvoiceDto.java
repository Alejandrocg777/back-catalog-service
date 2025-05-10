package com.asb.backCompanyService.dto.request;

import com.asb.backCompanyService.dto.SellProducts;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {
    private Long id;
    private String invoiceNumber;
    private Long customerId;
    private Long numerationPrefixId;
    private Double total;
    private List<SellProducts> products;
    private Long sellerId;
    private Long cashRegisterId;
    private Long companyId;
    private String status;

}
