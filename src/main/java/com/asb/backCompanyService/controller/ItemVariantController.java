package com.asb.backCompanyService.controller;

import com.asb.backCompanyService.business.Interfaces.IItemVariantBusiness;
import com.asb.backCompanyService.dto.request.ItemVariantDto;
import com.asb.backCompanyService.dto.responde.GenericResponse;
import com.asb.backCompanyService.dto.responde.ListGenericResponseOut;
import com.asb.backCompanyService.model.ItemVariant;
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
@RequestMapping("/${app.request.prefix}/${app.request.version}${app.request.mappings}/item-variant")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@Slf4j
public class ItemVariantController 
{
    private final IItemVariantBusiness IItemVariantBusiness;

    @PostMapping("/create")
    public ResponseEntity<ItemVariantDto> save(@RequestBody ItemVariantDto itemVariantDto) {
        ItemVariantDto savedItemVariant = IItemVariantBusiness.save(itemVariantDto);
        return ResponseEntity.ok(savedItemVariant);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ItemVariantDto> get(@PathVariable("id") long id) {
        ItemVariantDto itemVariantDto = IItemVariantBusiness.get(id);
        return ResponseEntity.ok(itemVariantDto);
    }

    @GetMapping
    public ResponseEntity<Page<ItemVariant>> getAll(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "ASC") String orders,
                                                    @RequestParam(defaultValue = "variant_id") String sortBy) {
        Page<ItemVariant> itemVariants = IItemVariantBusiness.getAll(page, size, orders, sortBy);
        return ResponseEntity.ok(itemVariants);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ItemVariant>> search(@RequestParam Map<String, String> customQuery) {
        Page<ItemVariant> itemVariants = IItemVariantBusiness.searchCustom(customQuery);
        return ResponseEntity.ok(itemVariants);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<GenericResponse> update(@PathVariable("id") long itemVariantId,
                                                     @RequestBody ItemVariantDto itemVariantDto) {
        log.info("Iniciando actualización para ItemVariant con ID: {} y DTO: {}", itemVariantId, itemVariantDto);
        GenericResponse response = IItemVariantBusiness.update(itemVariantId, itemVariantDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItemVariant(@PathVariable Long id) {
        IItemVariantBusiness.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/no-page/getItemVariant")
    public ResponseEntity<ListGenericResponseOut> getAllItemVariants(@RequestParam Map<String, String> customQuery) {
        log.info("El endpoint para obtener una lista de variantes ha sido iniciado");
        ListGenericResponseOut response = (ListGenericResponseOut) IItemVariantBusiness.getAllItemVariants(customQuery);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/no-page/getAllItemVariants")
    public ResponseEntity<List<ItemVariant>> getAllItemVariants() {
        log.info("Iniciando endpoint para obtener todas las variantes");
        List<ItemVariant> itemVariants = IItemVariantBusiness.getAllItemVariants();
        return new ResponseEntity<>(itemVariants, HttpStatus.OK);
    }
}
