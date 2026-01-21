package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.TransactionBusiness;
import com.asb.backCompanyService.dto.responde.ProductOfTransactionDTO;
import com.asb.backCompanyService.dto.responde.TransactionResponseNewDTO;
import com.asb.backCompanyService.model.Transaction;
import com.asb.backCompanyService.repository.ProductRepository;
import com.asb.backCompanyService.repository.TransactionRepository;
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
public class TransactionsService implements TransactionBusiness {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<TransactionResponseNewDTO> getTransactions(Integer page,
                                                           Integer size,
                                                           String orders,
                                                           String sortBy){
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return transactionRepository.getActiveTransactionsMaster(pagingSort);
    }

    @Override
    public Page<ProductOfTransactionDTO> getProductsOfTransaction(Long transactionId, Integer page, Integer size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return productRepository.getProductOfTransaction(transactionId,pagingSort);
    }

    @Override
    public Page<TransactionResponseNewDTO> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String transactionType = null;
        String transactionDate = null;
        String typeUser = null;
        String userName = null;
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

        if (customQuery.containsKey("id")) {
            id = "%" + customQuery.get("id") + "%";
        }

        if (customQuery.containsKey("transactionType")) {
            transactionType = "%" + customQuery.get("transactionType") + "%";
        }

        if (customQuery.containsKey("transactionDate")) {
            transactionDate = "%" + customQuery.get("transactionDate") + "%";
        }

        if (customQuery.containsKey("typeUser")) {
            typeUser = "%" + customQuery.get("typeUser") + "%";
        }

        if (customQuery.containsKey("userName")) {
            userName = "%" + customQuery.get("userName") + "%";
        }

        if (customQuery.containsKey("observation")) {
            observation = "%" + customQuery.get("observation") + "%";
        }

        String actualSortField = sortBy;
        if ("typeUser".equals(sortBy)) {
            actualSortField = "r.name";
        } else if ("userName".equals(sortBy)) {
            actualSortField = "u.name";
        } else if ("transactionDate".equals(sortBy)) {
            actualSortField = "t.transactionDate";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, actualSortField);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        Page<TransactionResponseNewDTO> result = transactionRepository.searchTransaction(
                id, transactionType, transactionDate, typeUser, userName, observation, pagingSort);

        log.info("Resultados encontrados: {}", result.getContent());
        return result;
    }
    @Override
    public Page<ProductOfTransactionDTO> searchProducts(Long transactionId, Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String productName = null;
        String purchasePrice = null;
        String quantity = null;
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
            productName = "%" + customQuery.get("productName").toUpperCase() + "%";
        }

        if (customQuery.containsKey("purchasePrice")) {
            purchasePrice = "%" + customQuery.get("purchasePrice") + "%";
        }

        if (customQuery.containsKey("quantity")) {
            quantity = "%" + customQuery.get("quantity") + "%";
        }


        if (customQuery.containsKey("total")) {
            total = "%" + customQuery.get("total") + "%";
        }

        String actualSortField = sortBy;
        switch (sortBy) {
            case "productId":
                actualSortField = "p.id";
                break;
            case "productName":
                actualSortField = "p.productName";
                break;
            case "purchasePrice":
                actualSortField = "tp.purchasePrice";
                break;
            case "quantity":
                actualSortField = "tp.quantity";
                break;
            case "total":
                actualSortField = "tp.total";
                break;
            case "id":
                actualSortField = "p.id";
                break;
            default:
                actualSortField = "p.id";
                break;
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, actualSortField);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        Page<ProductOfTransactionDTO> result = productRepository.searchProductsTransaction(
                transactionId, id, productName, purchasePrice,quantity, total, pagingSort);

        log.info("Resultados encontrados: {}", result.getContent());
        return result;
    }
    @Transactional
    public Transaction insertTransaction(String transactionType, Double total, Long userId,
                                         String date, String observation, String status) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido: debe ser 'yyyy-MM-dd'", e);
        }

        LocalDateTime transactionDate = parsedDate.atStartOfDay();

        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setTotal(total);
        transaction.setUserId(userId);
        transaction.setTransactionDate(transactionDate);
        transaction.setObservation(observation);
        transaction.setStatus(status != null ? status : "ACTIVE");

        return transactionRepository.save(transaction);
    }

}
