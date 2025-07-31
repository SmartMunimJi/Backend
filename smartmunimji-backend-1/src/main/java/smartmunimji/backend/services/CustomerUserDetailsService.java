package smartmunimji.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import smartmunimji.backend.daos.CustomerDao;
import smartmunimji.backend.entities.Customer;

import java.util.Collections;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerUserDetailsService.class);

    private final CustomerDao customerDao;

    @Autowired
    public CustomerUserDetailsService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading user details for email: {}", username);
        Customer customer = customerDao.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + username));
        if (!customer.getIsActive()) {
            throw new UsernameNotFoundException("Customer account is inactive");
        }
        return new User(customer.getEmail(), customer.getPasswordHash(),
                Collections.singletonList(() -> "ROLE_CUSTOMER"));
    }
}