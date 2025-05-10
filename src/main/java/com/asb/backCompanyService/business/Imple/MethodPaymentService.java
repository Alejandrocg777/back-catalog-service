package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.MethodPaymentBusiness;
import com.asb.backCompanyService.dto.request.MethodPaymentRequestDTO;
import com.asb.backCompanyService.dto.request.SellerRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.MethodPayment;
import com.asb.backCompanyService.model.Seller;
import com.asb.backCompanyService.repository.MethodPaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class MethodPaymentService implements MethodPaymentBusiness {

    private final MethodPaymentRepository methodPaymentRepository;

    @Override
    public MethodPaymentRequestDTO save(MethodPaymentRequestDTO request) {
        MethodPayment methodPayment = new MethodPayment();
        methodPayment.setType(request.getType());
        methodPayment.setCode(request.getCode());
        methodPayment.setStatus(request.getStatus());
        MethodPayment newMethodPayment = methodPaymentRepository.save(methodPayment);

        MethodPaymentRequestDTO response = new MethodPaymentRequestDTO();
        BeanUtils.copyProperties(newMethodPayment, response);
        return response;
    }

    @Override
    public GenericResponse update(Long id, MethodPaymentRequestDTO requestDTO) {
        if (!methodPaymentRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "metodo de pago no existe");

        Optional<MethodPayment> methodPaymentOptional = methodPaymentRepository.findById(id);

        MethodPayment methodPayment = methodPaymentOptional.get();
        methodPayment.setType(requestDTO.getType());
        methodPayment.setCode(requestDTO.getCode());
        methodPayment.setStatus(requestDTO.getStatus());
        methodPaymentRepository.save(methodPayment);

        return new GenericResponse("methodPayment actualizado con exito", 200);
    }

    @Override
    public Boolean delete(Long id) {
        if (methodPaymentRepository.existsById(id)) {
            MethodPayment methodPayment = methodPaymentRepository.findById(id).get();
            methodPayment.setStatus("INACTIVE");
            methodPaymentRepository.save(methodPayment);
            return true;
        } else {
            throw new RuntimeException("el motodo de pago no fue encontrada por el id " + id);
        }
    }

    @Override
    public Page<MethodPaymentRequestDTO> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return methodPaymentRepository.getStatus(pagingSort);
    }

    @Override
    public MethodPaymentRequestDTO get(Long id) {
        if (!methodPaymentRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "motodo de pago no existe");

        Optional<MethodPayment> methosPaymentOptional = methodPaymentRepository.findById(id);

        MethodPaymentRequestDTO response = new MethodPaymentRequestDTO();
        response.setCode(methosPaymentOptional.get().getCode());
        response.setType(methosPaymentOptional.get().getType());
        response.setStatus(methosPaymentOptional.get().getStatus());
        return response;
    }

    @Override
    public List<MethodPayment> getAllNoPage() {
        try {
            return (List<MethodPayment>) methodPaymentRepository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener el metodo de pago");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar el metodo de pago", e);
        }
    }

}
