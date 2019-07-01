package com.niloy.poc.customer.search.controller;

import com.niloy.poc.customer.search.entity.Customer;
import com.niloy.poc.customer.search.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * @author Niloy Saha<niloysaha@gmail.com>
 */
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<Customer> findCustomer(@ModelAttribute Customer customer) throws Exception {
        Customer output = customerService.findCustomer(customer);
        if(output == null)
           return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(output);
    }
}
