package com.niloy.poc.customer.search.service;

import com.niloy.poc.customer.search.controller.CustomerController;
import com.niloy.poc.customer.search.entity.Customer;
import com.niloy.poc.customer.search.repository.CustomerRepository;
import com.niloy.poc.customer.search.util.SearchEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Niloy Saha<niloysaha@gmail.com>
 */
@Service
public class CustomerService {

    private static Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    public Customer findById(int id) {
       return customerRepository.findById(id);
    }

    public Customer findCustomer(Customer customer) throws Exception {
        SearchEngine<Customer> engine = new SearchEngine<>();
        String queryAppender = engine.generateQuery(customer);

        log.info("Generated where clause by search engine ::: " + queryAppender);

        return customerRepository.find(queryAppender);
    }
}
