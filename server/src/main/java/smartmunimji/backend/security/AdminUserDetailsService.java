package smartmunimji.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import smartmunimji.backend.daos.AdminDao;
import java.util.Collections;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminDao adminDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return adminDao.findByEmail(email)
            .map(admin -> new User(
                admin.getEmail(),
                admin.getPassword(),
                Collections.singleton(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"))
            ))
            .orElseThrow(() -> new UsernameNotFoundException("Admin not found: " + email));
    }
}