package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.OrderAllocationDto;
import com.asb.backCompanyService.dto.responde.OrderAllocationResponseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IOrderAllocationBusiness {

    OrderAllocationDto save(OrderAllocationDto orderAllocationDto);

    GenericResponse update(Long id, OrderAllocationDto orderAllocationDto);

    GenericResponse updateStatus(Long id, String statusOrderAllocation);

    GenericResponse uploadOrderImage(Long orderId, MultipartFile image);

    GenericResponse uploadOrderSignature(Long orderId, MultipartFile signature);

    boolean delete(Long id);

    OrderAllocationDto get(Long id);

    Page<OrderAllocationResponseDto> getAll(int page, int size, String orders, String sortBy);

    Page<OrderAllocationResponseDto> getAllComplete(int page, int size, String orders, String sortBy);

    Page<OrderAllocationResponseDto> searchCustom(Map<String, String> customQuery);

    List<OrderAllocationResponseDto> getAllOrderAllocations();

    List<OrderAllocationResponseDto> getOrderAllocationsByTransporter(Long transporterId);

    List<OrderAllocationResponseDto> getOrderComplete(Long transporterId);
}