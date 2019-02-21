package com.oasis.hms.service.impl;

import com.oasis.hms.dao.UserDao;
import com.oasis.hms.model.User;
import com.oasis.hms.service.UserService;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;


	public List<User> findAll() {
		List<User> list = new ArrayList<>();
		userDao.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public Page<User> findByCriteria(BooleanExpression expression, Pageable pageRequest) {
		return userDao.findAll(expression, pageRequest);
	}

	@Override
	public void delete(long id) {
		userDao.deleteById(id);
	}

	@Override
	public User findOne(String username) {
		return userDao.findByEmail(username);
	}

	@Override
	public User findById(Long id) {
		return userDao.findById(id).orElse(null);
	}

	@Override
    public User save(User user) {
		user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userDao.save(user);
    }
}
