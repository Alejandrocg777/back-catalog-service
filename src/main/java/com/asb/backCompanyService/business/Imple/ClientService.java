package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.ClientBusiness;
import com.asb.backCompanyService.dto.request.ClientRequestDTO;
import com.asb.backCompanyService.dto.responde.ClientResponseDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.model.Client;
import com.asb.backCompanyService.repository.ClientRepository;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j

public class ClientService implements ClientBusiness {

    private final ClientRepository clientRepository;

    @Override
    public ClientRequestDTO save(ClientRequestDTO request) {
        Client client = new Client();
        client.setName(request.getName());
        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());
        client.setStatus(request.getStatus());
        client.setCityId(request.getCityId());
        client.setIdentification(request.getIdentification());
        Client newClient = clientRepository.save(client);

        ClientRequestDTO response = new ClientRequestDTO();
        BeanUtils.copyProperties(newClient, response);
        return response;
    }

    @Override
    public GenericResponse update(Long id, ClientRequestDTO requestDTO) {
        if (!clientRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "Client no existe");

        Optional<Client> clientOptional = clientRepository.findById(id);

        Client client = clientOptional.get();
        client.setName(requestDTO.getName());
        client.setCityId(requestDTO.getCityId());
        client.setStatus(requestDTO.getStatus());
        client.setIdentification(requestDTO.getIdentification());
        client.setAddress(requestDTO.getAddress());
        client.setPhone(requestDTO.getPhone());
        clientRepository.save(client);

        return new GenericResponse("client actualizado con exito", 200);
    }

    @Override
    public Boolean delete(Long id) {
        if (clientRepository.existsById(id)) {
            Client client = clientRepository.findById(id).get();
            client.setStatus("INACTIVE");
            clientRepository.save(client);
            return true;
        } else {
            throw new RuntimeException("el Cliente no fue encontrada por el id " + id);
        }
    }

    @Override
    public Page<ClientResponseDTO> getAll(int page, int size, String orders, String sortBy) {
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pagingSort = PageRequest.of(page, size, sort);
        return clientRepository.getStatus(pagingSort);
    }

    @Override
    public ClientRequestDTO get(Long id) {
        if (!clientRepository.existsById(id)) throw new CustomErrorException(HttpStatus.BAD_REQUEST, "client no existe");

        Optional<Client> clientOptional = clientRepository.findById(id);

        ClientRequestDTO response = new ClientRequestDTO();
        response.setName(clientOptional.get().getName());
        response.setAddress(clientOptional.get().getAddress());
        response.setStatus(clientOptional.get().getStatus());
        response.setPhone(clientOptional.get().getPhone());
        response.setIdentification(clientOptional.get().getIdentification());
        response.setCityId(clientOptional.get().getCityId());
        return response;
    }

    @Override
    public List<ClientResponseDTO> getAllNoPage(@RequestParam Map<String, String> customQuery) {
        try {

            String id = null;
            String name = null;
            String phone = null;
            String identification = null;
            String address = null;
            String cityName = null;
            String status = null;

            if (customQuery.containsKey("id")) {
                id = "%" + customQuery.get("id") + "%";
            }

            if (customQuery.containsKey("name")) {
                name = "%" + customQuery.get("name") + "%";
            }

            if (customQuery.containsKey("identification")) {
                identification = "%" + customQuery.get("identification") + "%";
            }

            if (customQuery.containsKey("address")) {
                address = "%" + customQuery.get("address") + "%";
            }

            if (customQuery.containsKey("phone")) {
                phone = "%" + customQuery.get("phone") + "%";
            }

            if (customQuery.containsKey("cityName")) {
                cityName = "%" + customQuery.get("cityName") + "%";
            }

            if (customQuery.containsKey("status")) {
                status = "%" + customQuery.get("status") + "%";
            }

            List<ClientResponseDTO> response = clientRepository.searchClientNoPage(id, name, phone, identification, address, cityName, status);
            return response;
        } catch (Exception e) {
            log.error("Error al obtener el cliente");
            log.error("Causa: {}", e.getCause().toString());
            throw new RuntimeException("No se puede recuperar el cliente", e);
        }
    }

    @Override
    public Page<ClientResponseDTO> searchCustom(Map<String, String> customQuery) {
        String orders = "ASC";
        String sortBy = "id";
        int page = 0;
        int size = 6;
        String id = null;
        String name = null;
        String phone = null;
        String identification = null;
        String address = null;
        String cityName = null;
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

        if (customQuery.containsKey("name")) {
            name = "%" + customQuery.get("name") + "%";
        }

        if (customQuery.containsKey("identification")) {
            identification = "%" + customQuery.get("identification") + "%";
        }

        if (customQuery.containsKey("address")) {
            address = "%" + customQuery.get("address") + "%";
        }

        if (customQuery.containsKey("phone")) {
            phone = "%" + customQuery.get("phone") + "%";
        }

        if (customQuery.containsKey("cityName")) {
            cityName = "%" + customQuery.get("cityName") + "%";
        }

        if (customQuery.containsKey("status")) {
            status = "%" + customQuery.get("status") + "%";
        }

        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);

        Pageable pagingSort = PageRequest.of(page, size, sort);
        Page<ClientResponseDTO> searchClient = clientRepository.searchClient(id, name, phone, identification, address, cityName, status, pagingSort);

        return searchClient;
    }
}
