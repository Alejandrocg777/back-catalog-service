package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.PendingOrderRequestDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.PendingOrderProductDtoResponse;
import com.asb.backCompanyService.dto.responde.PendingOrderResponseDto;
import com.asb.backCompanyService.dto.responde.SupplierProductDtoResponse;
import com.asb.backCompanyService.model.Employee;
import com.asb.backCompanyService.model.PendingOrder;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IPendingOrderBusiness {

    PendingOrderRequestDto save(PendingOrderRequestDto requestDTO);
    GenericResponse update(Long id, PendingOrderRequestDto requestDTO);
    Boolean delete(Long id);
    Page<PendingOrderResponseDto> getAll(int page, int size, String orders, String sortBy);

    Page<PendingOrderProductDtoResponse> getAllProductsByPendingOrder(Long pendingOrderId, int page, int size, String orders, String sortBy);


    PendingOrderRequestDto  get(Long id);
    List<PendingOrderRequestDto> getAllNoPage();
    Page<PendingOrderResponseDto>search(Map<String , String>customQuery);

    Page<PendingOrderProductDtoResponse>searchProductsByPendingOrder(Long pendingOrderId, Map<String , String>customQuery);
}
