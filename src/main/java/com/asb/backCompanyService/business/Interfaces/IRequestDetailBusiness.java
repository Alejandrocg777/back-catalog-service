package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.RequestDetailDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.RequestDetails;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface IRequestDetailBusiness {
    RequestDetailDto save(RequestDetailDto requestDetailDto);

    GenericResponse update(Long id, RequestDetailDto requestDetailDto);

    boolean delete(Long id);

    RequestDetailDto get(Long id);

    Page<RequestDetails> getAll(int page, int size, String orders, String sortBy);

    Page<RequestDetails> searchRequestDetails(Map<String, String> customQuery);

    List<RequestDetails> getAllRequestDetails();
}
