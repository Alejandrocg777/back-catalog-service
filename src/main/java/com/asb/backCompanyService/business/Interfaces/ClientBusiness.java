package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.ClientRequestDTO;
import com.asb.backCompanyService.dto.responde.ClientResponseDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface ClientBusiness {

    ClientRequestDTO save(ClientRequestDTO requestDTO);
    GenericResponse update(Long id, ClientRequestDTO requestDTO);
    Boolean delete(Long id);
    Page<ClientResponseDTO> getAll(int page, int size, String orders, String sortBy);
    ClientRequestDTO get(Long id);
    List<ClientResponseDTO> getAllNoPage(@RequestParam Map<String, String> customQuery);
    Page<ClientResponseDTO>searchCustom(Map<String , String>customQuery);
}
