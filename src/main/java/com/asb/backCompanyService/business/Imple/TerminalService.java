package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.TerminalBusiness;
import com.asb.backCompanyService.dto.request.TerminalRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.TerminalResponseDTO;
import com.asb.backCompanyService.exception.GenericException;
import com.asb.backCompanyService.model.Terminal;
import com.asb.backCompanyService.repository.TerminalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class TerminalService implements TerminalBusiness {

    private final TerminalRepository terminalRepository;

    @Override
    public Terminal save(TerminalRequestDTO terminal) {

        Terminal newTerminal = new Terminal();
        newTerminal.setName(terminal.getName());
        newTerminal.setNumerationId(terminal.getNumerationId());
        newTerminal.setUserId(terminal.getUserId());
        newTerminal.setStatus("ACTIVE");
        return terminalRepository.save(newTerminal);

    }

    @Override
    public Terminal update(Long id, TerminalRequestDTO terminal) {

        Terminal newTerminal = terminalRepository.findById(id).get();
        newTerminal.setName(terminal.getName());
        newTerminal.setNumerationId(terminal.getNumerationId());
        newTerminal.setUserId(terminal.getUserId());
        return terminalRepository.save(newTerminal);

    }

    @Override
    public Page<TerminalResponseDTO> getAll(Integer page, Integer size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Object[]> pageResult = terminalRepository.findAllTerminalesPaginadoRaw(pagingSort);

        // Mapeo manual de Object[] a TerminalResponseDTO
        List<TerminalResponseDTO> dtos = pageResult.getContent().stream()
                .map(row -> {
                    TerminalResponseDTO dto = new TerminalResponseDTO();
                    dto.setId(((Number) row[0]).longValue());
                    dto.setName((String) row[1]);
                    dto.setNumerationId(row[2] != null ? ((Number) row[2]).longValue() : null);
                    dto.setPrefix((String) row[3]);
                    dto.setInitialNumber(row[4] != null ? (Integer) row[4] : null);
                    dto.setFinalNumber(row[5] != null ? (Integer) row[5] : null);
                    dto.setUserId(row[6] != null ? ((Number) row[6]).longValue() : null);
                    dto.setUserName((String) row[7]);
                    dto.setNumberUser(((Number) row[8]).longValue());
                    dto.setStatus((String) row[9]);
                    return dto;
                })
                .toList();

        // Reconstruir el Page con los DTOs mapeados
        return new PageImpl<>(dtos, pagingSort, pageResult.getTotalElements());
    }

    @Override
    public GenericResponse delete(Long id) {

        Terminal terminal = terminalRepository.findById(id).get();

        if (terminal == null) {
            throw new GenericException("Id no encontrado", HttpStatus.BAD_REQUEST);
        }
        terminal.setStatus("INACTIVE");
        terminalRepository.save(terminal);

        return new GenericResponse("Borrado con exito", 0);
    }

    @Override
    public List<Terminal> getAllNoPage() {
        return terminalRepository.findTerminalByStatus("ACTIVE");
    }
}
