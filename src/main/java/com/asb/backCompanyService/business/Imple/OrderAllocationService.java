package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IOrderAllocationBusiness;
import com.asb.backCompanyService.dto.request.OrderAllocationDetailDto;
import com.asb.backCompanyService.dto.request.OrderAllocationDto;
import com.asb.backCompanyService.dto.responde.OrderAllocationResponseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.OrderAllocation;
import com.asb.backCompanyService.model.OrderAllocationDetail;
import com.asb.backCompanyService.repository.OrderAllocationDetailRepository;
import com.asb.backCompanyService.repository.OrderAllocationRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class OrderAllocationService implements IOrderAllocationBusiness {

    private final OrderAllocationRepository repository;
    private final OrderAllocationDetailRepository detailRepository;
    private final Cloudinary cloudinary;

    @Override
    @Transactional
    public OrderAllocationDto save(OrderAllocationDto dto) {
        try {
            log.info("📥 DTO recibido del frontend: {}", dto);

            if (dto.getId() != null && repository.existsById(dto.getId())) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La orden de asignación ya existe");
            }

            // Mapear del DTO del frontend a la entidad
            OrderAllocation orderAllocation = new OrderAllocation();
            orderAllocation.setTransporterId(dto.getUserId());
            orderAllocation.setPendingOrderId(dto.getOrderId());
            orderAllocation.setOriginWarehouseId(dto.getWarehouseId());
            orderAllocation.setDestinationNeighborhoodId(dto.getNeighborhoodRateId());

            // Parsear fecha y hora de forma segura
            orderAllocation.setDate(parseDate(String.valueOf(dto.getDate())));
            orderAllocation.setHour(parseHourSafely(String.valueOf(dto.getHour())));

            orderAllocation.setChargeInvoice(dto.getChargeInvoice());
            orderAllocation.setAddress(dto.getAddress() != null ? dto.getAddress() : dto.getCustomerAddress());
            orderAllocation.setPhone(dto.getCustomerPhone());
            orderAllocation.setObservations(dto.getObservation());
            orderAllocation.setTotal(dto.getTotalPurchase());
            orderAllocation.setStatus("ACTIVE");
            orderAllocation.setStatusOrderAllocation("ASIGNADO");
            orderAllocation.setCreatedAt(LocalDateTime.now());
            orderAllocation.setUpdatedAt(LocalDateTime.now());

            OrderAllocation savedOrder = repository.save(orderAllocation);
            log.info("✅ OrderAllocation guardada con ID: {}", savedOrder.getId());

            // Guardar detalles
            if (dto.getProducts() != null && !dto.getProducts().isEmpty()) {
                List<OrderAllocationDetail> details = new ArrayList<>();

                for (OrderAllocationDetailDto productDto : dto.getProducts()) {
                    if (productDto.getQuantityPerTrip() != null && productDto.getQuantityPerTrip() > 0) {

                        if (productDto.getPendingOrderDetailId() == null) {
                            log.error("❌ Producto sin pendingOrderDetailId: {}", productDto);
                            throw new CustomErrorException(HttpStatus.BAD_REQUEST,
                                    "El producto " + productDto.getProductName() + " no tiene ID de detalle del pedido");
                        }

                        OrderAllocationDetail detail = new OrderAllocationDetail();
                        detail.setOrderAllocationId(savedOrder.getId());
                        detail.setPendingOrderDetailId(productDto.getPendingOrderDetailId());
                        detail.setProductId(productDto.getId());
                        detail.setAssignedQuantity(productDto.getQuantityPerTrip());
                        detail.setUnitPrice(productDto.getPrice());
                        detail.setTotal(productDto.getPrice() * productDto.getQuantityPerTrip());
                        detail.setCreatedAt(LocalDateTime.now());
                        detail.setUpdatedAt(LocalDateTime.now());
                        details.add(detail);
                    }
                }

                if (!details.isEmpty()) {
                    detailRepository.saveAll(details);
                    log.info("✅ Guardados {} detalles", details.size());
                }
            }

            // Retornar el DTO
            dto.setId(savedOrder.getId());
            return dto;

        } catch (CustomErrorException e) {
            log.error("❌ Error de validación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ Error al guardar: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar la orden de asignación", e);
        }
    }

    @Override
    @Transactional
    public GenericResponse update(Long id, OrderAllocationDto dto) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("📥 Actualizando ID: {} con DTO: {}", id, dto);

            Optional<OrderAllocation> optionalOrderAllocation = repository.findById(id);
            if (!optionalOrderAllocation.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La orden de asignación no existe");
            }

            OrderAllocation orderAllocation = optionalOrderAllocation.get();

            // Mapear del DTO del frontend a la entidad
            orderAllocation.setTransporterId(dto.getUserId());
            orderAllocation.setPendingOrderId(dto.getOrderId());
            orderAllocation.setOriginWarehouseId(dto.getWarehouseId());
            orderAllocation.setDestinationNeighborhoodId(dto.getNeighborhoodRateId());
            orderAllocation.setDate(parseDate(String.valueOf(dto.getDate())));
            orderAllocation.setHour(parseHourSafely(String.valueOf(dto.getHour())));
            orderAllocation.setChargeInvoice(dto.getChargeInvoice());
            orderAllocation.setAddress(dto.getAddress() != null ? dto.getAddress() : dto.getCustomerAddress());
            orderAllocation.setPhone(dto.getCustomerPhone());
            orderAllocation.setObservations(dto.getObservation());
            orderAllocation.setTotal(dto.getTotalPurchase());

            if (dto.getStatus() != null) {
                orderAllocation.setStatus(dto.getStatus());
            }
            if (dto.getStatusOrder() != null) {
                orderAllocation.setStatusOrderAllocation(dto.getStatusOrder());
            }

            orderAllocation.setUpdatedAt(LocalDateTime.now());
            repository.save(orderAllocation);

            // Actualizar detalles
            detailRepository.deleteByOrderAllocationId(id);

            if (dto.getProducts() != null && !dto.getProducts().isEmpty()) {
                List<OrderAllocationDetail> details = new ArrayList<>();

                for (OrderAllocationDetailDto productDto : dto.getProducts()) {
                    if (productDto.getQuantityPerTrip() != null && productDto.getQuantityPerTrip() > 0) {
                        OrderAllocationDetail detail = new OrderAllocationDetail();
                        detail.setOrderAllocationId(id);
                        detail.setPendingOrderDetailId(productDto.getPendingOrderDetailId());
                        detail.setProductId(productDto.getId());
                        detail.setAssignedQuantity(productDto.getQuantityPerTrip());
                        detail.setUnitPrice(productDto.getPrice());
                        detail.setTotal(productDto.getPrice() * productDto.getQuantityPerTrip());
                        detail.setCreatedAt(LocalDateTime.now());
                        detail.setUpdatedAt(LocalDateTime.now());
                        details.add(detail);
                    }
                }

                if (!details.isEmpty()) {
                    detailRepository.saveAll(details);
                    log.info("✅ Actualizados {} detalles", details.size());
                }
            }

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Orden de asignación actualizada correctamente");

        } catch (CustomErrorException e) {
            log.error("❌ Error de validación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ Error al actualizar: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar la orden de asignación", e);
        }
        return response;
    }


    @Override
    @Transactional
    public GenericResponse updateStatus(Long id, String statusOrderAllocation) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("📥 Actualizando estado de orden ID: {} a: {}", id, statusOrderAllocation);

            Optional<OrderAllocation> optionalOrderAllocation = repository.findById(id);
            if (!optionalOrderAllocation.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La orden de asignación no existe");
            }

            OrderAllocation orderAllocation = optionalOrderAllocation.get();

            // Solo actualizar el estado
            orderAllocation.setStatusOrderAllocation(statusOrderAllocation);
            orderAllocation.setUpdatedAt(LocalDateTime.now());

            repository.save(orderAllocation);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Estado actualizado correctamente a: " + statusOrderAllocation);

            log.info("✅ Estado actualizado exitosamente");

        } catch (CustomErrorException e) {
            log.error("❌ Error de validación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ Error al actualizar estado: {}", e.getMessage(), e);
            throw new RuntimeException("Error al actualizar el estado de la orden", e);
        }
        return response;
    }

    @Override
    @Transactional
    public GenericResponse uploadOrderImage(Long orderId, MultipartFile image) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("📤 Subiendo imagen para orden ID: {}", orderId);

            // Verificar que la orden existe
            Optional<OrderAllocation> optionalOrderAllocation = repository.findById(orderId);
            if (!optionalOrderAllocation.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La orden de asignación no existe");
            }

            // Verificar que se envió una imagen
            if (image == null || image.isEmpty()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "No se proporcionó ninguna imagen");
            }

            OrderAllocation orderAllocation = optionalOrderAllocation.get();

            // ✅ Subir imagen a Cloudinary (simple, sin configuración de carpetas)
            Map uploadResult = cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto")
            );
            String imageUrl = (String) uploadResult.get("url");

            log.info("✅ Imagen subida exitosamente: {}", imageUrl);

            // Guardar la URL en la base de datos
            orderAllocation.setImage(imageUrl);
            orderAllocation.setUpdatedAt(LocalDateTime.now());
            repository.save(orderAllocation);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Imagen cargada correctamente");

            log.info("✅ URL de imagen guardada en BD");

        } catch (CustomErrorException e) {
            log.error("❌ Error de validación: {}", e.getMessage());
            throw e;
        } catch (IOException e) {
            log.error("❌ Error al subir imagen a Cloudinary: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar la imagen", e);
        } catch (Exception e) {
            log.error("❌ Error al cargar imagen: {}", e.getMessage(), e);
            throw new RuntimeException("Error al cargar la imagen de la orden", e);
        }
        return response;
    }

    @Override
    @Transactional
    public GenericResponse uploadOrderSignature(Long orderId, MultipartFile signature) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("📤 Subiendo firma para orden ID: {}", orderId);

            Optional<OrderAllocation> optionalOrderAllocation = repository.findById(orderId);
            if (!optionalOrderAllocation.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La orden de asignación no existe");
            }

            if (signature == null || signature.isEmpty()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "No se proporcionó ninguna imagen");
            }

            OrderAllocation orderAllocation = optionalOrderAllocation.get();

            Map uploadResult = cloudinary.uploader().upload(
                    signature.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto")
            );
            String signatureUrl = (String) uploadResult.get("url");

            log.info("✅ firma subida exitosamente: {}", signatureUrl);

            orderAllocation.setSignature(signatureUrl);
            orderAllocation.setUpdatedAt(LocalDateTime.now());
            repository.save(orderAllocation);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Firma cargada correctamente");

            log.info("✅ URL de firma guardada en BD");

        } catch (CustomErrorException e) {
            log.error("❌ Error de validación: {}", e.getMessage());
            throw e;
        } catch (IOException e) {
            log.error("❌ Error al subir firma a Cloudinary: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar la firma", e);
        } catch (Exception e) {
            log.error("❌ Error al cargar firma: {}", e.getMessage(), e);
            throw new RuntimeException("Error al cargar la firma de la orden", e);
        }
        return response;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        try {
            if (!repository.existsById(id)) {
                throw new RuntimeException("La orden de asignación no fue encontrada con el id " + id);
            }

            OrderAllocation orderAllocation = repository.findById(id).get();
            orderAllocation.setStatus("INACTIVE");
            orderAllocation.setUpdatedAt(LocalDateTime.now());
            repository.save(orderAllocation);

            log.info("✅ OrderAllocation ID: {} marcada como INACTIVE", id);
            return true;

        } catch (Exception e) {
            log.error("❌ Error al eliminar ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public OrderAllocationDto get(Long id) {
        try {
            Optional<OrderAllocation> orderAllocationOptional = repository.findById(id);

            if (!orderAllocationOptional.isPresent()) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La orden de asignación no existe");
            }

            OrderAllocation oa = orderAllocationOptional.get();

            // Mapear de entidad a DTO del frontend
            OrderAllocationDto dto = new OrderAllocationDto();
            dto.setId(oa.getId());
            dto.setUserId(oa.getTransporterId());
            dto.setWarehouseId(oa.getOriginWarehouseId());
            dto.setNeighborhoodRateId(oa.getDestinationNeighborhoodId());
            dto.setOrderId(oa.getPendingOrderId());
            dto.setDate(oa.getDate());
            dto.setHour(oa.getHour());
            dto.setChargeInvoice(oa.getChargeInvoice());
            dto.setAddress(oa.getAddress());
            dto.setCustomerPhone(oa.getPhone());
            dto.setObservation(oa.getObservations());
            dto.setTotalPurchase(oa.getTotal());
            dto.setStatus(oa.getStatus());
            dto.setStatusOrder(oa.getStatusOrderAllocation());

            // Cargar productos
            List<OrderAllocationDetailDto> products = detailRepository.findDetailsByOrderAllocationId(id);
            dto.setProducts(products);

            return dto;

        } catch (CustomErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error("❌ Error al obtener ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al obtener la orden de asignación", e);
        }
    }

    @Override
    public Page<OrderAllocationResponseDto> getAll(int page, int size, String orders, String sortBy) {
        try {
            String actualSortField = sortBy;

            switch (sortBy) {
                case "customerName":
                    actualSortField = "c.name";
                    break;
                case "paymentMethodName":
                    actualSortField = "m.description";
                    break;
            }

            Sort.Direction direction = Sort.Direction.fromString(orders);
            Sort sort = Sort.by(direction, actualSortField);
            Pageable pagingSort = PageRequest.of(page, size, sort);

            Page<OrderAllocationResponseDto> ordersPage = repository.getActiveOrders(pagingSort);

            ordersPage.forEach(order -> {
                List<OrderAllocationDetailDto> products = detailRepository.findDetailsByOrderAllocationId(order.getId());
                order.setProducts(products);
            });

            return ordersPage;

        } catch (Exception e) {
            log.error("❌ Error al obtener todas las órdenes: {}", e.getMessage());
            throw new RuntimeException("Error al obtener las órdenes de asignación", e);
        }
    }


    @Override
    public Page<OrderAllocationResponseDto> getAllComplete(int page, int size, String orders, String sortBy) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(orders);
            Sort sort = Sort.by(direction, sortBy);
            Pageable pagingSort = PageRequest.of(page, size, sort);

            Page<OrderAllocationResponseDto> ordersPage = repository.getActiveOrderComplete(pagingSort);

            ordersPage.forEach(order -> {
                List<OrderAllocationDetailDto> products = detailRepository.findDetailsByOrderAllocationId(order.getId());
                order.setProducts(products);
            });

            return ordersPage;

        } catch (Exception e) {
            log.error("❌ Error al obtener todas las órdenes: {}", e.getMessage());
            throw new RuntimeException("Error al obtener las órdenes de asignación", e);
        }
    }


    @Override
    public Page<OrderAllocationResponseDto> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = "%";
        String userName = "%";
        String warehouseName = "%";
        String neighborhoodName = "%";
        String address = "%";
        String paymentMethodName = "%";
        LocalDate date = null;
        String customerName = "%";
        String statusOrder = "%";

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

        if (customQuery.containsKey("id") && !customQuery.get("id").isEmpty()) {
            id = "%" + customQuery.get("id") + "%";
        }

        if (customQuery.containsKey("userName") && !customQuery.get("userName").isEmpty()) {
            userName = "%" + customQuery.get("userName") + "%";
        }

        if (customQuery.containsKey("warehouseName") && !customQuery.get("warehouseName").isEmpty()) {
            warehouseName = "%" + customQuery.get("warehouseName") + "%";
        }

        if (customQuery.containsKey("neighborhoodName") && !customQuery.get("neighborhoodName").isEmpty()) {
            neighborhoodName = "%" + customQuery.get("neighborhoodName") + "%";
        }

        if (customQuery.containsKey("address") && !customQuery.get("address").isEmpty()) {
            address = "%" + customQuery.get("address") + "%";
        }

        if (customQuery.containsKey("paymentMethodName") && !customQuery.get("paymentMethodName").isEmpty()) {
            paymentMethodName = "%" + customQuery.get("paymentMethodName") + "%";
        }

        if (customQuery.containsKey("date") && !customQuery.get("date").isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                date = LocalDate.parse(customQuery.get("date"), formatter);
            } catch (Exception e) {
                log.warn("Invalid date format: " + customQuery.get("date") + ". Expected format: yyyy-MM-dd");
            }
        }

        if (customQuery.containsKey("customerName") && !customQuery.get("customerName").isEmpty()) {
            customerName = "%" + customQuery.get("customerName") + "%";
        }

        if (customQuery.containsKey("statusOrder") && !customQuery.get("statusOrder").isEmpty()) {
            statusOrder = "%" + customQuery.get("statusOrder") + "%";
        }

        // Mapeo de campos de ordenamiento
        String actualSortField = sortBy;
        if ("userName".equals(sortBy)) {
            actualSortField = "u.name";
        } else if ("warehouseName".equals(sortBy)) {
            actualSortField = "w.warehouseName";
        } else if ("neighborhoodName".equals(sortBy)) {
            actualSortField = "rn.neighborhood";
        } else if ("customerName".equals(sortBy)) {
            actualSortField = "c.name";
        } else if ("paymentMethodName".equals(sortBy)) {
            actualSortField = "m.description";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, actualSortField);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        Page<OrderAllocationResponseDto> searchResult = repository.searchOrderAllocation(
                id, userName, warehouseName, neighborhoodName,
                address, paymentMethodName, date, customerName, statusOrder, pagingSort);

        log.info("Search results: " + searchResult.getContent());
        return searchResult;
    }


    @Override
    public List<OrderAllocationResponseDto> getAllOrderAllocations() {
        try {
            List<OrderAllocationResponseDto> orders = repository.getAllOrderAllocations();

            orders.forEach(order -> {
                List<OrderAllocationDetailDto> products = detailRepository.findDetailsByOrderAllocationId(order.getId());
                order.setProducts(products);
            });

            return orders;

        } catch (Exception e) {
            log.error("❌ Error al obtener órdenes: {}", e.getMessage());
            throw new RuntimeException("No se pueden recuperar las órdenes de asignación", e);
        }
    }

    @Override
    public List<OrderAllocationResponseDto> getOrderAllocationsByTransporter(Long transporterId) {
        try {
            List<OrderAllocationResponseDto> orders =
                    repository.getOrderAllocationsByTransporter(transporterId);

            orders.forEach(order -> {
                List<OrderAllocationDetailDto> products =
                        detailRepository.findDetailsByOrderAllocationId(order.getId());
                order.setProducts(products);
            });

            return orders;

        } catch (Exception e) {
            log.error("❌ Error al obtener órdenes del transportador {}: {}",
                    transporterId, e.getMessage());
            throw new RuntimeException("No se pueden recuperar las órdenes de asignación", e);
        }
    }


    @Override
    public List<OrderAllocationResponseDto> getOrderComplete(Long transporterId) {
        try {
            List<OrderAllocationResponseDto> orders =
                    repository.getOrderComplete(transporterId);

            orders.forEach(order -> {
                List<OrderAllocationDetailDto> products =
                        detailRepository.findDetailsByOrderAllocationId(order.getId());
                order.setProducts(products);
            });

            return orders;

        } catch (Exception e) {
            log.error("❌ Error al obtener órdenes del transportador {}: {}",
                    transporterId, e.getMessage());
            throw new RuntimeException("No se pueden recuperar las órdenes de asignación", e);
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Parsea una fecha de forma segura manejando diferentes formatos
     */
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty() || dateStr.equals("null")) {
            return null;
        }
        try {
            // Si viene con hora: "2026-01-20T14:30:00"
            if (dateStr.contains("T")) {
                return LocalDate.parse(dateStr.split("T")[0]);
            }
            // Si viene solo fecha: "2026-01-20"
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            log.error("❌ Error al parsear fecha: {}", dateStr, e);
            return null;
        }
    }

    /**
     * Parsea una hora de forma segura manejando valores null o vacíos
     */
    private LocalTime parseHourSafely(String hourString) {
        if (hourString == null || hourString.trim().isEmpty() || hourString.equals("null")) {
            return null;
        }
        try {
            // Si viene con fecha y hora: "2026-01-21T21:20:00"
            if (hourString.contains("T")) {
                return LocalTime.parse(hourString.split("T")[1]);
            }
            // Si viene solo hora: "21:20:00" o "21:20"
            return LocalTime.parse(hourString);
        } catch (DateTimeParseException e) {
            log.warn("⚠️ No se pudo parsear la hora: {}", hourString);
            return null;
        }
    }
}