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
public class AdminUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserDetailsService.class);

    private final CustomerDao customerDao;

    @Autowired
    public AdminUserDetailsService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading admin details for email: {}", username);
        Customer admin = customerDao.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + username));
        if (!admin.getIsActive()) {
            throw new UsernameNotFoundException("Admin account is inactive");
        }
        return new User(admin.getEmail(), admin.getPasswordHash(),
                Collections.singletonList(() -> "ROLE_ADMIN"));
    }
}