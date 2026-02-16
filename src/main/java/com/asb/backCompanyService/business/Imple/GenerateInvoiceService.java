package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IGenerateInvoiceBusiness;
import com.asb.backCompanyService.business.Interfaces.IPendingOrderBusiness;
import com.asb.backCompanyService.dto.request.GenerateInvoiceDto;
import com.asb.backCompanyService.dto.request.InvoiceDetailDTO;
import com.asb.backCompanyService.dto.request.InvoiceRequestDTO;
import com.asb.backCompanyService.dto.request.PendingOrderDetailDto;
import com.asb.backCompanyService.dto.request.PendingOrderRequestDto;
import com.asb.backCompanyService.dto.responde.GenerateInvoiceResponseDto;
import com.asb.backCompanyService.dto.responde.InvoiceResponseDto;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.exception.GenericException;
import com.asb.backCompanyService.model.*;
import com.asb.backCompanyService.repository.*;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class GenerateInvoiceService implements IGenerateInvoiceBusiness {

    private final GenerateInvoiceRepository generateInvoiceRepository;
    private final BillRepository billRepository;
    private final ClientRepository clientRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;
    private final BillDetailsRepopsitory billDetailsRepopsitory;
    private final NumerationService numerationService;
    private final ProductRepository productRepository;
    private final TransactionsService transactionsService;
    private final TransactionProductRepository transactionProductRepository;
    private final IPendingOrderBusiness pendingOrderBusiness;

    @Override
    @Transactional
    public InvoiceRequestDTO save(InvoiceRequestDTO dto) {
        String billStatus = calculateBillStatus(dto.getTotal(), dto.getInitialPayment(), dto.getDueDate());

        if (dto.getStatusBill() != null && dto.getStatusBill().equalsIgnoreCase("COTIZACION")) {
            billStatus = "COTIZACION";
        }
        if (!"COTIZACION".equalsIgnoreCase(billStatus)) {
            validateAndCheckInventory(dto.getInvoiceDetails());
        }

        Bill factura = new Bill();
        factura.setCustomerId(dto.getCustomerId());
        factura.setAddress(dto.getAddress());
        factura.setPhone(dto.getPhone());
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
        factura.setStatus("ACTIVE");
        factura.setStatusBill(billStatus);

        Bill newBill = billRepository.save(factura);

        Transaction transaction = null;
        if (!"COTIZACION".equalsIgnoreCase(billStatus)) {
            String dateStr = dto.getInvoiceDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String observation = "Factura #" + invoiceNumber +
                    (dto.getObservations() != null && !dto.getObservations().isEmpty()
                            ? " - " + dto.getObservations()
                            : "");

            transaction = transactionsService.insertTransaction(
                    "SALIDA",
                    dto.getTotal(),
                    dto.getUserId(),
                    dateStr,
                    observation,
                    "ACTIVE"
            );

            log.info("Transacción creada - ID: {} para factura {}", transaction.getId(), invoiceNumber);
        }

        for (InvoiceDetailDTO detailDto : dto.getInvoiceDetails()) {
            BillDetails detalle = new BillDetails();
            detalle.setFacturaId(newBill.getId());
            detalle.setProductId(detailDto.getProductId());
            detalle.setQuantity(detailDto.getQuantity());
            detalle.setUnitPrice(detailDto.getUnitPrice());
            detalle.setDiscountPercent(detailDto.getDiscountPercent());
            detalle.setDiscountFixed(detailDto.getDiscountFixed());
            detalle.setTotalDiscount(detailDto.getTotalDiscount());
            detalle.setSubtotal(detailDto.getSubtotal());
            detalle.setTotal(detailDto.getTotal());

            billDetailsRepopsitory.save(detalle);

            if (transaction != null) {
                TransactionProduct tp = new TransactionProduct();
                tp.setTransactionId(transaction.getId());
                tp.setProductId(detailDto.getProductId());
                tp.setPurchasePrice(detailDto.getUnitPrice());
                tp.setTotal(detailDto.getTotal());
                tp.setQuantity(Long.valueOf(detailDto.getQuantity()));
                transactionProductRepository.save(tp);
            }
        }

        if (!"COTIZACION".equalsIgnoreCase(billStatus)) {
            updateInventory(dto.getInvoiceDetails());
            log.info("Inventario actualizado para factura {} - Tipo: {}", invoiceNumber, billStatus);
        } else {
            log.info("Cotización creada {} - NO se afectó el inventario ni se creó transacción", invoiceNumber);
        }

        if ("LLEVAR".equalsIgnoreCase(dto.getDeliveryType()) && !"COTIZACION".equalsIgnoreCase(billStatus)) {
            createPendingOrderFromInvoice(newBill, dto);
            log.info("Pedido pendiente creado automáticamente para factura {} - Delivery Type: LLEVAR - Estado: {}",
                    invoiceNumber, billStatus);
        } else if ("LLEVAR".equalsIgnoreCase(dto.getDeliveryType()) && "COTIZACION".equalsIgnoreCase(billStatus)) {
            log.info("NO se creó pedido pendiente para factura {} - Es una COTIZACIÓN", invoiceNumber);
        }
        dto.setInvoiceNumber(invoiceNumber);
        dto.setBillId(newBill.getId());

        log.info("Factura creada - ID: {}, Número: {}, Estado: {}, Delivery: {}",
                newBill.getId(), invoiceNumber, billStatus, dto.getDeliveryType());

        return dto;
    }

    private void createPendingOrderFromInvoice(Bill bill, InvoiceRequestDTO invoiceDto) {
        try {
            PendingOrderRequestDto pendingOrderDto = new PendingOrderRequestDto();
            pendingOrderDto.setBillId(bill.getId());
            pendingOrderDto.setCustomerId(bill.getCustomerId());
            pendingOrderDto.setPaymentMethodId(bill.getPaymentMethodId());
            pendingOrderDto.setAddress(invoiceDto.getAddress());
            pendingOrderDto.setPhone(invoiceDto.getPhone());
            pendingOrderDto.setObservations(invoiceDto.getObservations() != null
                    ? invoiceDto.getObservations()
                    : "Pedido generado desde factura " + bill.getInvoiceNumber());

            String dateStr = bill.getInvoiceDate() != null
                    ? bill.getInvoiceDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            pendingOrderDto.setDate(dateStr);

            Double totalToSave;
            String billStatus = bill.getStatusBill();

            if ("ABONO".equalsIgnoreCase(billStatus) || "PENDIENTE".equalsIgnoreCase(billStatus)) {
                totalToSave = bill.getRemainingBalance() != null ? bill.getRemainingBalance() : bill.getTotal();
                log.info("Pedido pendiente - Tipo: {} - Total a cobrar (deuda): ${}",
                        billStatus, totalToSave);
            } else {
                totalToSave = bill.getTotal();
                log.info("Pedido pendiente - Tipo: CONTADO - Total a cobrar: ${}", totalToSave);
            }

            pendingOrderDto.setTotal(totalToSave);

            List<PendingOrderDetailDto> pendingDetails = invoiceDto.getInvoiceDetails().stream()
                    .map(invoiceDetail -> {
                        PendingOrderDetailDto detailDto = new PendingOrderDetailDto();
                        detailDto.setProductId(invoiceDetail.getProductId());
                        detailDto.setQuantity(invoiceDetail.getQuantity());
                        detailDto.setUnitPrice(invoiceDetail.getUnitPrice());

                        Double discount = invoiceDetail.getTotalDiscount() != null
                                ? invoiceDetail.getTotalDiscount()
                                : 0.0;
                        detailDto.setDiscount(discount);

                        detailDto.setTotal(invoiceDetail.getTotal());

                        return detailDto;
                    })
                    .collect(Collectors.toList());

            pendingOrderDto.setPendingOrderDetails(pendingDetails);

            pendingOrderBusiness.save(pendingOrderDto);

            log.info("Pedido pendiente creado exitosamente para factura ID: {} - Total productos: {} - Total a cobrar: ${} - Descuento total: ${}",
                    bill.getId(),
                    pendingDetails.size(),
                    totalToSave,
                    invoiceDto.getTotalDiscount());

        } catch (Exception e) {
            log.error("Error al crear pedido pendiente para factura ID: {} - Error: {} - Causa: {}",
                    bill.getId(),
                    e.getMessage(),
                    e.getCause() != null ? e.getCause().getMessage() : "Sin causa específica");

            throw new GenericException(
                    "Error al crear pedido pendiente: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private void validateAndCheckInventory(List<InvoiceDetailDTO> details) {
        for (InvoiceDetailDTO detail : details) {
            Product product = productRepository.findById(detail.getProductId())
                    .orElseThrow(() -> new GenericException(
                            "Producto no encontrado con ID: " + detail.getProductId(),
                            HttpStatus.NOT_FOUND));

            if (product.getQuantity() < detail.getQuantity()) {
                throw new GenericException(
                        String.format("Stock insuficiente para el producto '%s'. Disponible: %d, Solicitado: %d",
                                product.getProductName(),
                                product.getQuantity(),
                                detail.getQuantity()),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }

    private void updateInventory(List<InvoiceDetailDTO> details) {
        for (InvoiceDetailDTO detail : details) {
            Product product = productRepository.findById(detail.getProductId())
                    .orElseThrow(() -> new GenericException(
                            "Producto no encontrado con ID: " + detail.getProductId(),
                            HttpStatus.NOT_FOUND));

            Long newQuantity = (product.getQuantity() - detail.getQuantity());
            product.setQuantity(newQuantity);

            productRepository.save(product);

            log.debug("Producto {} - Cantidad anterior: {}, Vendido: {}, Nuevo stock: {}",
                    product.getProductName(),
                    product.getQuantity() + detail.getQuantity(),
                    detail.getQuantity(),
                    newQuantity);
        }
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
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new GenericException("Factura no encontrada con ID: " + id, HttpStatus.NOT_FOUND));

        if (!"COTIZACION".equalsIgnoreCase(bill.getStatusBill())) {
            List<BillDetails> details = billDetailsRepopsitory.findByFacturaId(id);
            restoreInventory(details);
            log.info("Inventario restaurado al eliminar factura {}", bill.getInvoiceNumber());
        }

        bill.setStatusBill("INACTIVO");
        billRepository.save(bill);
        return true;
    }

    private void restoreInventory(List<BillDetails> details) {
        for (BillDetails detail : details) {
            Product product = productRepository.findById(detail.getProductId())
                    .orElseThrow(() -> new GenericException(
                            "Producto no encontrado con ID: " + detail.getProductId(),
                            HttpStatus.NOT_FOUND));

            Long restoredQuantity = product.getQuantity() + detail.getQuantity();
            product.setQuantity(restoredQuantity);

            productRepository.save(product);

            log.debug("Producto {} - Inventario restaurado: +{}, Nuevo stock: {}",
                    product.getProductName(),
                    detail.getQuantity(),
                    restoredQuantity);
        }
    }

    @Override
    public GenerateInvoiceResponseDto get(Long id) {
        GenerateInvoiceResponseDto responseDto = billRepository.findByIdWithNames(id)
                .orElseThrow(() -> new GenericException("No existe la factura con id " + id, HttpStatus.NOT_FOUND));

        List<InvoiceDetailDTO> details;

        try {
            details = billDetailsRepopsitory.findDetailsByBillId(id);
        } catch (Exception e) {
            log.warn("No se pudo obtener detalles con nombres de productos, usando query simple");
            List<BillDetails> billDetails = billDetailsRepopsitory.findByFacturaId(id);
            details = billDetails.stream()
                    .map(bd -> new InvoiceDetailDTO(
                            bd.getId(),
                            bd.getProductId(),
                            "",
                            bd.getQuantity(),
                            bd.getUnitPrice(),
                            bd.getDiscountPercent(),
                            bd.getDiscountFixed(),
                            bd.getTotalDiscount(),
                            bd.getSubtotal(),
                            bd.getTotal()
                    ))
                    .toList();
        }

        responseDto.setInvoiceDetails(details);

        log.info("Factura obtenida - ID: {}, Número: {}, Detalles: {}",
                id, responseDto.getInvoiceNumber(), details.size());

        return responseDto;
    }

    @Override
    @Transactional
    public boolean setStatus(Long id, String status) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new GenericException("La factura no fue encontrada por el id " + id, HttpStatus.NOT_FOUND));

        bill.setStatusBill(status);
        bill.setUpdatedAt(LocalDateTime.now());

        billRepository.save(bill);

        return true;
    }

    @Override
    @Transactional
    public boolean setStatusWithPayment(Long id, String status, Double initialPayment, Double remainingBalance) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new GenericException("La factura no fue encontrada por el id " + id, HttpStatus.NOT_FOUND));

        bill.setStatusBill(status);
        bill.setInitialPayment(initialPayment);
        bill.setRemainingBalance(remainingBalance);
        bill.setUpdatedAt(LocalDateTime.now());

        billRepository.save(bill);

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

        return billRepository.findAllWithDetails(pageable);
    }

    private String calculateBillStatus(Double total, Double initialPayment, LocalDate dueDate) {
        if (initialPayment == null || initialPayment == 0.0) {
            return "PENDIENTE";
        }

        if (initialPayment.compareTo(total) >= 0) {
            return "PAGADO";
        }

        if (initialPayment > 0.0 && initialPayment < total) {
            return "ABONO";
        }

        if (dueDate != null && (initialPayment == null || initialPayment == 0.0)) {
            return "PENDIENTE";
        }

        return "PENDIENTE";
    }

    @Override
    public Page<InvoiceResponseDto> searchGenerateInvoice(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String invoiceNumber = null;
        String customerName = null;
        String paymentMethodName = null;
        String userName = null;
        LocalDateTime invoiceDate = null;
        String statusBill = null;
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
        if (customQuery.containsKey("id") && !customQuery.get("id").isEmpty()) {
            id = customQuery.get("id");
        }
        if (customQuery.containsKey("invoiceNumber") && !customQuery.get("invoiceNumber").isEmpty()) {
            invoiceNumber = customQuery.get("invoiceNumber");
        }
        if (customQuery.containsKey("customerName") && !customQuery.get("customerName").isEmpty()) {
            customerName = customQuery.get("customerName");
        }
        if (customQuery.containsKey("paymentMethodName") && !customQuery.get("paymentMethodName").isEmpty()) {
            paymentMethodName = customQuery.get("paymentMethodName");
        }
        if (customQuery.containsKey("userName") && !customQuery.get("userName").isEmpty()) {
            userName = customQuery.get("userName");
        }
        if (customQuery.containsKey("invoiceDate") && !customQuery.get("invoiceDate").isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                invoiceDate = LocalDate.parse(customQuery.get("invoiceDate"), formatter).atStartOfDay();
            } catch (Exception e) {
                log.warn("Invalid date format: " + customQuery.get("invoiceDate") + ". Expected format: yyyy-MM-dd");
            }
        }
        if (customQuery.containsKey("statusBill") && !customQuery.get("statusBill").isEmpty()) {
            statusBill = customQuery.get("statusBill");
        }
        if (customQuery.containsKey("total") && !customQuery.get("total").isEmpty()) {
            total = customQuery.get("total");
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // Construir Specification
        Specification<Bill> spec = Specification.where(null);

        // Filtro obligatorio: status ACTIVE
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("status"), "ACTIVE"));

        // Filtro por ID
        if (id != null) {
            final String idParam = id;
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("id").as(String.class), "%" + idParam + "%"));
        }

        // Filtro por número de factura
        if (invoiceNumber != null) {
            final String invoiceNumParam = invoiceNumber;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("invoiceNumber")), "%" + invoiceNumParam.toUpperCase() + "%"));
        }

        // Filtro por nombre de cliente (usando Subquery)
        if (customerName != null) {
            final String customerParam = customerName;
            spec = spec.and((root, query, cb) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Client> clientRoot = subquery.from(Client.class);
                subquery.select(clientRoot.get("id"))
                        .where(cb.like(cb.upper(clientRoot.get("name")), "%" + customerParam.toUpperCase() + "%"));
                return cb.in(root.get("customerId")).value(subquery);
            });
        }

        // Filtro por método de pago (usando Subquery)
        if (paymentMethodName != null) {
            final String paymentMethodParam = paymentMethodName;
            spec = spec.and((root, query, cb) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<PaymentMethod> paymentMethodRoot = subquery.from(PaymentMethod.class);
                subquery.select(paymentMethodRoot.get("id"))
                        .where(cb.like(cb.upper(paymentMethodRoot.get("description")), "%" + paymentMethodParam.toUpperCase() + "%"));
                return cb.in(root.get("paymentMethodId")).value(subquery);
            });
        }

        // Filtro por nombre de usuario (usando Subquery)
        if (userName != null) {
            final String userParam = userName;
            spec = spec.and((root, query, cb) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<User> userRoot = subquery.from(User.class);
                subquery.select(userRoot.get("id"))
                        .where(cb.like(cb.upper(userRoot.get("name")), "%" + userParam.toUpperCase() + "%"));
                return cb.in(root.get("userId")).value(subquery);
            });
        }

        // Filtro por fecha de factura
        if (invoiceDate != null) {
            final LocalDateTime dateParam = invoiceDate;
            spec = spec.and((root, query, cb) -> {
                Expression<LocalDate> dateExpression = cb.function(
                        "DATE",
                        LocalDate.class,
                        root.get("invoiceDate")
                );
                LocalDate searchDate = dateParam.toLocalDate();
                return cb.equal(dateExpression, searchDate);
            });
        }

        // Filtro por estado de factura
        if (statusBill != null) {
            final String statusParam = statusBill;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("statusBill")), "%" + statusParam.toUpperCase() + "%"));
        }

        // Filtro por total
        if (total != null) {
            final String totalParam = total;
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("total").as(String.class), "%" + totalParam + "%"));
        }

        Page<Bill> entityPage = billRepository.findAll(spec, pagingSort);

        log.info("Búsqueda de facturas - Resultados: {} registros", entityPage.getTotalElements());

        return entityPage.map(this::mapToInvoiceResponseDto);
    }

    // Método de mapeo
    private InvoiceResponseDto mapToInvoiceResponseDto(Bill entity) {
        String customerNameValue = null;
        String paymentMethodNameValue = null;
        String userNameValue = null;

        // Obtener nombre de cliente
        if (entity.getCustomerId() != null) {
            customerNameValue = clientRepository.findById(entity.getCustomerId())
                    .map(Client::getName)
                    .orElse(null);
        }

        // Obtener descripción de método de pago
        if (entity.getPaymentMethodId() != null) {
            paymentMethodNameValue = paymentMethodRepository.findById(entity.getPaymentMethodId())
                    .map(PaymentMethod::getDescription)
                    .orElse(null);
        }

        // Obtener nombre de usuario
        if (entity.getUserId() != null) {
            userNameValue = userRepository.findById(entity.getUserId())
                    .map(User::getName)
                    .orElse(null);
        }

        return new InvoiceResponseDto(
                entity.getId(),
                entity.getCustomerId(),
                customerNameValue,
                entity.getInvoiceNumber(),
                entity.getInvoiceDate(),
                entity.getDueDate(),
                entity.getPaymentTypeId(),
                entity.getPaymentMethodId(),
                paymentMethodNameValue,
                entity.getDeliveryType(),
                entity.getDeliveryCost(),
                entity.getObservations(),
                entity.getTotalDiscount(),
                entity.getTotal(),
                entity.getSubtotal(),
                entity.getInitialPayment(),
                entity.getRemainingBalance(),
                entity.getCashReceived(),
                entity.getUserId(),
                userNameValue,
                entity.getChangeGiven(),
                entity.getStatus(),
                entity.getStatusBill()
        );
    }

    @Override
    public List<GenerateInvoice> getAllGenerateInvoices() {
        return generateInvoiceRepository.findAll();
    }
}