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
import org.springframework.stereotype.Component;
import smartmunimji.backend.entities.Customer;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${jwt.token.expiration.millis:86400000}")
    private long jwtExpiration;

    @Value("${jwt.token.secret:defaultSecretKeyWithSufficientLengthForHS256Algorithm}")
    private String jwtSecret;

    private Key jwtKey;

    @PostConstruct
    public void init() {
        jwtKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String createToken(Authentication auth) {
        Customer user = (Customer) auth.getPrincipal();
        String subject = String.valueOf(user.getId()); // Calls getId() which is now available
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
            return null; // Invalid token
        }
    }
}