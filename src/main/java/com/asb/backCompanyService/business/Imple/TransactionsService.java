package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.TransactionBusiness;
import com.asb.backCompanyService.dto.responde.ProductResponseDTO;
import com.asb.backCompanyService.dto.responde.TransactionResponseDTO;
import com.asb.backCompanyService.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class TransactionsService implements TransactionBusiness {

    private final TransactionRepository transactionRepository;
    @Override
    public Page<TransactionResponseDTO> getTransactions(Integer page,
                                                        Integer size,
                                                        String orders,
                                                        String sortBy){
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return transactionRepository.getActiveTransactions(pagingSort);
    }

    @Override
    public Page<TransactionResponseDTO> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String productName = null;
        String transactionType = null;
        String date = null;
        String typeUser = null;
        String quantity = null;
        String userName = null;
        String value = null;
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

        if (customQuery.containsKey("id")) {
            id = "%" + customQuery.get("id") + "%";
        }

        if (customQuery.containsKey("productName")) {
            productName = "%" + customQuery.get("productName") + "%";
        }

        if (customQuery.containsKey("transactionType")) {
            transactionType = "%" + customQuery.get("transactionType") + "%";
        }

        if (customQuery.containsKey("date")) {
            date = "%" + customQuery.get("date") + "%";
        }

        if (customQuery.containsKey("typeUser")) {
            typeUser = "%" + customQuery.get("typeUser") + "%";
        }

        if (customQuery.containsKey("quantity")) {
            quantity = "%" + customQuery.get("quantity") + "%";
        }


        if (customQuery.containsKey("userName")) {
            userName = "%" + customQuery.get("userName") + "%";
        }

        if (customQuery.containsKey("value")) {
            value = "%" + customQuery.get("value") + "%";
        }

        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status") + "%";
        }


        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        Page<TransactionResponseDTO> result = transactionRepository.search( id,  productName,  transactionType,  quantity,  typeUser, userName, value, status, pagingSort);
        log.info("Resultados encontrados: {}", result.getContent());
        return result;
    }
}
