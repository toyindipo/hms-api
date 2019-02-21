package com.oasis.hms.controller;

import com.oasis.hms.dao.predicate.CustomPredicateBuilder;
import com.oasis.hms.dao.predicate.Operation;
import com.oasis.hms.dto.DrugDto;
import com.oasis.hms.dto.DrugInventoryDto;
import com.oasis.hms.model.Drug;
import com.oasis.hms.model.DrugGroup;
import com.oasis.hms.model.DrugInventory;
import com.oasis.hms.service.DrugService;
import com.oasis.hms.utils.PageUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

/**
 * Created by Toyin on 2/4/19.
 */
@RestController
@RequestMapping("/api/v1/drugs")
public class DrugController {
    private DrugService drugService;

    @Autowired
    public DrugController(DrugService drugService) {
        this.drugService = drugService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @PostMapping(value = "/groups")
    public ResponseEntity<DrugGroup> createDrugGroup(@RequestBody @Valid DrugGroup drugGroup,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            if (drugService.findDrugGroupByName(drugGroup.getName()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else return ResponseEntity.ok(drugService.createDrugGroup(drugGroup));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACY')")
    @PostMapping("{/groupId}")
    public ResponseEntity<Drug> createDrug(@PathVariable("/groupId") Long groupId, @RequestBody @Valid DrugDto drugDto,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            if (drugService.findDrugByName(drugDto.getName()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else {
                DrugGroup drugGroup = drugService.findDrugGroupById(groupId);
                if (drugGroup == null) {
                    ResponseEntity.notFound();
                }
                return ResponseEntity.ok(drugService.createDrug(drugGroup, drugDto.toDrug()));
            }
        }
    }

    @GetMapping
    public ResponseEntity<Page<DrugInventory>> listDrugs(@RequestParam(value="page",
            required = false, defaultValue = "0") int page,
            @RequestParam(value="pageSize", defaultValue = "10") int pageSize, @RequestParam(value="name", required = false) String name,
            @RequestParam(value="group", required = false) String group) {
        BooleanExpression filter = new CustomPredicateBuilder<DrugInventory>("drugInventory", DrugInventory.class)
                .with("drug.name", Operation.LIKE, name)
                .with("drug.group.name", Operation.LIKE, group).build();
        Pageable pageRequest =
                PageUtil.createPageRequest(page, pageSize,
                        Sort.by(Sort.Order.asc("drug.name"), Sort.Order.asc("drug.group.name")));
        return ResponseEntity.ok(drugService.findByCriteria(filter, pageRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/inventories/stock")
    public ResponseEntity<?> stockInventory(@RequestBody @Valid Set<DrugInventoryDto> inventoryDtos,
                                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else {
            drugService.stockInventory(inventoryDtos);
            return ResponseEntity.ok().build();
        }
    }
}
