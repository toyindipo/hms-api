package com.oasis.hms.dao;

import com.oasis.hms.model.DrugInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Toyin on 2/1/19.
 */
@Repository
public interface DrugInventoryDao extends JpaRepository<DrugInventory, Long>,
        QuerydslPredicateExecutor<DrugInventory> {
    Optional<DrugInventory> findByDrugId(Long drugId);
}
