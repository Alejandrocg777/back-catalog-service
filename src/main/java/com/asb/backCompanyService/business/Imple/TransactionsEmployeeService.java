package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.TransactionBusiness;
import com.asb.backCompanyService.business.Interfaces.TransactionEmployeeBusiness;
import com.asb.backCompanyService.dto.request.TransactionEmployeeRequestDTO;
import com.asb.backCompanyService.dto.responde.TransactionEmployeeResponseDTO;
import com.asb.backCompanyService.model.Transaction;
import com.asb.backCompanyService.model.TransactionEmployee;
import com.asb.backCompanyService.model.TransactionType;
import com.asb.backCompanyService.repository.EmployeeRepository;
import com.asb.backCompanyService.repository.TransactionEmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        int size = 6;

        String id = null;
        String employeeName = null;
        String typeTransaction = null;
        String paymentAmount = null;
        String date = null;
        String observation = null;
        String status = null;

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
        if (customQuery.containsKey("id") && !customQuery.get("id").trim().isEmpty()) {
            id = "%" + customQuery.get("id").trim() + "%";
        }

        if (customQuery.containsKey("employeeName") && !customQuery.get("employeeName").trim().isEmpty()) {
            employeeName = "%" + customQuery.get("employeeName").trim() + "%";
        }

        if (customQuery.containsKey("typeTransaction") && !customQuery.get("typeTransaction").trim().isEmpty()) {
            typeTransaction = "%" + customQuery.get("typeTransaction").trim() + "%";
        }

        if (customQuery.containsKey("paymentAmount") && !customQuery.get("paymentAmount").trim().isEmpty()) {
            paymentAmount = "%" + customQuery.get("paymentAmount").trim() + "%";
        }

        if (customQuery.containsKey("date") && !customQuery.get("date").trim().isEmpty()) {
            date = "%" + customQuery.get("date").trim() + "%";
        }

        if (customQuery.containsKey("observation") && !customQuery.get("observation").trim().isEmpty()) {
            observation = "%" + customQuery.get("observation").trim() + "%";
        }

        if (customQuery.containsKey("status") && !customQuery.get("status").trim().isEmpty()) {
            status = "%" + customQuery.get("status").trim() + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);
        Page<TransactionEmployeeResponseDTO> result = transactionEmployeeRepository.search(
                id,
                employeeName,
                typeTransaction,
                paymentAmount,
                date ,
                observation,
                status,
                pagingSort
        );

        log.info("Resultados encontrados: {} elementos", result.getContent().size());
        return result;
    }


}
