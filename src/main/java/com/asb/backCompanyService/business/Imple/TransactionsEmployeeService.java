package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.TransactionBusiness;
import com.asb.backCompanyService.business.Interfaces.TransactionEmployeeBusiness;
import com.asb.backCompanyService.dto.request.TransactionEmployeeRequestDTO;
import com.asb.backCompanyService.dto.responde.TransactionEmployeeResponseDTO;
import com.asb.backCompanyService.model.Employee;
import com.asb.backCompanyService.model.Transaction;
import com.asb.backCompanyService.model.TransactionEmployee;
import com.asb.backCompanyService.model.TransactionType;
import com.asb.backCompanyService.repository.EmployeeRepository;
import com.asb.backCompanyService.repository.TransactionEmployeeRepository;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class TransactionsEmployeeService implements TransactionEmployeeBusiness {

    private final TransactionEmployeeRepository transactionEmployeeRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public TransactionEmployeeResponseDTO createTransaction(TransactionEmployeeRequestDTO requestDTO) {
        log.info("Creando transacción para empleado ID: {}", requestDTO.getEmployeeId());

        TransactionEmployee entity = new TransactionEmployee();
        entity.setEmployeeId(requestDTO.getEmployeeId());
        entity.setTypeTransaction(TransactionType.valueOf(requestDTO.getTypeTransaction()));
        entity.setPaymentAmount(requestDTO.getPaymentAmount());
        entity.setDate(requestDTO.getDate());
        entity.setStatus("ACTIVE");
        entity.setObservation(requestDTO.getObservation());

        TransactionEmployee saved = transactionEmployeeRepository.save(entity);

        TransactionEmployeeResponseDTO response = new TransactionEmployeeResponseDTO();
        response.setId(saved.getId());
        response.setEmployeeId(saved.getEmployeeId());
        response.setTypeTransaction(TransactionType.valueOf(String.valueOf(saved.getTypeTransaction())));
        response.setPaymentAmount(saved.getPaymentAmount());
        response.setDate(saved.getDate());
        response.setObservation(saved.getObservation());
        response.setStatus(saved.getStatus());

        return response;
    }

    @Override
    public Page<TransactionEmployeeResponseDTO> getTransactions(Integer page,
                                                                Integer size,
                                                                String orders,
                                                                String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return transactionEmployeeRepository.getStatus(pagingSort);
    }


    @Override
    public Page<TransactionEmployeeResponseDTO> search(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 10;
        String id = null;
        String employeeName = null;
        String typeTransaction = null;
        String dateFrom = null;
        String dateTo = null;
        LocalDate date = null;  // ✅ NUEVO: Variable para fecha exacta
        String paymentAmount = null;
        String observation = null;

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
        if (customQuery.containsKey("employeeName") && !customQuery.get("employeeName").isEmpty()) {
            employeeName = customQuery.get("employeeName");
        }
        if (customQuery.containsKey("typeTransaction") && !customQuery.get("typeTransaction").isEmpty()) {
            typeTransaction = customQuery.get("typeTransaction");
        }
        if (customQuery.containsKey("dateFrom") && !customQuery.get("dateFrom").isEmpty()) {
            dateFrom = customQuery.get("dateFrom");
        }
        if (customQuery.containsKey("dateTo") && !customQuery.get("dateTo").isEmpty()) {
            dateTo = customQuery.get("dateTo");
        }
        // ✅ NUEVO: Parsear fecha exacta
        if (customQuery.containsKey("date") && !customQuery.get("date").isEmpty()) {
            try {
                date = LocalDate.parse(customQuery.get("date"));
            } catch (Exception e) {
                log.warn("Fecha inválida: {}", customQuery.get("date"));
            }
        }
        if (customQuery.containsKey("paymentAmount") && !customQuery.get("paymentAmount").isEmpty()) {
            paymentAmount = customQuery.get("paymentAmount");
        }

        if (customQuery.containsKey("observation") && !customQuery.get("observation").isEmpty()) {
            observation = customQuery.get("observation");
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<TransactionEmployee> spec = Specification.where(null);

        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("status"), "ACTIVE"));

        if (id != null) {
            final String idParam = id;
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("id").as(String.class), "%" + idParam + "%"));
        }

        if (typeTransaction != null) {
            try {
                final TransactionType typeParam = TransactionType.valueOf(typeTransaction.toUpperCase());
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("typeTransaction"), typeParam));
            } catch (IllegalArgumentException e) {
                log.warn("Tipo de transacción inválido: {}", typeTransaction);
            }
        }

        // ✅ NUEVO: Filtro por fecha exacta (tiene prioridad sobre rango)
        if (date != null) {
            final LocalDate dateParam = date;
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("date"), dateParam));
        } else {
            // Si no hay fecha exacta, usar rango de fechas
            if (dateFrom != null) {
                try {
                    final LocalDate dateFromParam = LocalDate.parse(dateFrom);
                    spec = spec.and((root, query, cb) ->
                            cb.greaterThanOrEqualTo(root.get("date"), dateFromParam));
                } catch (Exception e) {
                    log.warn("Fecha desde inválida: {}", dateFrom);
                }
            }

            if (dateTo != null) {
                try {
                    final LocalDate dateToParam = LocalDate.parse(dateTo);
                    spec = spec.and((root, query, cb) ->
                            cb.lessThanOrEqualTo(root.get("date"), dateToParam));
                } catch (Exception e) {
                    log.warn("Fecha hasta inválida: {}", dateTo);
                }
            }
        }

        if (paymentAmount != null) {
            final String paymentAmountParam = paymentAmount;
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("paymentAmount").as(String.class), "%" + paymentAmountParam + "%"));
        }


        if (observation != null) {
            final String observationParam = observation;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("observation")), "%" + observationParam.toUpperCase() + "%"));
        }

        if (employeeName != null) {
            final String employeeParam = employeeName;
            spec = spec.and((root, query, cb) -> {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Employee> employeeRoot = subquery.from(Employee.class);
                subquery.select(employeeRoot.get("id"))
                        .where(cb.like(cb.upper(employeeRoot.get("name")), "%" + employeeParam.toUpperCase() + "%"));
                return cb.in(root.get("employeeId")).value(subquery);
            });
        }

        Page<TransactionEmployee> entityPage = transactionEmployeeRepository.findAll(spec, pagingSort);

        log.info("Transacciones encontradas: {}", entityPage.getContent().size());

        return entityPage.map(this::mapToTransactionEmployeeResponseDTO);
    }

    private TransactionEmployeeResponseDTO mapToTransactionEmployeeResponseDTO(TransactionEmployee entity) {
        String employeeName = null;

        if (entity.getEmployeeId() != null) {
            employeeName = employeeRepository.findById(entity.getEmployeeId())
                    .map(Employee::getName)
                    .orElse(null);
        }

        return TransactionEmployeeResponseDTO.builder()
                .id(entity.getId())
                .typeTransaction(entity.getTypeTransaction())
                .employeeId(entity.getEmployeeId())
                .employeeName(employeeName)
                .date(entity.getDate())
                .paymentAmount(entity.getPaymentAmount())
                .observation(entity.getObservation())
                .status(entity.getStatus())
                .build();
    }


}
