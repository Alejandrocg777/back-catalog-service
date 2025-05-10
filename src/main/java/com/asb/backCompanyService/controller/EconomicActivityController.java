package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IEconomicActivityBusiness;
import com.asb.backCompanyService.dto.request.EconomicActivityDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.ListGenericResponse;
import com.asb.backCompanyService.model.EconomicActivity;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/economic-activity")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class EconomicActivityController {

    private final IEconomicActivityBusiness iEconomicActivityBusiness;

    @PostMapping("/create")
    public ResponseEntity<EconomicActivityDto> save(@RequestBody EconomicActivityDto economicActivityDto) {
        EconomicActivityDto savedActivity = iEconomicActivityBusiness.save(economicActivityDto);
        return ResponseEntity.ok(savedActivity);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<EconomicActivityDto> get(@PathVariable("id") long id) {
        EconomicActivityDto economicActivityDto = iEconomicActivityBusiness.get(id);
        return ResponseEntity.ok(economicActivityDto);
    }

    @GetMapping
    public ResponseEntity<Page<EconomicActivity>> getAll(@RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "5") int size,
                                                         @RequestParam(defaultValue = "ASC") String orders,
                                                         @RequestParam(defaultValue = "economic_activity_id") String sortBy) {
        Page<EconomicActivity> activities = iEconomicActivityBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EconomicActivity>> search(@RequestParam Map<String, String> customQuery) {
        Page<EconomicActivity> activities = iEconomicActivityBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(activities);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long activityId,
                                                     @RequestBody EconomicActivityDto economicActivityDto) {
        log.info("Iniciando actualización para EconomicActivity con ID: {} y DTO: {}", activityId, economicActivityDto);
        GenericResponse response = iEconomicActivityBusiness.update(activityId, economicActivityDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        iEconomicActivityBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-page/getEconomicActivity")
    public ResponseEntity<ListGenericResponse> getAllEconomicActivity(@RequestParam Map<String, String> customQuery) {
        log.info("The endpoint for obtaining a list of economic activities is initiated");
        ListGenericResponse response = (ListGenericResponse) iEconomicActivityBusiness.getAllEconomicActivity(customQuery);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/no-page/getAllEconomicActivity")
    public ResponseEntity<List<EconomicActivity>> getAllEconomic() {
        log.info("Starting endpoint to get all economic activities");
        List<EconomicActivity> economicActivities = iEconomicActivityBusiness.getAllEconomic();
        return new ResponseEntity<>(economicActivities, HttpStatus.OK);
    }



}
