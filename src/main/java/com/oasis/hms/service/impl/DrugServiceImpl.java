package com.oasis.hms.service.impl;

import com.oasis.hms.dao.DrugDao;
import com.oasis.hms.dao.DrugGroupDao;
import com.oasis.hms.dao.DrugInventoryDao;
import com.oasis.hms.dto.DrugInventoryDto;
import com.oasis.hms.model.Drug;
import com.oasis.hms.model.DrugGroup;
import com.oasis.hms.model.DrugInventory;
import com.oasis.hms.model.Prescription;
import com.oasis.hms.service.DrugService;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by Toyin on 2/3/19.
 */
@Service
public class DrugServiceImpl implements DrugService {
    private DrugDao drugDao;
    private DrugGroupDao drugGroupDao;
    private DrugInventoryDao drugInventoryDao;

    @Autowired
    public DrugServiceImpl(DrugDao drugDao, DrugGroupDao drugGroupDao, DrugInventoryDao drugInventoryDao) {
        this.drugDao = drugDao;
        this.drugGroupDao = drugGroupDao;
        this.drugInventoryDao = drugInventoryDao;
    }

    @Override
    public DrugGroup createDrugGroup(DrugGroup drugGroup) {
        return drugGroupDao.saveAndFlush(drugGroup);
    }

    @Override
    public Drug createDrug(DrugGroup drugGroup, Drug drug) {
        drug.setGroup(drugGroup);
        drug = drugDao.saveAndFlush(drug);
        DrugInventory inventory = new DrugInventory();
        inventory.setDrug(drug);
        drugInventoryDao.saveAndFlush(inventory);
        return drug;
    }

    @Override
    public Page<DrugInventory> findByCriteria(BooleanExpression expression, Pageable pageRequest) {
        return drugInventoryDao.findAll(expression, pageRequest);
    }

    @Transactional
    @Override
    public Set<Prescription> depleteInventory(Set<Prescription> prescriptions) {
        prescriptions.forEach(p -> {
            drugInventoryDao.findByDrugId(p.getDrug().getId()).ifPresent(inventory -> {
                if (inventory.getCount() != 0 && p.getCount() != 0) {
                    if (inventory.getCount() >= p.getCount()) {
                        inventory.setCount(inventory.getCount() - p.getCount());
                        p.setSold(p.getCount());
                    } else {
                        p.setSold(inventory.getCount());
                        inventory.setCount(0);
                    }
                    inventory.setModifiedAt(LocalDateTime.now());
                    drugInventoryDao.saveAndFlush(inventory);
                }
            });
        });
        return prescriptions;
    }

    @Override
    public void stockInventory(Set<DrugInventoryDto> drugCounts) {
        drugCounts.forEach(d -> {
            drugInventoryDao.findByDrugId(d.getDrugId()).ifPresent(inventory -> {
                inventory.setCount(inventory.getCount() + d.getCount());
                inventory.setModifiedAt(LocalDateTime.now());
                drugInventoryDao.saveAndFlush(inventory);
            });
        });
    }

    @Override
    public Drug findDrugByName(String name) {
        return drugDao.findByNameEquals(name);
    }

    @Override
    public DrugGroup findDrugGroupByName(String name) {
        return drugGroupDao.findByNameEquals(name);
    }

    @Override
    public Drug findDrugById(Long id) {
        return drugDao.findById(id).orElse(null);
    }

    @Override
    public DrugGroup findDrugGroupById(Long id) {
        return drugGroupDao.getOne(id);
    }
}
