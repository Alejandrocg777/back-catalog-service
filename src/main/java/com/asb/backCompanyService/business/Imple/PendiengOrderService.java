package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IPendingOrderBusiness;
import com.asb.backCompanyService.dto.request.PendingOrderDetailDto;
import com.asb.backCompanyService.dto.request.PendingOrderRequestDto;
import com.asb.backCompanyService.dto.responde.*;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.*;
import com.asb.backCompanyService.repository.PendingOrderDetailRepository;
import com.asb.backCompanyService.repository.PendingOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j

public class PendiengOrderService implements IPendingOrderBusiness {

    private final PendingOrderRepository pendingOrderRepository;
    private final PendingOrderDetailRepository pendingOrderDetailRepository;



    @Override
    @Transactional
    public PendingOrderRequestDto save(PendingOrderRequestDto request) {
        PendingOrder pendingOrder = new PendingOrder();
        pendingOrder.setBillId(request.getBillId());
        pendingOrder.setCustomerId(request.getCustomerId());
        pendingOrder.setAddress(request.getAddress());
        pendingOrder.setPhone(request.getPhone());
        pendingOrder.setObservations(request.getObservations());
        pendingOrder.setTotal(request.getTotal());
        pendingOrder.setStatus("ACTIVE");
        pendingOrder.setStatusPendingOrder("PENDIENTE");

        if (request.getDate() != null && !request.getDate().isEmpty()) {
            pendingOrder.setDate(LocalDate.parse(request.getDate()));
        }

        PendingOrder newPendingOrder = pendingOrderRepository.save(pendingOrder);

        List<PendingOrderDetailDto> savedDetails = new ArrayList<>();

        if (request.getPendingOrderDetails() != null && !request.getPendingOrderDetails().isEmpty()) {
            for (PendingOrderDetailDto detailDto : request.getPendingOrderDetails()) {
                PendingOrderDetails detail = new PendingOrderDetails();
                detail.setPendingOrderId(newPendingOrder.getId());
                detail.setProductId(detailDto.getProductId());
                detail.setQuantity(detailDto.getQuantity());
                detail.setUnitPrice(detailDto.getUnitPrice());
                detail.setTotal(detailDto.getTotal());

                PendingOrderDetails savedDetail = pendingOrderDetailRepository.save(detail);

                PendingOrderDetailDto savedDetailDto = new PendingOrderDetailDto();
                savedDetailDto.setProductId(savedDetail.getProductId());
                savedDetailDto.setQuantity(savedDetail.getQuantity());
                savedDetailDto.setUnitPrice(savedDetail.getUnitPrice());
                savedDetailDto.setTotal(savedDetail.getTotal());

                savedDetails.add(savedDetailDto);
            }
        }

        PendingOrderRequestDto response = new PendingOrderRequestDto();
        BeanUtils.copyProperties(newPendingOrder, response);

        if (newPendingOrder.getDate() != null) {
            response.setDate(newPendingOrder.getDate().toString());
        }

        response.setPendingOrderDetails(savedDetails);

        return response;
    }


    @Override
    @Transactional
    public GenericResponse update(Long id, PendingOrderRequestDto requestDTO) {
        if (!pendingOrderRepository.existsById(id))
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Pedido pendiente no existe");

        PendingOrder pendingOrder = pendingOrderRepository.findById(id).get();

        pendingOrder.setAddress(requestDTO.getAddress());
        pendingOrder.setPhone(requestDTO.getPhone());
        pendingOrder.setObservations(requestDTO.getObservations());
        pendingOrder.setTotal(requestDTO.getTotal());

        if (requestDTO.getDate() != null && !requestDTO.getDate().isEmpty()) {
            pendingOrder.setDate(LocalDate.parse(requestDTO.getDate()));
        }

        pendingOrderRepository.save(pendingOrder);

        if (requestDTO.getPendingOrderDetails() != null && !requestDTO.getPendingOrderDetails().isEmpty()) {
            List<PendingOrderDetails> existingDetails = pendingOrderDetailRepository.findByPendingOrderId(id);
            pendingOrderDetailRepository.deleteAll(existingDetails);

            for (PendingOrderDetailDto detailDto : requestDTO.getPendingOrderDetails()) {
                PendingOrderDetails detail = new PendingOrderDetails();
                detail.setPendingOrderId(id);
                detail.setProductId(detailDto.getProductId());
                detail.setQuantity(detailDto.getQuantity());
                detail.setUnitPrice(detailDto.getUnitPrice());
                detail.setTotal(detailDto.getTotal());

                pendingOrderDetailRepository.save(detail);
            }
        }

        return new GenericResponse("Pedido pendiente actualizado con éxito", 200);
    }

    @Override
    @Transactional
    public Boolean delete(Long id) {
        if (!pendingOrderRepository.existsById(id)) {
            throw new RuntimeException("El pedido pendiente no fue encontrado con el id " + id);
        }

        PendingOrder pendingOrder = pendingOrderRepository.findById(id).get();
        pendingOrder.setStatus("INACTIVE");
        pendingOrderRepository.save(pendingOrder);

        return true;
    }



    @Override
    public Page<PendingOrderResponseDto> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);

        String actualSortField = sortBy;
        if ("cityName".equals(sortBy)) {
            actualSortField = "city.cityName";
        } else if ("customerName".equals(sortBy)) {
            actualSortField = "c.name";
        } else if ("neighborhood".equals(sortBy)) {
            actualSortField = "c.neighborhood";
        }

        Sort sort = Sort.by(direction, actualSortField);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return pendingOrderRepository.getActivePendingOrders(pagingSort);
    }

    @Override
    public Page<PendingOrderProductDtoResponse> getAllProductsByPendingOrder(Long pendingOrderId, int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return pendingOrderRepository.getActiveProductsByPendingOrder(pendingOrderId, pagingSort);
    }


    @Override
    public PendingOrderRequestDto get(Long id) {
        if (!pendingOrderRepository.existsById(id))
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Pedido pendiente no existe");

        PendingOrder pendingOrder = pendingOrderRepository.findById(id).get();

        List<PendingOrderDetails> details = pendingOrderDetailRepository.findByPendingOrderId(id);

        PendingOrderRequestDto response = new PendingOrderRequestDto();
        response.setBillId(pendingOrder.getBillId());
        response.setCustomerId(pendingOrder.getCustomerId());
        response.setAddress(pendingOrder.getAddress());
        response.setPhone(pendingOrder.getPhone());
        response.setObservations(pendingOrder.getObservations());
        response.setTotal(pendingOrder.getTotal());

        if (pendingOrder.getDate() != null) {
            response.setDate(pendingOrder.getDate().toString());
        }


        List<PendingOrderDetailDto> detailDtos = details.stream()
                .map(detail -> {
                    PendingOrderDetailDto dto = new PendingOrderDetailDto();
                    dto.setProductId(detail.getProductId());
                    dto.setQuantity(detail.getQuantity());
                    dto.setUnitPrice(detail.getUnitPrice());
                    dto.setTotal(detail.getTotal());
                    return dto;
                })
                .collect(Collectors.toList());

        response.setPendingOrderDetails(detailDtos);

        return response;
    }

    @Override
    public List<PendingOrderRequestDto> getAllNoPage() {
        try {
            List<Object[]> results = pendingOrderRepository.findActiveAndPendingOrdersData();

            return results.stream().map(row -> {
                PendingOrderRequestDto dto = new PendingOrderRequestDto();
                dto.setId((Long) row[0]);
                dto.setBillId((Long) row[1]);
                dto.setCustomerId((Long) row[2]);
                dto.setCustomerName((String) row[3]);
                dto.setNeighborhood((String) row[4]);
                dto.setCityName((String) row[5]);
                dto.setAddress((String) row[6]);
                dto.setPhone((String) row[7]);
                dto.setObservations((String) row[8]);
                dto.setDate((String) row[9]);
                dto.setTotal((Double) row[10]);
                dto.setStatusOrder((String) row[11]);

                List<PendingOrderDetailDto> details =
                        pendingOrderDetailRepository.findDetailsByOrderId(dto.getId());
                dto.setPendingOrderDetails(details);

                return dto;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error al obtener pedidos pendientes: {}", e.getMessage());
            throw new RuntimeException("No se pueden recuperar los pedidos pendientes", e);
        }
    }

    @Override
    public Page<PendingOrderResponseDto> search(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String customerName = null;
        String neighborhood = null;
        String cityName = null;
        String address = null;
        String phone = null;
        String observations = null;
        String total = null;
        String statusOrder = null;

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
        if (customQuery.containsKey("customerName")) {
            customerName = "%" + customQuery.get("customerName") + "%";
        }
        if (customQuery.containsKey("neighborhood")) {
            neighborhood = "%" + customQuery.get("neighborhood") + "%";
        }
        if (customQuery.containsKey("cityName")) {
            cityName = "%" + customQuery.get("cityName") + "%";
        }
        if (customQuery.containsKey("address")) {
            address = "%" + customQuery.get("address") + "%";
        }
        if (customQuery.containsKey("phone")) {
            phone = "%" + customQuery.get("phone") + "%";
        }
        if (customQuery.containsKey("observations")) {
            observations = "%" + customQuery.get("observations") + "%";
        }
        if (customQuery.containsKey("total")) {
            total = "%" + customQuery.get("total") + "%";
        }
        if (customQuery.containsKey("statusOrder")) {
            statusOrder = "%" + customQuery.get("statusOrder") + "%";
        }

        String actualSortField = sortBy;
        if ("cityName".equals(sortBy)) {
            actualSortField = "city.cityName";
        } else if ("customerName".equals(sortBy)) {
            actualSortField = "c.name";
        } else if ("neighborhood".equals(sortBy)) {
            actualSortField = "c.neighborhood";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, actualSortField);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        Page<PendingOrderResponseDto> searchResult = pendingOrderRepository.searchPendingOrder(
                id, customerName, neighborhood, cityName, address, phone,
                observations, total, statusOrder, pagingSort);

        log.info("Search results: " + searchResult.getContent());
        return searchResult;
    }


    @Override
    public Page<PendingOrderProductDtoResponse> searchProductsByPendingOrder(
            Long pendingOrderId,
            Map<String, String> customQuery) {

        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String productName = null;
        String quantity = null;
        String salePrice = null;
        String total = null;

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

        if (customQuery.containsKey("productName")) {
            productName = "%" + customQuery.get("productName") + "%";
        }

        if (customQuery.containsKey("quantity")) {
            quantity = "%" + customQuery.get("quantity") + "%";
        }

        if (customQuery.containsKey("salePrice")) {
            salePrice = "%" + customQuery.get("salePrice") + "%";
        }

        if (customQuery.containsKey("total")) {
            total = "%" + customQuery.get("total") + "%";
        }

        String actualSortField = sortBy;
        if ("productName".equals(sortBy)) {
            actualSortField = "p.productName";
        } else if ("unitPrice".equals(sortBy)) {
            actualSortField = "op.unitPrice";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, actualSortField);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        Page<PendingOrderProductDtoResponse> searchResult =
                pendingOrderRepository.searchPendingOrderProducts(
                        pendingOrderId, id, productName, quantity, salePrice, total, pagingSort);

        log.info("Search results: " + searchResult.getContent());
        return searchResult;
    }



}
