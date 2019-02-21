package com.oasis.hms.dao;

import com.oasis.hms.model.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by Toyin on 2/1/19.
 */
@Repository
public interface DrugDao extends JpaRepository<Drug, Long>,
        QuerydslPredicateExecutor<Drug> {

    Drug findByNameEquals(String name);
}
