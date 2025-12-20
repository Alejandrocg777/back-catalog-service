package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Imple.TerminalService;
import com.asb.backCompanyService.business.Interfaces.TerminalBusiness;
import com.asb.backCompanyService.dto.request.TerminalRequestDTO;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.TerminalResponseDTO;
import com.asb.backCompanyService.model.Terminal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/terminal")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Slf4j
public class TerminalController {

    private final TerminalBusiness terminalBusiness;

    @PostMapping("/create")
    public ResponseEntity<Terminal> save(@RequestBody TerminalRequestDTO terminal) {
        return new ResponseEntity<>(terminalBusiness.save(terminal), HttpStatus.OK);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Terminal> update(@PathVariable("id")Long id,
                                           @RequestBody TerminalRequestDTO terminal) {
        return new ResponseEntity<>(terminalBusiness.update(id,terminal), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<Page<TerminalResponseDTO>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "6") Integer size,
                                                            @RequestParam(defaultValue = "ASC") String orders,
                                                            @RequestParam(defaultValue = "id") String sortBy) {
        return new ResponseEntity<>(terminalBusiness.getAll(page,size,orders,sortBy), HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponse> delete(@PathVariable("id")Long id){
        return new ResponseEntity<>(terminalBusiness.delete(id), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/get-all/no-page")
    public ResponseEntity<List<Terminal>> getAllNoPage() {
        return new ResponseEntity<>(terminalBusiness.getAllNoPage(), HttpStatus.OK);
    }

}
