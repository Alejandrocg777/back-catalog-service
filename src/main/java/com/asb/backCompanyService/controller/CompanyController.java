package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.ICompanyBusiness;
import com.asb.backCompanyService.dto.request.CompanyDto;
import com.asb.backCompanyService.dto.responde.CompanyResponseDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/company")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class CompanyController {

    private final ICompanyBusiness iCompanyBusiness;

    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = iCompanyBusiness.getAllCompany();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CompanyResponseDto> getCompanyById(@PathVariable Long id) {
        CompanyResponseDto companyResponse = iCompanyBusiness.get(id);
        return ResponseEntity.ok(companyResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto company) {
        CompanyDto createdCompany = iCompanyBusiness.save(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable Long id, @RequestBody CompanyDto companyDto) {
        CompanyDto updatedCompany = iCompanyBusiness.update(id, companyDto);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        iCompanyBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/set-status/{id}")
    public ResponseEntity<Void> setStatus(@PathVariable("id") Long id, @RequestParam String status) {
        boolean updated = iCompanyBusiness.setStatus(id, status);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<CompanyResponseDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int size,
                                                           @RequestParam(defaultValue = "ASC") String orders,
                                                           @RequestParam(defaultValue = "company_id") String sortBy) {
        Page<CompanyResponseDto> companies = iCompanyBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Company>> search(@RequestParam Map<String, String> customQuery) {
        Page<Company> companyPage = iCompanyBusiness.searchCompany(customQuery);
        return ResponseEntity.ok(companyPage);
    }

    @GetMapping("/no-page/getAllCompanies")
    public ResponseEntity<List<Company>> getAllCities() {
        List<Company> companies = iCompanyBusiness.getAllCompany();
        return ResponseEntity.ok(companies);
    }
}
