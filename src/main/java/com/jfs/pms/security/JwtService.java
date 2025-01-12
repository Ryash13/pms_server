package com.jfs.pms.security;

import com.jfs.pms.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${APPLICATION.SECURITY.JWT_SECRET}")
    private String SECRET_KEY;

    @Value("${APPLICATION.SECURITY.JWT_EXPIRATION}")
    private long EXPIRATION_DURATION;

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, User user) {
        String username = extractUsername(token);
        return (username.equalsIgnoreCase(user.getEmail()) && !isTokenExpired(token));
    }

    public String generateToken(User user) {
        return generateAuthToken(user, new HashMap<>());
    }

    private String generateAuthToken(User user, HashMap<String, Object> extraClaims) {
        var authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DURATION))
                .claim("authorities", authorities)
                .claim("public_id", user.getPublicId().toString())
                .setIssuer("JFS-PMS-API")
                .signWith(getSigningKey())
                .compact();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                ;
    }

    private Key getSigningKey() {
        byte[] keyBite = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBite);
    }
}
