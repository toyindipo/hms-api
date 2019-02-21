package com.oasis.hms.security;

import com.oasis.hms.dao.UserDao;
import com.oasis.hms.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Toyin on 2/17/19.
 */

@Service
public class ProfileDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        return authorities;
        //return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
