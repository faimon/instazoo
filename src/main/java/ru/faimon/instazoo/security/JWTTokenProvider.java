package ru.faimon.instazoo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.faimon.instazoo.entity.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenProvider {
    public static final Logger LOG = LoggerFactory.getLogger(JWTTokenProvider.class);

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Date nowDate = new Date(System.currentTimeMillis());
        Date expireDate = new Date(nowDate.getTime() + SecurityConstants.EXPIRATION_TIME);
        String userId = Long.toString(user.getId());
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", user.getEmail());
        claims.put("firstname", user.getFirstname());
        claims.put("lastname", user.getLastname());
        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(expireDate)
                .setSubject(userId)
                .setIssuedAt(nowDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();
        String userId = (String) claims.get("id");
        return Long.parseLong(userId);
    }
}
