package smartmunimji.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import smartmunimji.backend.daos.SellerDao;
import smartmunimji.backend.entities.Seller;
import java.util.Optional;

@Service
public class SellerUserDetailsService implements UserDetailsService {

    @Autowired
    private SellerDao sellerDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Seller> sellerOptional = sellerDao.findBySellersEmail(username);
        return sellerOptional.orElseThrow(() -> 
            new UsernameNotFoundException("Seller not found with email: " + username));
    }
}