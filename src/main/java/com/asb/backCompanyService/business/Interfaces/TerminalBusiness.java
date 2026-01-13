package com.asb.backCompanyService.business.Interfaces;

import com.asb.backCompanyService.dto.request.TerminalRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.NumerationResponseDto;
import com.asb.backCompanyService.dto.responde.TerminalResponseDTO;
import com.asb.backCompanyService.model.Terminal;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface TerminalBusiness {


    Terminal save( TerminalRequestDTO terminal);

    Terminal update(Long id,
                    TerminalRequestDTO terminal);

    Page<TerminalResponseDTO> getAll(Integer page,
                                     Integer size,
                                     String orders,
                                     String sortBy);

    GenericResponse delete(Long id);

    Page<TerminalResponseDTO> search(Map<String, String> customQuery);

    List<Terminal> getAllNoPage();
}
