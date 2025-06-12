package smartmunimji.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import smartmunimji.backend.entities.Admin;
import smartmunimji.backend.entities.Customer;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${jwt.token.expiration.millis:86400000}")
    private long jwtExpiration;

    @Value("${jwt.token.secret}")
    private String jwtSecret;

    private Key jwtKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 32 bytes (256 bits) after Base64 decoding");
        }
        jwtKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication auth) {
        UserDetails user = (UserDetails) auth.getPrincipal();
        String subject;
        if (user instanceof Customer) {
            subject = String.valueOf(((Customer) user).getId());
        } else if (user instanceof Admin) {
            subject = String.valueOf(((Admin) user).getId());
        } else {
            throw new IllegalArgumentException("Unsupported user type");
        }
        String roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claim("role", roles)
                .signWith(jwtKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication validateToken(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder().setSigningKey(jwtKey).build();
            Claims claims = parser.parseClaimsJws(token).getBody();
            String custId = claims.getSubject();
            String roles = (String) claims.get("role");
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
            return new UsernamePasswordAuthenticationToken(custId, null, authorities);
        } catch (Exception e) {
            return null;
        }
    }
}