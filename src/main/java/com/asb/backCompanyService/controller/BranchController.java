package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IBranchBusiness;
import com.asb.backCompanyService.dto.request.BranchDto;
import com.asb.backCompanyService.dto.request.BranchDtoRequest;
import com.asb.backCompanyService.dto.responde.BranchDtoResponse;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.model.Branch;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/branch")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class BranchController {

    private final IBranchBusiness iBranchBusiness;

    @PostMapping("/create")
    public ResponseEntity<BranchDtoResponse> save(@RequestBody BranchDtoRequest branchDto) {
        BranchDtoResponse savedBranch = iBranchBusiness.save(branchDto);
        return ResponseEntity.ok(savedBranch);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BranchDto> get(@PathVariable("id") long id) {
        BranchDto branchDto = iBranchBusiness.get(id);
        return ResponseEntity.ok(branchDto);
    }

    @GetMapping
    public ResponseEntity<Page<BranchDtoResponse>> getAll(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size,
                                                          @RequestParam(defaultValue = "ASC") String orders,
                                                          @RequestParam(defaultValue = "id") String sortBy) {
        Page<BranchDtoResponse> branches = iBranchBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BranchDtoResponse>> search(@RequestParam Map<String, String> customQuery) {
        Page<BranchDtoResponse> branches = iBranchBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(branches);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") Long branchId,
                                                  @RequestBody BranchDtoRequest branchDto) {
        log.info("Starting update endpoint for branch with ID: {} and DTO: {}", branchId, branchDto);
        GenericResponse response = iBranchBusiness.update(branchId, branchDto);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteBranches(@PathVariable Long id) {
        iBranchBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-page/getAllBranch")
    public ResponseEntity<List<Branch>> getAllBranches() {
        log.info("Starting endpoint to get all branches");
        List<Branch> branches = iBranchBusiness.getAllBranches();
        return new ResponseEntity<>(branches, HttpStatus.OK);
    }
}
