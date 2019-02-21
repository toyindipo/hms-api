package com.oasis.hms.dao;

import com.oasis.hms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long>,
        QuerydslPredicateExecutor<User> {
    User findByEmail(String username);
}
