package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.StoreBusiness;
import com.asb.backCompanyService.dto.StoreDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Store;
import com.asb.backCompanyService.repository.StoreRepository;
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
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class StoreService implements StoreBusiness {

    private final StoreRepository storeRepository;

    @Override
    public StoreDto save(StoreDto requestDto) {
        Store store = new Store();
        store.setStoreName(requestDto.getStoreName());
        store.setCapacity(requestDto.getCapacity());
        store.setServiceType(requestDto.getServiceType());
        store.setStoreTypeId(requestDto.getStoreTypeId());
        store.setStoreTypeId(requestDto.getStoreTypeId());

        Store result = storeRepository.save(store);
        StoreDto responseDto = new StoreDto();
        BeanUtils.copyProperties(result, responseDto);
        return responseDto;
    }

    @Override
    public StoreDto get(long id) {
        Store store = storeRepository.getById(id);
        StoreDto responseDto = new StoreDto();
        BeanUtils.copyProperties(store, responseDto);
        return responseDto;
    }

    @Override
    public Page<Store> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return storeRepository.findStore(pagingSort);
    }

    @Override
    public Page<Store> search(Map<String, String> customQuery) {
        return null;
    }

    @Override
    public GenericResponse update(long id, StoreDto requestDto) {
        if (!storeRepository.existsById(id)) {
            throw new com.asb.backCompanyService.exception.GenericException("No existe el atributo con este id" , HttpStatus.BAD_REQUEST);
        }

        Store store = storeRepository.getById(id);
        store.setStoreName(requestDto.getStoreName());
        store.setCapacity(requestDto.getCapacity());
        store.setServiceType(requestDto.getServiceType());
        store.setStoreTypeId(requestDto.getStoreTypeId());
        store.setStoreTypeId(requestDto.getStoreTypeId());

        storeRepository.save(store);

        return new GenericResponse("Actualizado con exito" , 0);
    }

    @Override
    public GenericResponse delete(long id) {
        if (!storeRepository.existsById(id)) {
            throw new com.asb.backCompanyService.exception.GenericException("No existe el atributo con este id" , HttpStatus.BAD_REQUEST);
        }
        storeRepository.deleteById(id);
        return new GenericResponse("Eliminado con exito" , 0);
    }

    @Override
    public List<Store> getAllWithoutPagination() {
        return  storeRepository.findAll();
    }

}
