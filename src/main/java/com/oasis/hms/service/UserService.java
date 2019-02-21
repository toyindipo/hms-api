package com.oasis.hms.service;

import com.oasis.hms.model.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User save(User user);
    List<User> findAll();
    Page<User> findByCriteria(BooleanExpression expression, Pageable pageRequest);
    void delete(long id);
    User findOne(String username);

    User findById(Long id);
}
