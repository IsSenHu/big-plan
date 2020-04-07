package com.gapache.auth.sdk;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * create on 2019/8/27 17:30
 */
public class JwtManager {

    private static final String SUB = "sub";
    private static final String CREATED = "created";
    private final String secret;
    private final Integer expire;

    public JwtManager(String secret, Integer expire) {
        this.secret = secret;
        this.expire = expire;
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(SUB, username);
        claims.put(CREATED, new Date());
        return generateToken(claims);
    }

    public String getUsernameFromToken(String token) {
        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            // claims 可以获得Token的过期时间
            username = claims.getSubject();
        } catch (Exception ignored) {}
        return username;
    }

    public boolean validateToken(String token, String username) {
        String gUsername = getUsernameFromToken(token);
        return StringUtils.endsWith(gUsername, username);
    }

    private String generateToken(Map<String, Object> claims) {
        Date created = (Date) claims.get(CREATED);
        Date expirationDate = new Date(created.getTime() + TimeUnit.MINUTES.toMillis(this.expire));
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, this.secret).compact();
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        } catch (Exception ignored) {}
        return claims;
    }
}
