package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.PaymentMethodDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.PaymentMethod;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IPaymentMethodBusiness {
    PaymentMethodDto save(PaymentMethodDto paymentMethodDto);

    GenericResponse update(Long id, PaymentMethodDto paymentMethodDto);

    boolean delete(Long id);

    PaymentMethodDto get(Long id);

    Page<PaymentMethod> getAll(int page, int size, String orders, String sortBy);

    Page<PaymentMethod> searchPaymentMethod(Map<String, String> customQuery);

    List<PaymentMethod> getAllPaymentMethod();
}