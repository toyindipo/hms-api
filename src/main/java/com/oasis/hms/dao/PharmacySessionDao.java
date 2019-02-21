package com.oasis.hms.dao;

import com.oasis.hms.model.PharmacySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Toyin on 2/1/19.
 */
@Repository
public interface PharmacySessionDao extends JpaRepository<PharmacySession, Long>  {
}
