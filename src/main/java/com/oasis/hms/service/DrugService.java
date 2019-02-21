package com.oasis.hms.service;

import com.oasis.hms.dto.DrugInventoryDto;
import com.oasis.hms.model.Drug;
import com.oasis.hms.model.DrugGroup;
import com.oasis.hms.model.DrugInventory;
import com.oasis.hms.model.Prescription;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Created by Toyin on 2/2/19.
 */
public interface DrugService {
    DrugGroup createDrugGroup(DrugGroup drugGroup);

    Drug createDrug(DrugGroup drugGroup, Drug drug);

    Page<DrugInventory> findByCriteria(BooleanExpression expression, Pageable pageRequest);

    Set<Prescription> depleteInventory(Set<Prescription> prescriptions);

    void stockInventory(Set<DrugInventoryDto> drugCounts);

    Drug findDrugByName(String name);

    DrugGroup findDrugGroupByName(String name);

    Drug findDrugById(Long id);

    DrugGroup findDrugGroupById(Long id);
}
