package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.DiscountBusiness;
import com.asb.backCompanyService.dto.request.DiscountRequestDTO;
import com.asb.backCompanyService.dto.responde.DiscountResponseDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.exception.GenericException;
import com.asb.backCompanyService.model.Discount;
import com.asb.backCompanyService.repository.DiscountRepository;
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
public class DiscountService implements DiscountBusiness {

    private final DiscountRepository discountRepository;
    private final DiscountTypeRepository typeRepository;

    @Override
    public DiscountRequestDTO save(DiscountRequestDTO request) {
        Discount discount = new Discount();
        discount.setAmount(request.getAmount().doubleValue());
        discount.setQuantity(request.getQuantity());
        discount.setStatus(request.getStatus());
        discount.setDiscountTypeId(request.getDiscountTypeId());
        Discount newDiscount = discountRepository.save(discount);

        DiscountRequestDTO response = new DiscountRequestDTO();
        BeanUtils.copyProperties(newDiscount, response);
        return response;
    }

    public Double calculatePriceLessDiscount(Long id, Double price, Long quantity) {

        if (!discountRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "descuento no existe");

        Discount discount = discountRepository.findById(id).get();

        Double discountPriceFinal = 0.0;
        Double totalPriceBeforeDiscount = price * quantity;

        if (discount.getDiscountTypeId() == 1){
            discountPriceFinal = totalPriceBeforeDiscount - ((discount.getAmount() / 100) * totalPriceBeforeDiscount);
        }

        if (discount.getDiscountTypeId() == 2){
            discountPriceFinal = totalPriceBeforeDiscount - discount.getAmount();
        }

        if (discount.getDiscountTypeId() == 3){
            if (quantity >= discount.getQuantity()) {
                discountPriceFinal = totalPriceBeforeDiscount - ((discount.getAmount() / 100) * totalPriceBeforeDiscount);
            }
        }

        if (discount.getDiscountTypeId() == 4){
            if (quantity >= discount.getQuantity()) {
                discountPriceFinal = totalPriceBeforeDiscount - discount.getAmount();
            }
        }

        if(discountPriceFinal == 0.0){
            return totalPriceBeforeDiscount;
        }

        return discountPriceFinal;
    }

    @Override
    public GenericResponse update(Long id, DiscountRequestDTO requestDTO) {
        if (!discountRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Discount no existe");

        Optional<Discount> discountOptional = discountRepository.findById(id);

        Discount discount = discountOptional.get();
        discount.setAmount(requestDTO.getAmount().doubleValue());
        discount.setQuantity(requestDTO.getQuantity());
        discount.setStatus(requestDTO.getStatus());
        discount.setDiscountTypeId(requestDTO.getDiscountTypeId());
        discountRepository.save(discount);

        return new GenericResponse("discount actualizado con exito", 200);

    }

    @Override
    public Boolean delete(Long id) {
        if (discountRepository.existsById(id)) {
            Discount client = discountRepository.findById(id).get();
            client.setStatus("INACTIVE");
            discountRepository.save(client);
            return true;
        } else {
            throw new RuntimeException("el Discount no fue encontrada por el id " + id);
        }
    }

    @Override
    public Page<DiscountResponseDTO> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return discountRepository.getStatus(pagingSort);
    }

    @Override
    public DiscountRequestDTO get(Long id) {
        if (!discountRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "discount no existe");

        Optional<Discount> discountOptional = discountRepository.findById(id);

        DiscountRequestDTO response = new DiscountRequestDTO();
        response.setAmount(discountOptional.get().getAmount().longValue());
        response.setQuantity(discountOptional.get().getQuantity());
        response.setStatus(discountOptional.get().getStatus());
        response.setDiscountTypeId(discountOptional.get().getDiscountTypeId());
        return response;
    }

    @Override
    public List<Discount> getAllNoPage() {
        try {
            return (List<Discount>) discountRepository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener el discount", e);
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar el discount", e);
        }
    }
}
