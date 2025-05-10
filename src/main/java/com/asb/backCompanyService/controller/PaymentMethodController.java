package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IPaymentMethodBusiness;
import com.asb.backCompanyService.dto.request.PaymentMethodDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.PaymentMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${app.request.prefix}/${app.request.version}${app.request.mappings}/payment-method")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class PaymentMethodController {

    private final IPaymentMethodBusiness iPaymentMethodBusiness;

    @PostMapping("/create")
    public ResponseEntity<PaymentMethodDto> save(@RequestBody PaymentMethodDto paymentMethodDto) {
        PaymentMethodDto savedPaymentMethod = iPaymentMethodBusiness.save(paymentMethodDto);
        return ResponseEntity.ok(savedPaymentMethod);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PaymentMethodDto> get(@PathVariable("id") long id) {
        PaymentMethodDto paymentMethodDto = iPaymentMethodBusiness.get(id);
        return ResponseEntity.ok(paymentMethodDto);
    }

    @GetMapping
    public ResponseEntity<Page<PaymentMethod>> getAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "4") int size,
                                                      @RequestParam(defaultValue = "ASC") String orders,
                                                      @RequestParam(defaultValue = "payment_method_id") String sortBy) {
        Page<PaymentMethod> paymentMethods = iPaymentMethodBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(paymentMethods);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PaymentMethod>> search(@RequestParam Map<String, String> customQuery) {
        Page<PaymentMethod> paymentMethods = iPaymentMethodBusiness.searchPaymentMethod(customQuery);
        return ResponseEntity.ok(paymentMethods);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long paymentMethodId,
                                                  @RequestBody PaymentMethodDto paymentMethodDto) {
        log.info("Iniciando actualización para PaymentMethod con ID: {} y DTO: {}", paymentMethodId, paymentMethodDto);
        GenericResponse response = iPaymentMethodBusiness.update(paymentMethodId, paymentMethodDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        iPaymentMethodBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-page/getAllPaymentMethods")
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods() {
        log.info("Starting endpoint to get all payment methods");
        List<PaymentMethod> paymentMethods = iPaymentMethodBusiness.getAllPaymentMethod();
        return new ResponseEntity<>(paymentMethods, HttpStatus.OK);
    }
}
