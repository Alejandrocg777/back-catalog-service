package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.IBranchBusiness;
import com.asb.backCompanyService.dto.request.BranchDto;
import com.asb.backCompanyService.dto.request.BranchDtoRequest;
import com.asb.backCompanyService.dto.responde.BranchDtoResponse;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Branch;
import com.asb.backCompanyService.repository.BranchRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class BranchService implements IBranchBusiness {

    private final BranchRepository repository;


    @Override
    @Transactional
    public BranchDtoResponse save(BranchDtoRequest branchDto) {
        try {

                Branch branch = new Branch();

                branch.setMainBranch(branchDto.getMainBranch());
                branch.setBranchName(branchDto.getBranchName());
                branch.setDepartmentId(branchDto.getDepartmentId());
                branch.setCityId(branchDto.getMunicipalityId());
                branch.setCompanyId(branchDto.getCompanyId());
                branch.setStatus("ACTIVE");

                Branch newObject = repository.save(branch);

                BranchDtoResponse objectDtoVo = new BranchDtoResponse();
                BeanUtils.copyProperties(newObject, objectDtoVo);
                return objectDtoVo;

        } catch (Exception e) {
            throw new RuntimeException("Error", e);
        }
    }




    @Override
    @Transactional
    public GenericResponse update(Long id, BranchDtoRequest branchDto) {
        GenericResponse response = new GenericResponse();
        try {
            log.info("Iniciando método de actualización para Branch con ID: {} y BranchDto: {}", id, branchDto);

            boolean objectExists = repository.existsById(id);
            if (!objectExists) {
                throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La sucursal no existe");
            }

            Branch branch = repository.findById(id).get();
            branch.setMainBranch(branchDto.getMainBranch());
            branch.setBranchName(branchDto.getBranchName());
            branch.setDepartmentId(branchDto.getDepartmentId());
            branch.setCityId(branchDto.getMunicipalityId());
            branch.setCompanyId(branchDto.getCompanyId());
            branch.setStatus(branchDto.getStatus());

            repository.save(branch);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Sucursal actualizada");
        } catch (Exception e) {
            log.error("Se produjo un error al actualizar la sucursal: {}", e.getMessage());
            throw new RuntimeException("Error", e);
        }
        return response;
    }


    @Override
    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            Branch branch = repository.findById(id).get();
            branch.setStatus("INACTIVE");
            repository.save(branch);
            return true;
        } else {
            throw new RuntimeException("La sucursul no fue encontra por el id " + id);
        }
    }

    @Override
    public BranchDto get(long id) {
        Optional<Branch> branchOptional = repository.findById(id);
        BranchDto branchDto = null;
        if (branchOptional.isPresent()) {
            branchDto = new BranchDto();
            BeanUtils.copyProperties(branchOptional.get(), branchDto);
        } else {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "la sucursal no existe");
        }
        return branchDto;
    }

    @Override
    public Page<BranchDtoResponse> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        Page<BranchDtoResponse> rawResults = repository.getStatus(pagingSort);
        return rawResults;
    }


    @Override
    public Page<BranchDtoResponse> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String mainBranch = null;
        String branchName = null;
        String departmentName = null;
        String municipality = null;
        String companyName = null;
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

        if (customQuery.containsKey("mainBranch")) {
            mainBranch = "%" + customQuery.get("mainBranch") + "%";
        }

        if (customQuery.containsKey("branchName")) {
            branchName = "%" + customQuery.get("branchName") + "%";
        }

        if (customQuery.containsKey("departmentName")) {
            departmentName = "%" + customQuery.get("departmentName") + "%";
        }

        if (customQuery.containsKey("municipality")) {
            municipality = "%" + customQuery.get("municipality")+ "%";
        }

        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status") + "%";
        }

        if (customQuery.containsKey("companyName")) {
            companyName = "%" + customQuery.get("companyName") + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);

        log.info("id: " + id);
        log.info("mainBranch: " + mainBranch);
        log.info("branchName: " + branchName);
        log.info("departmentName: " + departmentName);
        log.info("municipality: " + municipality);
        log.info("status: " + status);
        log.info("page: " + page);
        log.info("size: " + size);
        log.info("orders: " + orders);
        log.info("sortBy: " + sortBy);

        Page<BranchDtoResponse> searchResult = repository.searchBranch(id, mainBranch, branchName, departmentName, municipality, companyName, status, pagingSort);
        log.info("Resultados encontrados: {}", searchResult.getContent());
        return searchResult;
    }


    @Override
    public List<Branch> getAllBranches() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error al obtener la sucursal");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar la sucursal", e);
        }
    }
}
