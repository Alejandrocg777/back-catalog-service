package com.asb.backCompanyService.business.Imple;

import com.asb.backCompanyService.business.Interfaces.ICompanyBusiness;
import com.asb.backCompanyService.dto.request.CompanyDto;
import com.asb.backCompanyService.dto.responde.CompanyResponseDto;
import com.asb.backCompanyService.exception.CustomErrorException;
import com.asb.backCompanyService.exception.GenericException;
import com.asb.backCompanyService.model.Company;
import com.asb.backCompanyService.repository.CompanyRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class CompanyService implements ICompanyBusiness {

    private final CompanyRepository companyRepository;
    private final Cloudinary cloudinary;


    @Override
    @Transactional
    public CompanyDto save(CompanyDto companyDto) {
        if (companyDto.getId() != null && companyRepository.existsById(companyDto.getId())) {
            throw new CustomErrorException(HttpStatus.BAD_REQUEST, "La empresa ya existe");
        }

        Company company = new Company();
        company.setCompanyName(companyDto.getCompanyName());
        company.setNit(companyDto.getNit());
        company.setAddress(companyDto.getAddress());
        company.setEmail(companyDto.getEmail());
        company.setPhone(companyDto.getPhone());
        company.setEconomicActivityId(companyDto.getEconomicActivityId());
        company.setStatus("ACTIVE");

        Company savedCompany = companyRepository.save(company);
        CompanyDto savedCompanyDto = new CompanyDto();
        BeanUtils.copyProperties(savedCompany, savedCompanyDto);
        return savedCompanyDto;
    }

    @Override
    @Transactional
    public CompanyDto update(Long id, CompanyDto companyDto, MultipartFile imageFile) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new GenericException("La empresa no fue encontrada por el id " + id, HttpStatus.NOT_FOUND));

        // Actualizar campos de texto
        if (companyDto.getCompanyName() != null) company.setCompanyName(companyDto.getCompanyName());
        if (companyDto.getNit() != null) company.setNit(companyDto.getNit());
        if (companyDto.getAddress() != null) company.setAddress(companyDto.getAddress());
        if (companyDto.getEmail() != null) company.setEmail(companyDto.getEmail());
        if (companyDto.getPhone() != null) company.setPhone(companyDto.getPhone());
        if (companyDto.getEconomicActivityId() != null) company.setEconomicActivityId(companyDto.getEconomicActivityId());
        if (companyDto.getStatus() != null) company.setStatus(companyDto.getStatus());

        // Manejo de la imagen (opcional)
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                String imageUrl = (String) uploadResult.get("url");
                company.setImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen a Cloudinary", e);
            }
        }
        // Si no se envía imagen, se mantiene la existente (no se sobrescribe con null o vacío)

        Company updatedCompany = companyRepository.save(company);

        CompanyDto updatedDto = new CompanyDto();
        BeanUtils.copyProperties(updatedCompany, updatedDto);
        return updatedDto;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new GenericException("La empresa no fue encontrada por el id " + id, HttpStatus.NOT_FOUND));
        company.setStatus("INACTIVE");
        companyRepository.save(company);
        return true;
    }

    @Override
    public CompanyResponseDto get(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new GenericException("No existe la compañia" , HttpStatus.NOT_FOUND);
        }
        List<Object[]> companys = companyRepository.getCompanyIds(id);
        CompanyResponseDto companyDto = new CompanyResponseDto();
        for(Object[] company: companys) {
            companyDto.setId(company[0] != null ? Long.parseLong(company[0].toString()) : null);
            companyDto.setCompanyName(company[1] != null ? company[1].toString() : null);
            companyDto.setNit(company[2] != null ? company[2].toString() : null);
            companyDto.setAddress(company[3] != null ? company[3].toString() : null);
            companyDto.setEmail(company[4] != null ? company[4].toString() : null);
            companyDto.setPhone(company[5] != null ? company[5].toString() : null);
            companyDto.setPhone(company[5] != null ? company[5].toString() : null);
            companyDto.setDescription(company[6] != null ? company[6].toString() : null);
            companyDto.setCiiuCode(company[7] != null ? company[7].toString() : null);
            companyDto.setEconomicActivityId(company[8] != null ? Long.parseLong(company[8].toString()) : null);
            companyDto.setImage(company[9] != null ? company[9].toString() : null);
        }
        return companyDto;
    }

    @Override
    @Transactional
    public boolean setStatus(Long id, String status) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new GenericException("La empresa no fue encontrada por el id " + id, HttpStatus.NOT_FOUND));
        company.setStatus(status);
        companyRepository.save(company);
        return true;
    }

    public Page<CompanyResponseDto> getAll(int page, int size, String orders, String sortBy) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero");
        }
        Sort.Direction direction = Sort.Direction.fromString(orders);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Object[]> results = companyRepository.getStatus(pageable);
        return results.map(result -> {
            CompanyResponseDto dto = new CompanyResponseDto();
            dto.setId((Long) result[0]);
            dto.setCompanyName((String) result[1]);
            dto.setNit((String) result[2]);
            dto.setAddress((String) result[3]);
            dto.setEmail((String) result[4]);
            dto.setPhone((String) result[5]);
            dto.setDescription((String) result[6]);
            dto.setCiiuCode((String) result[7]);
            dto.setEconomicActivityId((Long) result[8]);
            return dto;
        });
    }


    @Override
    public Page<Company> searchCompany(Map<String, String> customQuery) {
        return null;
    }

    @Override
    public List<Company> getAllCompany() {
        return companyRepository.findByStatus("ACTIVE");
    }
}