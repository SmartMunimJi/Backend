package smartmunimji.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import smartmunimji.backend.daos.CustomerDao;
import java.util.Collections;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return customerDao.findByEmail(email)
            .map(customer -> new User(
                customer.getEmail(),
                customer.getPassword(),
                Collections.singleton(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_CUSTOMER"))
            ))
            .orElseThrow(() -> new UsernameNotFoundException("Customer not found: " + email));
    }
}