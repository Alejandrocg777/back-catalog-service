package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.BranchDtoRequest;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.request.BranchDto;
import com.asb.backCompanyService.dto.responde.BranchDtoResponse;
import com.asb.backCompanyService.model.Branch;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IBranchBusiness {
    BranchDtoResponse save(BranchDtoRequest branchDtoResponse);

    GenericResponse update(Long id, BranchDtoRequest branchDtoResponse);

    boolean delete(Long id);

    BranchDto get(long id);

    Page<BranchDtoResponse> getAll(int page, int size, String orders, String sortBy);

    Page<BranchDtoResponse> searchCustom(Map<String, String> customQuery);

    List<Branch> getAllBranches();
}

