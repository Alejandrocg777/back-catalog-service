package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.SellerBusiness;
import com.asb.backCompanyService.dto.request.SellerRequestDTO;
import com.asb.backCompanyService.dto.request.TaxConfigurationRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Seller;
import com.asb.backCompanyService.model.TaxConfiguration;
import com.asb.backCompanyService.repository.SellerRepository;
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

public class SellerService implements SellerBusiness {

    private final SellerRepository sellerRepository;

    @Override
    public SellerRequestDTO save(SellerRequestDTO request) {
        Seller seller = new Seller();
        seller.setName(request.getName());
        seller.setLastName(request.getLastName());
        seller.setEmail(request.getEmail());
        seller.setStatus(request.getStatus());
        seller.setPhone(request.getPhone());
        Seller newSeller = sellerRepository.save(seller);

        SellerRequestDTO response = new SellerRequestDTO();
        BeanUtils.copyProperties(newSeller, response);
        return response;
    }

    @Override
    public GenericResponse update(Long id, SellerRequestDTO requestDTO) {
        if (!sellerRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Seller no existe");

        Optional<Seller> sellerOptional = sellerRepository.findById(id);

        Seller seller = sellerOptional.get();
        seller.setName(requestDTO.getName());
        seller.setLastName(requestDTO.getLastName());
        seller.setStatus(requestDTO.getStatus());
        seller.setEmail(requestDTO.getEmail());
        seller.setPhone(requestDTO.getPhone());
        sellerRepository.save(seller);

        return new GenericResponse("seller actualizado con exito", 200);
    }

    @Override
    public Boolean delete(Long id) {
        if (sellerRepository.existsById(id)) {
            Seller seller = sellerRepository.findById(id).get();
            seller.setStatus("INACTIVE");
            sellerRepository.save(seller);
            return true;
        } else {
            throw new RuntimeException("el seller no fue encontrada por el id " + id);
        }
    }

    @Override
    public Page<SellerRequestDTO> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return sellerRepository.getStatus(pagingSort);
    }

    @Override
    public SellerRequestDTO get(Long id) {
        if (!sellerRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "seller no existe");

        Optional<Seller> sellerOptional = sellerRepository.findById(id);

        SellerRequestDTO response = new SellerRequestDTO();
        response.setName(sellerOptional.get().getName());
        response.setLastName(sellerOptional.get().getLastName());
        response.setStatus(sellerOptional.get().getStatus());
        response.setPhone(sellerOptional.get().getPhone());
        response.setEmail(sellerOptional.get().getEmail());
        return response;
    }

    @Override
    public List<Seller> getAllNoPage() {
        try {
            return (List<Seller>) sellerRepository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener el seller");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar el seller", e);
        }
    }
}
