package com.atl23.authapp.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtHelper {

    @Value("${jwt.expire}")
    private Long EXPIRE;
    @Value("${jwt.secret}")
    private String SECRET_KEY;



    private SecretKey secretKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username){
          return Jwts.builder()
                  .subject(username)
                  .expiration(new Date(System.currentTimeMillis()+EXPIRE))
                  .signWith(secretKey())
                  .compact();
    }

    public String tokenByDeCoder(String token){
        var parse = Jwts.parser()
                .verifyWith(secretKey())
                .build();
        return parse.parseSignedClaims(token).getPayload()
                .getSubject();
    }



}
