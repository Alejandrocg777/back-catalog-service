package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.DiscountTypeBusiness;
import com.asb.backCompanyService.dto.request.DiscountTypeRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.DiscountType;
import com.asb.backCompanyService.repository.DiscountTypeRepository;
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
public class DiscountTypeService implements DiscountTypeBusiness {
    private final DiscountTypeRepository discountTypeRepository;

    @Override
    public DiscountTypeRequestDTO save(DiscountTypeRequestDTO request) {
        DiscountType discountType = new DiscountType();
        discountType.setDescription(request.getDescription());
        discountType.setStatus(request.getStatus());
        DiscountType newDiscount = discountTypeRepository.save(discountType);

        DiscountTypeRequestDTO response = new DiscountTypeRequestDTO();
        BeanUtils.copyProperties(newDiscount, response);
        return response;
    }

    @Override
    public GenericResponse update(Long id, DiscountTypeRequestDTO requestDTO) {
        if (!discountTypeRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Discount type no existe");

        Optional<DiscountType> discountOptional = discountTypeRepository.findById(id);

        DiscountType discount = discountOptional.get();
        discount.setStatus(requestDTO.getStatus());
        discount.setDescription(requestDTO.getDescription());
        discountTypeRepository.save(discount);

        return new GenericResponse("discount type actualizado con exito", 200);
    }

    @Override
    public Boolean delete(Long id) {
        if (discountTypeRepository.existsById(id)) {
            DiscountType discountType = discountTypeRepository.findById(id).get();
            discountType.setStatus("INACTIVE");
            discountTypeRepository.save(discountType);
            return true;
        } else {
            throw new RuntimeException("el Discount type no fue encontrada por el id " + id);
        }
    }

    @Override
    public Page<DiscountTypeRequestDTO> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return discountTypeRepository.getStatus(pagingSort);
    }

    @Override
    public DiscountTypeRequestDTO get(Long id) {
        if (!discountTypeRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "discount type no existe");

        Optional<DiscountType> discountOptional = discountTypeRepository.findById(id);

        DiscountTypeRequestDTO response = new DiscountTypeRequestDTO();
        response.setDescription(discountOptional.get().getDescription());
        response.setStatus(discountOptional.get().getStatus());
        return response;
    }

    @Override
    public List<DiscountType> getAllNoPage() {
        try {
            return (List<DiscountType>) discountTypeRepository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener el discount type", e);
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar el discount type", e);
        }
    }

}
