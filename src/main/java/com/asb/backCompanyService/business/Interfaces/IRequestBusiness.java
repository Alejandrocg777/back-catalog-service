package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.RequestDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Requests;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IRequestBusiness {
    RequestDto save(RequestDto requestDto);

    GenericResponse update(Long id, RequestDto requestDto);

    boolean delete(Long id);

    RequestDto get(Long id);

    Page<Requests> getAll(int page, int size, String orders, String sortBy);

    Page<Requests> searchRequests(Map<String, String> customQuery);

    List<Requests> getAllRequests();
}
