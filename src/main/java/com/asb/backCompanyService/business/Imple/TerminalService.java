package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.TerminalBusiness;
import com.asb.backCompanyService.dto.request.TerminalRequestDTO;
import com.asb.backCompanyService.dto.request.UserRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.TerminalResponseDTO;
import com.asb.backCompanyService.dto.responde.UserListResponseDTO;
import com.asb.backCompanyService.exception.GenericException;
import com.asb.backCompanyService.model.Terminal;
import com.asb.backCompanyService.model.TerminalDetails;
import com.asb.backCompanyService.repository.TerminalDetailsRepository;
import com.asb.backCompanyService.repository.TerminalRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class TerminalService implements TerminalBusiness {

    private final TerminalRepository terminalRepository;
    private final TerminalDetailsRepository terminalDetailsRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Terminal save(TerminalRequestDTO dto) {

        List<Long> userIds = new ArrayList<>();
        if (dto.getUsers() != null && !dto.getUsers().isEmpty()) {
            userIds = dto.getUsers().stream()
                    .map(UserRequestDTO::getUserId)
                    .filter(Objects::nonNull)  // Evitar userId nulos
                    .toList();

            // Validar duplicados dentro de la misma petición (opcional pero recomendado)
            Set<Long> duplicados = userIds.stream()
                    .collect(Collectors.toSet());
            if (duplicados.size() < userIds.size()) {
                throw new GenericException("No se permiten usuarios duplicados en la misma terminal", HttpStatus.BAD_REQUEST);
            }

            // Validar que ninguno de estos usuarios ya esté asignado a otra terminal
            List<Long> usuariosYaAsignados = terminalDetailsRepository.findUserIdsInUse(userIds);
            if (!usuariosYaAsignados.isEmpty()) {
                throw new GenericException("Los siguientes usuarios ya están asignados a otra terminal: " + usuariosYaAsignados, HttpStatus.BAD_REQUEST);
            }
        }

        // Crear la terminal
        Terminal terminal = new Terminal();
        terminal.setName(dto.getName());
        terminal.setNumerationId(dto.getNumerationId());
        terminal.setStatus("ACTIVE");  // valor por defecto si lo deseas
        terminal = terminalRepository.save(terminal);

        // Asignar los usuarios a la terminal
        if (!userIds.isEmpty()) {
            for (Long userId : userIds) {
                TerminalDetails detail = new TerminalDetails();
                detail.setUserId(userId);
                detail.setTerminalId(terminal.getId());
                terminalDetailsRepository.save(detail);
            }
        }

        return terminal;

    }

    @Override
    @Transactional
    public Terminal update(Long id, TerminalRequestDTO dto) {


        // 1. Buscar la terminal
        Terminal terminal = terminalRepository.findById(id)
                .orElseThrow(() -> new GenericException("Terminal no encontrada con id: " + id, HttpStatus.NOT_FOUND));

        // 2. Actualizar campos básicos
        terminal.setName(dto.getName());
        terminal.setNumerationId(dto.getNumerationId());

        // 3. Procesar usuarios si se envía la lista
        if (dto.getUsers() != null) {
            List<Long> newUserIds = dto.getUsers().stream()
                    .map(UserRequestDTO::getUserId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();

            if (!newUserIds.isEmpty()) {
                // Validar que los nuevos usuarios no estén en otra terminal (excluimos la actual)
                List<Long> usuariosOcupados = terminalDetailsRepository
                        .findUserIdsInUseExcludingTerminal(newUserIds, id);

                if (!usuariosOcupados.isEmpty()) {
                    throw new GenericException("Los usuarios ya están asignados a otra terminal: " + usuariosOcupados, HttpStatus.BAD_REQUEST);
                }
            }

            List<TerminalDetails> detallesActuales = terminalDetailsRepository.findByTerminalId(id);
            if (!detallesActuales.isEmpty()) {
                terminalDetailsRepository.deleteAll(detallesActuales);  // Seguro dentro de @Transactional
            }

            // Crear los nuevos
            for (Long userId : newUserIds) {
                TerminalDetails detail = new TerminalDetails();
                detail.setUserId(userId);
                detail.setTerminalId(terminal.getId());
                terminalDetailsRepository.save(detail);
            }
        }
        // Si no envían "uses", no tocamos los usuarios actuales

        return terminalRepository.save(terminal);

    }

    @Override
    public Page<TerminalResponseDTO> getAll(Integer page, Integer size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Object[]> pageResult = terminalRepository.findAllTerminalesPaginadoRaw(pagingSort);

        // Mapeo manual de Object[] a TerminalResponseDTO
        List<TerminalResponseDTO> dtos = pageResult.getContent().stream()
                .map(row -> {
                    try {
                        TerminalResponseDTO dto = new TerminalResponseDTO();
                        dto.setId(((Number) row[0]).longValue());
                        dto.setName((String) row[1]);
                        dto.setNumerationId(row[2] != null ? ((Number) row[2]).longValue() : null);
                        dto.setPrefix((String) row[3]);
                        dto.setInitialNumber(row[4] != null ? ((Number) row[4]).intValue() : null);
                        dto.setFinalNumber(row[5] != null ? ((Number) row[5]).intValue() : null);
                        dto.setNumberUser(((Number) row[6]).longValue());
                        dto.setStatus((String) row[7]);

                        // Parsear el JSON de usuarios
                        String usersJson = (String) row[8];
                        if (usersJson != null && !usersJson.equals("[]")) {
                            List<UserListResponseDTO> users = objectMapper.readValue(
                                    usersJson,
                                    new TypeReference<List<UserListResponseDTO>>() {}
                            );
                            dto.setUsers(users);
                        } else {
                            dto.setUsers(List.of()); // Lista vacía si no hay usuarios
                        }

                        return dto;
                    } catch (Exception e) {
                        throw new RuntimeException("Error mapeando terminal: " + e.getMessage(), e);
                    }
                })
                .toList();

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
