package com.oasis.hms.dao;

import com.oasis.hms.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Toyin on 2/1/19.
 */
@Repository
public interface PrescriptionDao extends JpaRepository<Prescription, Long> {
}
