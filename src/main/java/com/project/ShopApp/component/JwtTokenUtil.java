package com.project.ShopApp.component;

import com.project.ShopApp.exception.ErrorResponse;
import com.project.ShopApp.exception.ErrorResult;
import com.project.ShopApp.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secretKey}")
    private String secretKey;
    public String generateToken(User user){
//        this.generateSecretKey();
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ErrorResponse(ErrorResult.JWT_CANT_BE_GENERATED);
        }
    }

    public Key getSignInKey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        return Encoders.BASE64.encode(keyBytes);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public  <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token){
        Date expirationDate = this.extractClaims(token,Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String extractPhoneNumber(String token){
        return extractClaims(token,Claims::getSubject);
    }

    public boolean validateToke(String token, UserDetails userDetails){
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()) &&
                !isTokenExpired(token));
    }
}
