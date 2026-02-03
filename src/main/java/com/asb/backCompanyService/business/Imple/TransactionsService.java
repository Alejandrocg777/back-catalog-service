package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.TransactionBusiness;
import com.asb.backCompanyService.dto.responde.ProductOfTransactionDTO;
import com.asb.backCompanyService.dto.responde.TransactionResponseNewDTO;
import com.asb.backCompanyService.model.Rol;
import com.asb.backCompanyService.model.Transaction;
import com.asb.backCompanyService.model.User;
import com.asb.backCompanyService.repository.ProductRepository;
import com.asb.backCompanyService.repository.RolRepository;
import com.asb.backCompanyService.repository.TransactionRepository;
import com.asb.backCompanyService.repository.UserRepository;
import jakarta.persistence.criteria.*;
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
public class TransactionsService implements TransactionBusiness {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    public Page<TransactionResponseNewDTO> getTransactions(Integer page, Integer size,
                                                           String orders, String sortBy,
                                                           LocalDate startDate, LocalDate endDate) {

        Sort.Direction direction = orders.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            return transactionRepository.getActiveTransactionsByDateRange(startDateTime, endDateTime, pageable);
        }

        // Si no hay fechas, retornar todas las transacciones activas
        return transactionRepository.getActiveTransactionsMaster(pageable);
    }

    @Override
    public Page<ProductOfTransactionDTO> getProductsOfTransaction(Long transactionId, Integer page, Integer size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return productRepository.getProductOfTransaction(transactionId,pagingSort);
    }

    @Override
    public Page<TransactionResponseNewDTO> searchCustom(Map<String, String> customQuery,
                                                        LocalDate startDate,
                                                        LocalDate endDate) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String transactionType = null;
        LocalDate transactionDate = null;
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
        if (customQuery.containsKey("id") && !customQuery.get("id").isEmpty()) {
            id = customQuery.get("id");
        }
        if (customQuery.containsKey("transactionType") && !customQuery.get("transactionType").isEmpty()) {
            transactionType = customQuery.get("transactionType");
        }
        if (customQuery.containsKey("date") && !customQuery.get("date").isEmpty()) {
            try {
                transactionDate = LocalDate.parse(customQuery.get("date"));
            } catch (Exception e) {
                log.warn("Invalid date format: " + customQuery.get("date") + ". Expected format: yyyy-MM-dd");
            }
        }
        if (customQuery.containsKey("typeUser") && !customQuery.get("typeUser").isEmpty()) {
            typeUser = customQuery.get("typeUser");
        }
        if (customQuery.containsKey("userName") && !customQuery.get("userName").isEmpty()) {
            userName = customQuery.get("userName");
        }
        if (customQuery.containsKey("observation") && !customQuery.get("observation").isEmpty()) {
            observation = customQuery.get("observation");
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Transaction> spec = Specification.where(null);

        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("status"), "ACTIVE"));

        // 🔥 AGREGAR FILTRO POR RANGO DE FECHAS
        if (startDate != null && endDate != null) {
            final LocalDateTime startDateTime = startDate.atStartOfDay();
            final LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("transactionDate"), startDateTime, endDateTime)
            );
        }

        if (id != null) {
            final String idParam = id;
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("id").as(String.class), "%" + idParam + "%"));
        }

        if (transactionType != null) {
            final String typeParam = transactionType;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("transactionType")), "%" + typeParam.toUpperCase() + "%"));
        }

        if (transactionDate != null) {
            final LocalDate dateParam = transactionDate;
            spec = spec.and((root, query, cb) -> {
                Expression<LocalDate> dateExpression = cb.function(
                        "DATE",
                        LocalDate.class,
                        root.get("transactionDate")
                );
                return cb.equal(dateExpression, dateParam);
            });
        }

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

        if (typeUser != null) {
            final String rolParam = typeUser;
            spec = spec.and((root, query, cb) -> {
                Subquery<Long> userSubquery = query.subquery(Long.class);
                Root<User> userRoot = userSubquery.from(User.class);

                Subquery<Long> rolSubquery = query.subquery(Long.class);
                Root<Rol> rolRoot = rolSubquery.from(Rol.class);
                rolSubquery.select(rolRoot.get("id"))
                        .where(cb.like(cb.upper(rolRoot.get("name")), "%" + rolParam.toUpperCase() + "%"));

                userSubquery.select(userRoot.get("id"))
                        .where(cb.in(userRoot.get("rolId")).value(rolSubquery));

                return cb.in(root.get("userId")).value(userSubquery);
            });
        }

        if (observation != null) {
            final String obsParam = observation;
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("observation")), "%" + obsParam.toUpperCase() + "%"));
        }

        Page<Transaction> entityPage = transactionRepository.findAll(spec, pagingSort);

        log.info("Resultados encontrados: {}", entityPage.getContent().size());

        return entityPage.map(this::mapToTransactionResponseNewDTO);
    }

    private TransactionResponseNewDTO mapToTransactionResponseNewDTO(Transaction entity) {
        Long rolIdValue = null;
        String rolNameValue = null;
        Long userIdValue = null;
        String userNameValue = null;

        if (entity.getUserId() != null) {
            User user = userRepository.findById(entity.getUserId()).orElse(null);
            if (user != null) {
                userIdValue = user.getId();
                userNameValue = user.getName();

                if (user.getRolId() != null) {
                    Rol rol = rolRepository.findById(user.getRolId()).orElse(null);
                    if (rol != null) {
                        rolIdValue = rol.getId();
                        rolNameValue = rol.getName();
                    }
                }
            }
        }

        return new TransactionResponseNewDTO(
                entity.getId(),
                rolIdValue,
                rolNameValue,
                userIdValue,
                userNameValue,
                entity.getTransactionDate(),
                entity.getTransactionType(),
                entity.getObservation()
        );
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
