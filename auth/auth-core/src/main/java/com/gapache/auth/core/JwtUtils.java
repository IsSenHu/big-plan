package com.gapache.auth.core;

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
public class JwtUtils {

    private static final String SUB = "sub";
    private static final String CREATED = "created";
    private static final int EXPIRATION = 60;
    private static final String SECRET = "sensen-520";

    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(SUB, username);
        claims.put(CREATED, new Date());
        return generateToken(claims);
    }

    public static String getUsernameFromToken(String token) {
        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            // claims 可以获得Token的过期时间
            username = claims.getSubject();
        } catch (Exception ignored) {}
        return username;
    }

    public static boolean validateToken(String token, String username) {
        String gUsername = getUsernameFromToken(token);
        return StringUtils.endsWith(gUsername, username);
    }

    private static String generateToken(Map<String, Object> claims) {
        Date created = (Date) claims.get(CREATED);
        Date expirationDate = new Date(created.getTime() + TimeUnit.MINUTES.toMillis(EXPIRATION));
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    private static Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception ignored) {}
        return claims;
    }
}
