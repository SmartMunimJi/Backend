package smartmunimji.backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import smartmunimji.backend.services.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtil jwtUtil;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final AdminUserDetailsService adminUserDetailsService;
    private final SellerUserDetailsService sellerUserDetailsService;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, CustomerUserDetailsService customerUserDetailsService,
                     AdminUserDetailsService adminUserDetailsService, SellerUserDetailsService sellerUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customerUserDetailsService = customerUserDetailsService;
        this.adminUserDetailsService = adminUserDetailsService;
        this.sellerUserDetailsService = sellerUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                logger.warn("Failed to extract username from token: {}", e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                userDetails = customerUserDetailsService.loadUserByUsername(username);
            } catch (Exception e) {
                try {
                    userDetails = adminUserDetailsService.loadUserByUsername(username);
                } catch (Exception e2) {
                    try {
                        userDetails = sellerUserDetailsService.loadUserByUsername(username);
                    } catch (Exception e3) {
                        logger.warn("No user found for username: {}", username);
                    }
                }
            }

            if (userDetails != null && jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("Authenticated user: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}