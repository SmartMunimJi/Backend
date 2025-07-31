package smartmunimji.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import smartmunimji.backend.services.CustomerUserDetailsService;
import smartmunimji.backend.services.AdminUserDetailsService;
import smartmunimji.backend.services.SellerUserDetailsService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final String secret;
    private final Long expiration;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final AdminUserDetailsService adminUserDetailsService;
    private final SellerUserDetailsService sellerUserDetailsService;

    @Autowired
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") Long expiration,
                   CustomerUserDetailsService customerUserDetailsService,
                   AdminUserDetailsService adminUserDetailsService,
                   SellerUserDetailsService sellerUserDetailsService) {
        this.secret = secret;
        this.expiration = expiration;
        this.customerUserDetailsService = customerUserDetailsService;
        this.adminUserDetailsService = adminUserDetailsService;
        this.sellerUserDetailsService = sellerUserDetailsService;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication) {
        String username = (String) authentication.getPrincipal(); // Principal is typically the username/email
        UserDetails userDetails = loadUserDetails(username);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", authentication.getAuthorities());

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey())
                .compact();
        logger.debug("Generated JWT for user: {}", userDetails.getUsername());
        return token;
    }

    private UserDetails loadUserDetails(String username) {
        try {
            return customerUserDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            try {
                return adminUserDetailsService.loadUserByUsername(username);
            } catch (Exception e2) {
                try {
                    return sellerUserDetailsService.loadUserByUsername(username);
                } catch (Exception e3) {
                    logger.error("Failed to load user details for username: {}", username);
                    throw new IllegalArgumentException("User not found: " + username);
                }
            }
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        logger.debug("Token validation for user {}: {}", username, isValid);
        return isValid;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}