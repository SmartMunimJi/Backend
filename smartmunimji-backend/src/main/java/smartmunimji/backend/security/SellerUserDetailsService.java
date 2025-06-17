package smartmunimji.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import smartmunimji.backend.daos.SellerDao;
import java.util.Collections;

@Service
public class SellerUserDetailsService implements UserDetailsService {

    @Autowired
    private SellerDao sellerDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return sellerDao.findBySellersEmail(email)
            .map(seller -> new User(
                seller.getSellersEmail(),
                seller.getPassword(),
                Collections.singleton(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_SELLER"))
            ))
            .orElseThrow(() -> new UsernameNotFoundException("Seller not found: " + email));
    }
}