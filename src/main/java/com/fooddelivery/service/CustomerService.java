package com.fooddelivery.service;

import com.fooddelivery.dto.CustomerRegisterRequest;
import com.fooddelivery.dto.LoginRequest;
import com.fooddelivery.entity.Customer;
import com.fooddelivery.repository.CustomerRepository;
import com.fooddelivery.util.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional
    public Customer register(CustomerRegisterRequest req) {
        Customer customer = new Customer();
        customer.setName(req.getName());
        customer.setPhone(req.getPhone());
        customer.setPassword(PasswordUtil.hash(req.getPassword()));
        customer.setAddress(req.getAddress());
        customer.setLatitude(req.getLatitude());
        customer.setLongitude(req.getLongitude());
        return customerRepository.save(customer);
    }

    public Customer login(LoginRequest req) {
        Customer customer = customerRepository.findByPhone(req.getPhone())
                .orElseThrow(() -> new RuntimeException("Invalid phone"));

        if (!PasswordUtil.matches(req.getPassword(), customer.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return customer;
    }

    @Transactional
    public Customer updateAddress(Long customerId, String address, Double lat, Double lon) {
        Customer c = customerRepository.findById(customerId).orElseThrow();
        c.setAddress(address);
        c.setLatitude(lat);
        c.setLongitude(lon);
        return customerRepository.save(c);
    }

}
