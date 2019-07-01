package com.niloy.poc.customer.search.repository;

import com.niloy.poc.customer.search.entity.Customer;
import com.niloy.poc.customer.search.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Niloy Saha<niloysaha@gmail.com>
 */
@Repository
public class CustomerRepository {

    private static Logger log = LoggerFactory.getLogger(CustomerRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Customer findById(int id) {
        return jdbcTemplate.queryForObject("select * from customer where id=?",
                new Object[] {id},
                new BeanPropertyRowMapper<>(Customer.class));
    }

    public Customer find(String queryAppender) {
        Customer customer = null;
        try {
             customer = jdbcTemplate.queryForObject("select * from customer where " + queryAppender,
                    new BeanPropertyRowMapper<>(Customer.class));
        } catch (EmptyResultDataAccessException ex) {
            log.error(" No record found in DB", ex);
        }
        return customer;
    }
}
