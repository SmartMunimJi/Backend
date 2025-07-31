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
public class SellerUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(SellerUserDetailsService.class);

    private final CustomerDao customerDao;

    @Autowired
    public SellerUserDetailsService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading seller details for email: {}", username);
        Customer seller = customerDao.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Seller not found with email: " + username));
        if (!seller.getIsActive()) {
            throw new UsernameNotFoundException("Seller account is inactive");
        }
        return new User(seller.getEmail(), seller.getPasswordHash(),
                Collections.singletonList(() -> "ROLE_SELLER"));
    }
}