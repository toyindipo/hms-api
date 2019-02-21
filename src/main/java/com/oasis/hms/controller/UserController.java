package com.oasis.hms.controller;

import com.oasis.hms.dao.predicate.CustomPredicateBuilder;
import com.oasis.hms.dao.predicate.Operation;
import com.oasis.hms.model.User;
import com.oasis.hms.model.enums.Role;
import com.oasis.hms.service.UserService;
import com.oasis.hms.utils.PageUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<Page<User>> listUser(@RequestParam(value="page", required = false, defaultValue = "0") int page,
          @RequestParam(value="pageSize", defaultValue = "10") int pageSize, @RequestParam(value="firstname", required = false) String firstname,
          @RequestParam(value="lastname", required = false) String lastname, @RequestParam(value="email", required = false) String email,
                                               @RequestParam(value="phoneNumber", required = false) String phoneNumber,
                                               @RequestParam(value="role", required = false) Role role) {
        BooleanExpression filter = new CustomPredicateBuilder<User>("user", User.class)
                .with("firstname", Operation.LIKE, firstname)
                .with("lastname", Operation.LIKE, lastname)
                .with("email", Operation.LIKE, email)
                .with("phoneNumber", Operation.LIKE, phoneNumber)
                .with("role", Operation.ENUM, role).build();
        Pageable pageRequest =
                PageUtil.createPageRequest(page, pageSize,
                        Sort.by(Sort.Order.asc("firstname"), Sort.Order.asc("lastname")));
        return ResponseEntity.ok(userService.findByCriteria(filter, pageRequest));
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getOne(@PathVariable(value = "id") Long id){
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        } else if (userService.findOne(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(userService.save(user));
    }


    @GetMapping(value = "/me")
    public ResponseEntity<User> getUserInSession(@AuthenticationPrincipal Principal principal){
        return ResponseEntity.ok(userService.findOne(principal.getName()));
    }
}
